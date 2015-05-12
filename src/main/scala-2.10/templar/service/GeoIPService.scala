/*
 *
 *  * (C) Copyright 2015 Paitoon Cheewinsiriwat
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the GNU Lesser General Public License
 *  * (LGPL) version 2.1 which accompanies this distribution, and is available at
 *  * http://www.gnu.org/licenses/lgpl-2.1.html
 *  *
 *  * This library is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * Lesser General Public License for more details.
 *  *
 *
 */

package templar.service

import akka.actor.{ActorLogging, TypedActor}
import akka.event.{LogSource, Logging}
import com.fasterxml.jackson.databind.ObjectMapper
import com.maxmind.geoip.LookupService

import collection.JavaConversions._

case class IPNotFoundException(cause : String) extends Exception

trait GeoIPService {
	def getGeoIP(ipAddress : String) : Option[Result]
	def getCountry(ipAddress : String) : Option[Result]
	def getRegion(ipAddress : String) : Option[Result]
	def getCity(ipAddress : String) : Option[Result]
	def getOrganization(ipAddress : String) : Option[Result]
	def getGeoLocation(ipAddress : String) : Option[Result]
	def getDistance(fromIpAddress : String, toIpAddress : String) : Option[Result]
}

object GeoIPServiceImpl {
	implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
		def genString(o: AnyRef): String = o.getClass.getName
		override def getClazz(o: AnyRef): Class[_] = o.getClass
	}
}

class GeoIPServiceImpl extends GeoIPService {
	val logger = Logging(TypedActor.context.system, this)

	private val cityService = new LookupService("./geoip/GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE);
	private val orgService = new LookupService("./geoip/GeoIPASNum.dat", LookupService.GEOIP_MEMORY_CACHE)

	override def getGeoIP(ipAddress: String): Option[Result] = {
		val location = cityService.getLocation(ipAddress)

		if (location == null) {
			Some(Result("ERR", "Not found."))
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "countryCode" -> location.countryCode
			dataMap += "countryName" -> location.countryName
			dataMap += "region" -> location.region
			dataMap += "regionName" -> com.maxmind.geoip.regionName.regionNameByCode(location.countryCode, location.region)
			dataMap += "city" -> location.city
			dataMap += "postalCode" -> location.postalCode
			dataMap += "latitude" -> location.latitude
			dataMap += "longitude" -> location.longitude
			dataMap += "metroCode" -> location.metro_code
			dataMap += "areaCode" -> location.area_code
			dataMap += "timeZone" -> com.maxmind.geoip.timeZone.timeZoneByCountryAndRegion(location.countryCode, location.region)
			dataMap += "organization" -> orgService.getOrg(ipAddress)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}

	override def getCountry(ipAddress: String): Option[Result] = {
		val location = cityService.getLocation(ipAddress)

		if (location == null) {
			Some(Result("ERR", "Not found."))
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "countryCode" -> location.countryCode
			dataMap += "countryName" -> location.countryName

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}

	override def getRegion(ipAddress: String): Option[Result] = {
		val location = cityService.getLocation(ipAddress)

		if (location == null) {
			Some(Result("ERR", "Not found."))
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "region" -> location.region
			dataMap += "regionName" -> com.maxmind.geoip.regionName.regionNameByCode(location.countryCode, location.region)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}

	override def getCity(ipAddress: String): Option[Result] = {
		val location = cityService.getLocation(ipAddress)

		if (location == null) {
			Some(Result("ERR", "Not found."))
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "city" -> location.city
			dataMap += "postalCode" -> location.postalCode
			dataMap += "metroCode" -> location.metro_code
			dataMap += "areaCode" -> location.area_code
			dataMap += "timeZone" -> com.maxmind.geoip.timeZone.timeZoneByCountryAndRegion(location.countryCode, location.region)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}


	override def getOrganization(ipAddress: String): Option[Result] = {
		val location = cityService.getLocation(ipAddress)
		var org : Organization = null

		if (location == null) {
			Some(Result("ERR", "Not found."))
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "organization" -> orgService.getOrg(ipAddress)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}

	override def getGeoLocation(ipAddress: String): Option[Result] = {
		val location = cityService.getLocation(ipAddress)

		if (location == null) {
			Some(Result("ERR", "Not found."))
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "latitude" -> location.latitude
			dataMap += "longitude" -> location.longitude

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}

	override def getDistance(fromIpAddress: String, toIpAddress: String): Option[Result] = {
		val fromLocation = cityService.getLocation(fromIpAddress)
		val toLocation = cityService.getLocation(toIpAddress)
		val distance = fromLocation.distance(toLocation)

		val dataMap = new java.util.LinkedHashMap[String, Any]()

		dataMap += "fromip" -> fromIpAddress
		dataMap += "toip" -> toIpAddress
		dataMap += "distance" -> distance

		val mapper = new ObjectMapper
		val mapString = mapper.writeValueAsString(dataMap)

		Some(Result("OK", mapString))
	}
}
