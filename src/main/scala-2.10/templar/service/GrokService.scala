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
import org.aicer.grok.dictionary.GrokDictionary
import org.aicer.grok.util.Grok
import org.slf4j.{LoggerFactory, Logger}

/**
 * Created by paitoon on 5/2/15 AD.
 */
class GrokService {
	private val logger: Logger = LoggerFactory.getLogger(classOf[GrokService])

	private val grokMap = collection.mutable.Map[Long, Tuple2[String, Grok]]()
	private val dictionary = new GrokDictionary

	dictionary.addBuiltInDictionaries()
	dictionary.bind()

	private var idNext : Long = 0

	private def getNextId() : Long = {
		idNext += 1
		idNext
	}

	def compile(pattern : String) : Return = {
		val grok = dictionary.compileExpression(pattern)
		val id = getNextId
		grokMap += id -> (pattern, grok)
		new Return("OK", id.toString)
	}

	def extract(data : String, withPatternId : Long) : Return = {
		//val line = "111.73.45.49 - - [24/Oct/2013:14:03:32 +0700] \"GET /firstcore/viewcourse.jsp?courseId=154+++++++Result:+chosen+nickname+%22Essepsciedo%22;+success;+Result:+chosen+nickname+%22tisteteEbra%22;+success; HTTP/1.0\" 200 26473 \"http://www.cdgm.co.th/firstcore/viewcourse.jsp?courseId=154+++++++Result:+chosen+nickname+%22Essepsciedo%22;+success;+Result:+chosen+nickname+%22tisteteEbra%22;+success;\" \"Opera/9.80 (Windows NT 5.1; U; ru) Presto/2.10.289 Version/12.00\""

		val compiledGrok = grokMap.get(withPatternId).getOrElse(Tuple2("", null))._2
		if (compiledGrok == null) {
			return new Return("ERR", s"Pattern id : ${withPatternId} is not found.")
		}

		val dataMap = compiledGrok.extractNamedGroups(data)

		val mapper = new ObjectMapper
		val mapString = mapper.writeValueAsString(dataMap)
		new Return("OK", mapString)
	}
}
