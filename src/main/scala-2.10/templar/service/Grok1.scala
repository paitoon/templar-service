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

import scala.collection.mutable
import scala.io.Source

/**
 * Grok Name Regular Expression
 */
class Grok1 {
	private var _patternMap = new mutable.HashMap[String, String]()

	def patternMap = _patternMap

	def addPatternsFromFile(patternFile : String) : Int = {
		Source.fromFile(patternFile).getLines()
											.map(_.trim)
											.filter(line => (line.length > 0 && line.charAt(0) != '#'))
											.map(_.split(' '))
											.filter(arr => (arr(0).length > 0 || arr(1).length > 0))
											.map(pair => pair(0) -> pair(1))
											.foreach(_patternMap.+=)
		_patternMap.size
	}

	def addPatternsFromFolder(patternFolder : String) : Int = {
		-1
	}

	def loadBuiltInPatterns() = {

	}

	def bind() = {}

	/**
	 * Digests the original expression into a pure named regex
	 *
	 * @param originalExpression
	 */
	/*
	private def digestExpressionAux(originalExpression : String): String = {
		val PATTERN_START: String = "%{"
		val PATTERN_STOP: String = "}"
		val PATTERN_DELIMITER: Char = ':'

		while (originalExpression.indexOf(PATTERN_START) >= 0 && originalExpression.indexOf(PATTERN_STOP, PATTERN_START_INDEX + PATTERN_START.length) >= 0) {
			val PATTERN_START_INDEX: Int = originalExpression.indexOf(PATTERN_START)
			val PATTERN_STOP_INDEX: Int = originalExpression.indexOf(PATTERN_STOP, PATTERN_START_INDEX + PATTERN_START.length)
			if (PATTERN_START_INDEX < 0 || PATTERN_STOP_INDEX < 0) {
				break //todo: break is not supported
			}
			val grokPattern: String = originalExpression.substring(PATTERN_START_INDEX + PATTERN_START.length, PATTERN_STOP_INDEX)
			val PATTERN_DELIMITER_INDEX: Int = grokPattern.indexOf(PATTERN_DELIMITER)
			var regexName: String = grokPattern
			var groupName: String = null
			if (PATTERN_DELIMITER_INDEX >= 0) {
				regexName = grokPattern.substring(0, PATTERN_DELIMITER_INDEX)
				groupName = grokPattern.substring(PATTERN_DELIMITER_INDEX + 1, grokPattern.length)
			}
			val dictionaryValue: String = regexDictionary.get(regexName)
			if (dictionaryValue == null) {
				throw new GrokCompilationException("Missing value for regex name : " + regexName)
			}
			if (dictionaryValue.contains(PATTERN_START)) {
				break //todo: break is not supported
			}
			var replacement: String = dictionaryValue
			if (null != groupName) {
				replacement = "(?<" + groupName + ">" + dictionaryValue + ")"
			}
			originalExpression = new StringBuilder(originalExpression).replace(PATTERN_START_INDEX, PATTERN_STOP_INDEX + PATTERN_STOP.length, replacement).toString
		}
		return originalExpression
	}

	private def digestExpressions {
		var wasModified = true


		while (wasModified) {
			wasModified = false
			for (entry <- regexDictionary.entrySet) {
				val originalExpression: String = entry.getValue
				val digestedExpression: String = digestExpressionAux(originalExpression)
				wasModified = (originalExpression ne digestedExpression)
				if (wasModified) {
					entry.setValue(digestedExpression)
					break //todo: break is not supported
				}
			}
		}
	}
	*/
}
