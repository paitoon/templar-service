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

import akka.actor._
import akka.event.Logging
import spray.json._
import spray.routing.HttpServiceActor


class TemplarServiceActor extends HttpServiceActor {
	val logger = Logging(context.system, this)

	import TemplarObjects._

	val grokService : GrokService = TypedActor(context.system).typedActorOf(TypedProps[GrokServiceImpl]())
	val geoIPService : GeoIPService = TypedActor(context.system).typedActorOf(TypedProps[GeoIPServiceImpl]())

	def receive = runRoute {
		pathPrefix("grok") {
			pathPrefix("compile") {
				post {
					entity(as[Pattern]) { pattern =>
						val result = grokService.compile(pattern.pattern)
						logger.info(result.toJson.toString())
						complete(result.toJson.toString())
					} ~
					get {
						parameter('pattern) { pattern =>
							logger.info(s"Request pattern : '$pattern'")
							val result = grokService.compile(pattern)
							logger.info(s"Status : $result.status, Result : $result.msg.")
							complete(result.toJson.toString())
						}
					}
				}
			} ~
			pathPrefix("extract") {
				post {
					entity(as[Extract]) { extract =>
						val result = grokService.extract(extract.data, extract.patternId)
						logger.info(result.toJson.toString())
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameters('patternId.as[Long], 'data) { (patternId, data) =>
						logger.info(s"Request pattern id : '$patternId', data ; '$data'")
						val result = grokService.extract(data, patternId)
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			}
		} ~
		pathPrefix("geoip") {
			pathPrefix("info") {
				post {
					entity(as[IP]) { ip =>
						logger.info(s"Request ip : '$ip.ipAddress'")
						val result = geoIPService.getGeoIP(ip.ipAddress).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.info(s"Request ip : '$ip'")
						val result = geoIPService.getGeoIP(ip).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("country") {
				post {
					entity(as[IP]) { ip =>
						logger.info(s"Request ip : '$ip.ipAddress'")
						val result = geoIPService.getCountry(ip.ipAddress).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.info(s"Request ip : '$ip'")
						val result = geoIPService.getCountry(ip).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("region") {
				post {
					entity(as[IP]) { ip =>
						logger.info(s"Request ip : '$ip.ipAddress'")
						val result = geoIPService.getRegion(ip.ipAddress).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.info(s"Request ip : '$ip'")
						val result = geoIPService.getRegion(ip).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("city") {
				post {
					entity(as[IP]) { ip =>
						logger.info(s"Request ip : '$ip.ipAddress'")
						val result = geoIPService.getCity(ip.ipAddress).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.info(s"Request ip : '$ip'")
						val result = geoIPService.getCity(ip).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("organization") {
				post {
					entity(as[IP]) { ip =>
						logger.info(s"Request ip : '$ip.ipAddress'")
						val result = geoIPService.getOrganization(ip.ipAddress).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.info(s"Request ip : '$ip'")
						val result = geoIPService.getOrganization(ip).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("location") {
				post {
					entity(as[IP]) { ip =>
						logger.info(s"Request ip : '$ip.ipAddress'")
						val result = geoIPService.getGeoLocation(ip.ipAddress).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.info(s"Request ip : '$ip'")
						val result = geoIPService.getGeoLocation(ip).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("distance") {
				post {
					entity(as[IPPair]) { ip =>
						logger.info(s"Request from ip : '$ip.fromIp', to ip ; '$ip.toIp'")
						val result = geoIPService.getDistance(ip.fromIp, ip.toIp).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameters('from, 'to) { (fromIp, toIp) =>
						logger.info(s"Request from ip : '$fromIp', to ip ; '$toIp'")
						val result = geoIPService.getDistance(fromIp, toIp).getOrElse(Result("ERR", "Time out."))
						logger.info(s"Status : $result.status, Result : $result.msg.")
						complete(result.toJson.toString())
					}
				}
			}
		}
	}
}
