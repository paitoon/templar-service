import java.io.File
import java.net.InetAddress

import com.maxmind.geoip.{timeZone, regionName, LookupService}

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

/**
 * Created by paitoon on 5/3/15 AD.
 */
object GeoIpTest {
	def main(args: Array[String]) {
		val cityService = new LookupService("./geoip/GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE );
		val orgService = new LookupService(("./geoip/GeoIPASNum.dat"))

		//val l2 = cityService.getLocation("111.73.45.49");
		val l2 = cityService.getLocation("192.168.0.1");
		if (l2 == null) {
			println("Not found.")
			return
		}
		println("countryCode: " + l2.countryCode +
			"\n countryName: " + l2.countryName +
			"\n region: " + l2.region +
			"\n regionName: " + regionName.regionNameByCode(l2.countryCode, l2.region) +
			"\n city: " + l2.city +
			"\n postalCode: " + l2.postalCode +
			"\n latitude: " + l2.latitude +
			"\n longitude: " + l2.longitude +
			"\n distance: " + l2.distance(l2) +
			"\n metro code: " + l2.metro_code +
			"\n area code: " + l2.area_code +
			"\n timezone: " + timeZone.timeZoneByCountryAndRegion(l2.countryCode, l2.region));


		println("\n organize: " + orgService.getOrg("111.73.45.49"))
		cityService.close();
		orgService.close();
	}
}
