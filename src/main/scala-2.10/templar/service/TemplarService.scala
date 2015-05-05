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
import spray.json._
import org.slf4j.{LoggerFactory, Logger}
import spray.routing.HttpService

class TemplarServiceActor extends Actor with TemplarService {
	private val logger: Logger = LoggerFactory.getLogger(classOf[TemplarServiceActor])

	def actorRefFactory = context

	def receive = runRoute(templarRoute)
}

trait TemplarService extends HttpService {
	private val logger: Logger = LoggerFactory.getLogger(classOf[TemplarService])

	implicit def executionContext = actorRefFactory.dispatcher

	import TemplarObjects._

	val grokService = new GrokService
	val geoIPService = new GeoIPService

	val templarRoute = {
		pathPrefix("grok") {
			pathPrefix("compile") {
				post {
					entity(as[Pattern]) { pattern =>
						val result = grokService.compile(pattern.pattern)
						logger.debug(result.toJson.toString())
						complete(result.toJson.toString())
					} ~
					get {
						parameter('pattern) { pattern =>
							logger.debug("Request pattern : '{}'", pattern)
							val result = grokService.compile(pattern)
							logger.debug ("Status : {}, Result : {}.", result.status, result.result)
							complete (result.toJson.toString ())
						}
					}
				}
			} ~
			pathPrefix("extract") {
				post {
					entity(as[Extract]) { extract =>
						val result = grokService.extract(extract.data, extract.patternId)
						logger.debug(result.toJson.toString())
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameters('patternId.as[Long], 'data) { (patternId, data) =>
						logger.debug("Request pattern id : '{}', data ; '{}'", patternId, data)
						val result = grokService.extract(data, patternId)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			}
		} ~
		pathPrefix("geoip") {
			pathPrefix("info") {
				post {
					entity(as[IP]) { ip =>
						logger.debug("Request ip : '{}'", ip.ipAddress)
						val result = geoIPService.getGeoIp(ip.ipAddress)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.debug("Request ip : '{}'", ip)
						val result = geoIPService.getGeoIp (ip)
						logger.debug ("Status : {}, Result : {}.", result.status, result.result)
						complete (result.toJson.toString ())
					}
				}
			} ~
			pathPrefix("country") {
				post {
					entity(as[IP]) { ip =>
						logger.debug("Request ip : '{}'", ip.ipAddress)
						val result = geoIPService.getCountry(ip.ipAddress)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.debug("Request ip : '{}'", ip)
						val result = geoIPService.getCountry(ip)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("region") {
				post {
					entity(as[IP]) { ip =>
						logger.debug("Request ip : '{}'", ip.ipAddress)
						val result = geoIPService.getRegion(ip.ipAddress)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.debug("Request ip : '{}'", ip)
						val result = geoIPService.getRegion(ip)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("city") {
				post {
					entity(as[IP]) { ip =>
						logger.debug("Request ip : '{}'", ip.ipAddress)
						val result = geoIPService.getCity(ip.ipAddress)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.debug("Request ip : '{}'", ip)
						val result = geoIPService.getCity(ip)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("organization") {
				post {
					entity(as[IP]) { ip =>
						logger.debug("Request ip : '{}'", ip.ipAddress)
						val result = geoIPService.getOrganization(ip.ipAddress)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.debug("Request ip : '{}'", ip)
						val result = geoIPService.getOrganization(ip)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("location") {
				post {
					entity(as[IP]) { ip =>
						logger.debug("Request ip : '{}'", ip.ipAddress)
						val result = geoIPService.getGeoLocation(ip.ipAddress)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameter('ip) { ip =>
						logger.debug("Request ip : '{}'", ip)
						val result = geoIPService.getGeoLocation(ip)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			} ~
			pathPrefix("distance") {
				post {
					entity(as[IPPair]) { ip =>
						logger.debug("Request from ip : '{}', to ip : '{}'", ip.fromIp, ip.toIp)
						val result = geoIPService.getDistance(ip.fromIp, ip.toIp)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				} ~
				get {
					parameters('from, 'to) { (fromIp, toIp) =>
						logger.debug("Request from ip : '{}', to ip ; '{}'", fromIp, toIp)
						val result = geoIPService.getDistance(fromIp, toIp)
						logger.debug("Status : {}, Result : {}.", result.status, result.result)
						complete(result.toJson.toString())
					}
				}
			}
		}
	}
}
