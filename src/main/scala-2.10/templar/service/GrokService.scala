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

import akka.actor.TypedActor
import akka.event.{LogSource, Logging}
import com.fasterxml.jackson.databind.ObjectMapper
import org.aicer.grok.dictionary.GrokDictionary
import org.aicer.grok.util.Grok

trait GrokService {
	def compile(pattern : String) : Option[Result]
	def extract(data : String, withPatternId : Long) : Option[Result]
}

object GrokServiceImpl {
	implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
		def genString(o: AnyRef): String = o.getClass.getName
		override def getClazz(o: AnyRef): Class[_] = o.getClass
	}
}

class GrokServiceImpl extends GrokService {
	val logger = Logging(TypedActor.context.system, this)

	private val grokMap = collection.mutable.Map[Long, Tuple2[String, Grok]]()
	private val dictionary = new GrokDictionary

	dictionary.addBuiltInDictionaries()
	dictionary.bind()

	private var idNext : Long = 0

	private def getNextId() : Long = {
		idNext += 1
		idNext
	}

	override def compile(pattern: String): Option[Result] = {
		val grok = dictionary.compileExpression(pattern)
		val id = getNextId
		grokMap += id -> (pattern, grok)

		Some(Result("OK", id.toString))
	}

	override def extract(data: String, withPatternId: Long): Option[Result] = {
		val compiledGrok = grokMap.get(withPatternId).getOrElse(Tuple2("", null))._2
		if (compiledGrok == null) {
			Some(Result("ERR", s"Pattern id : ${withPatternId} is not found."))
		}
		else {
			val dataMap = compiledGrok.extractNamedGroups(data)

			val mapper = new ObjectMapper
			val mapString = mapper.writeValueAsString(dataMap)

			Some(Result("OK", mapString))
		}
	}
}
