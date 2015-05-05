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

import com.fasterxml.jackson.databind.ObjectMapper
import com.maxmind.geoip.LookupService
import org.slf4j.{LoggerFactory, Logger}

import collection.JavaConversions._

/**
 * Created by paitoon on 5/3/15 AD.
 */
class GeoIPService {
	private val logger: Logger = LoggerFactory.getLogger(classOf[GeoIPService])

	private val cityService = new LookupService("./geoip/GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE);
	private val orgService = new LookupService("./geoip/GeoIPASNum.dat", LookupService.GEOIP_MEMORY_CACHE)

	def getGeoIp(ipAddress : String) : Return = {
		val location = cityService.getLocation(ipAddress);

		if (location == null) {
			new Return("ERR", "Not found.")
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
			new Return("OK", mapString)
		}
	}

	def getCountry(ipAddress : String) : Return = {
		val location = cityService.getLocation(ipAddress);

		if (location == null) {
			new Return("ERR", "Not found.")
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "countryCode" -> location.countryCode
			dataMap += "countryName" -> location.countryName

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)
			new Return("OK", mapString)
		}
	}

	def getRegion(ipAddress : String) : Return = {
		val location = cityService.getLocation(ipAddress);

		if (location == null) {
			new Return("ERR", "Not found.")
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "region" -> location.region
			dataMap += "regionName" -> com.maxmind.geoip.regionName.regionNameByCode(location.countryCode, location.region)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)
			new Return("OK", mapString)
		}
	}

	def getCity(ipAddress : String) : Return = {
		val location = cityService.getLocation(ipAddress);

		if (location == null) {
			new Return("ERR", "Not found.")
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
			new Return("OK", mapString)
		}
	}

	def getOrganization(ipAddress : String) : Return = {
		val location = cityService.getLocation(ipAddress);
		var org : Organization = null

		if (location == null) {
			new Return("ERR", "Not found.")
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "organization" -> orgService.getOrg(ipAddress)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)
			new Return("OK", mapString)
		}
	}

	def getGeoLocation(ipAddress : String) : Return = {
		val location = cityService.getLocation(ipAddress);

		if (location == null) {
			new Return("ERR", "Not found.")
		}
		else {
			val dataMap = new java.util.LinkedHashMap[String, Any]()

			dataMap += "ip" -> ipAddress
			dataMap += "latitude" -> location.latitude
			dataMap += "longitude" -> location.longitude

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)
			new Return("OK", mapString)
		}
	}

	def getDistance(fromIpAddress : String, toIpAddress : String) : Return = {
		val fromLocation = cityService.getLocation(fromIpAddress);
		val toLocation = cityService.getLocation(toIpAddress);
		val distance = fromLocation.distance(toLocation)

		val dataMap = new java.util.LinkedHashMap[String, Any]()

		dataMap += "fromip" -> fromIpAddress
		dataMap += "toip" -> toIpAddress
		dataMap += "distance" -> distance

		val mapper = new ObjectMapper
		val mapString = mapper.writeValueAsString(dataMap)
		new Return("OK", mapString)
	}
}
