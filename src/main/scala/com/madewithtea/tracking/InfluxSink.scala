package com.madewithtea.tracking

import com.madewithtea.tracking.controllers.{ClientFetchRequest, EventDataRequest}
import com.paulgoldbaum.influxdbclient.Parameter.Precision
import com.paulgoldbaum.influxdbclient._
import com.twitter.inject.Logging
import org.joda.time.DateTime
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * The InfluxSink is used to push the measurements to InfluxDB.
  */
object InfluxSink extends Logging {
  val na = "N/A"
  val influxdb = InfluxDB.connect(Config.InfluxDB, 8086)
  val db = influxdb.selectDatabase(Config.InfluxDBDatabase)

  /**
    * Writing the fetch, which is asked to the ClientController
    */
  def writeFetch(request: ClientFetchRequest) = {
    val point = new Point("clientfetch", DateTime.now().getMillis)
      .addTag("useragent", request.useragent.getOrElse(na))
      .addTag("site", request.site.getOrElse(na))
      .addTag("version", request.version.getOrElse(na))
      .addField("remote", request.remote)
    db.write(point, precision = Precision.MILLISECONDS)
      .recover { case e: Exception =>
        logger.error(e.getMessage)
      }
  }

  /**
    * Writing event data, which is sent to the EventDataController
    *
    * @param eventDataRequest
    */
  def writeEventData(eventDataRequest: EventDataRequest) = {
    val point = new Point("visit", eventDataRequest.time)
      .addTag("siteid", eventDataRequest.site.getOrElse(na))
      .addTag("siteversion", eventDataRequest.version.getOrElse(na))
      .addTag("event", eventDataRequest.event.getOrElse(na))
      .addTag("cookie", eventDataRequest.cookie.getOrElse(na))
      .addField("fingerprint", eventDataRequest.fingerprint.getOrElse(na))
      .addField("useragent", eventDataRequest.userAgent.getOrElse(na))
      .addField("remote", eventDataRequest.remoteAdress)
      .addField("screen", eventDataRequest.screen.getOrElse(na))
    db.write(point, precision = Precision.MILLISECONDS)
      .recover { case e: Exception =>
        logger.error(e.getMessage)
      }
  }
}
