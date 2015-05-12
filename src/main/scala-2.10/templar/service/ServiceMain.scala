package templar.service

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

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.ConfigFactory
import org.apache.log4j.{RollingFileAppender, PatternLayout, Level, LogManager}
import org.rogach.scallop.ScallopConf
import org.slf4j.{LoggerFactory, Logger}
import spray.can.Http

/**
 * Created by paitoon on 5/1/15 AD.
 */
object ServiceMain {
	private val logger: Logger = LoggerFactory.getLogger("ServiceMain")

	val projectVersion: String = getClass.getPackage.getImplementationVersion

	def main(args: Array[String]): Unit = {
		val opts = new ScallopConf(args) {
			banner("templar-service")
			val version = opt[Boolean]("version", noshort = true, descr = "Print version")
			val help = opt[Boolean]("help", noshort = true, descr = "Show this message")
			val debug = opt[Boolean]("debug", noshort = true, descr = "Print debugging information")
			val silent = opt[Boolean]("silent", noshort = true, descr = "Omit messages to console")
			val log = opt[String]("log", descr = "Enable log to file, default is SERVICE_HOME/logs/templar-service.log")
			val port = opt[Int]("port", descr = "Service port, default is 10000")
		}

		if (opts.version.get.getOrElse(false)) {
			println("templar-service version " + projectVersion)
			System.exit(0)
		}

		if (opts.debug.get.getOrElse(false))
			LogManager.getRootLogger.setLevel(Level.DEBUG)

		if (opts.silent.get.getOrElse(false))
			LogManager.getRootLogger.removeAppender("CONSOLE")

		val logFile = opts.log.get.getOrElse("./logs/templar-service.log")
		val layout: PatternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %-20c{1} - %m%n")
		val fileAppender: RollingFileAppender = new RollingFileAppender(layout, logFile)
		fileAppender.setMaxBackupIndex(5)
		fileAppender.setMaxFileSize("10MB")
		fileAppender.setAppend(true)
		fileAppender.setImmediateFlush(true)
		LogManager.getRootLogger.addAppender(fileAppender)

		val _port = opts.port.get.getOrElse(10000)

		logger.info("templar-service is starting...")

		val config = ConfigFactory.load()

		implicit val system = ActorSystem("Templar", config)
		val service = system.actorOf(Props[TemplarServiceActor], "templar-service")

		IO(Http) ! Http.Bind(service, interface = "localhost", port = _port)

		println("Press any key to exit...")
		val i = Console.in.read()
		system.shutdown();
	}
}
