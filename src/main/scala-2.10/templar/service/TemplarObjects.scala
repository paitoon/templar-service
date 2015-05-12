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

import spray.httpx.SprayJsonSupport
import spray.json.{NullOptions, DefaultJsonProtocol}

// For GeoIP
case class IP(ipAddress : String)
case class IPPair(fromIp : String, toIp : String)
case class Country(countryCode : String, countryName : String)
case class Region(region : String, regionName : String)
case class City(city : String, postalCode : String, metroCode : Int, areaCode : Int, timeZone : String)
case class Organization(organization : String)
case class GeoLocation(latitude : Float, longitude : Float)
case class GeoIPInfo(countryCode : String, countryName : String, region : String, regionName : String,
					  city : String, postalCode : String, latitude : Float, longitude : Float, metroCode : Int, areaCode : Int,
					  timeZone : String, organization : String)

// For Grok
case class Pattern(pattern : String)
case class Extract(patternId : Long, data : String)

// For Result
case class Result(status : String, msg : String)

object TemplarObjects extends DefaultJsonProtocol with SprayJsonSupport with NullOptions {
	implicit val ipFormat = jsonFormat1(IP)
	implicit val ipPairFormat = jsonFormat2(IPPair)
	implicit val geoIpFormat = jsonFormat12(GeoIPInfo)
	implicit val countryFormat = jsonFormat2(Country)
	implicit val regionFormat = jsonFormat2(Region)
	implicit val cityFormat = jsonFormat5(City)
	implicit val organizationFormat = jsonFormat1(Organization)
	implicit val geoLocationFormat = jsonFormat2(GeoLocation)

	implicit val patternFormat = jsonFormat1(Pattern)
	implicit val dataFormat = jsonFormat2(Extract)

	implicit val returnFormat = jsonFormat2(Result)
}
