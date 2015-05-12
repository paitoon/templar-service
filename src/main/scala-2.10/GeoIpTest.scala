import java.io.BufferedReader

import scala.io.Source

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

object GeoIpTest {
	var br: BufferedReader = null

	def streamLine(head : String) : Stream[String] = {
		/*
		(head != null) match {
			case false => Stream.empty
			case true => streamLine(br.readLine())
		}
		*/
		head #:: streamLine(br.readLine())
	}

	def infiniteList(head : Int) : Stream[Int] = {
		head #:: infiniteList(head + 1)
	}

	def main(args: Array[String]): Unit = {
		br = Source.fromFile("/Users/paitoon/logs/sample.log").bufferedReader()
		var sline : Stream[String] = null
		var line = br.readLine()
		do {
			sline = streamLine(br.readLine())
			println(sline.head)
		} while (sline.head != null)
	}
}
