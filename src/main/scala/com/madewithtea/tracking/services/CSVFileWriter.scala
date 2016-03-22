package com.madewithtea.tracking.services

import java.io._
import javax.inject.Singleton
import com.madewithtea.tracking.Config
import com.madewithtea.tracking.controllers.EventDataRequest
import com.github.tototoshi.csv.CSVWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.util.{Failure, Success, Try}

case class CSVWriterResponse()

@Singleton
class CSVFileWriter {
  /**
    * Adds the values to the CSV file of given hour, if raised exceptions
    * sends back Failure, this will result into resending on the client side
    *
    * @param r
    * @return
    */
  def addValues(r: EventDataRequest): Try[CSVWriterResponse] = {
    try {
      val fmt = DateTimeFormat.forPattern("yyyyMMdd")
      val time = DateTime.now().toString(fmt)
      val dir = Config.CSVDirectory
      val writer = CSVWriter.open(new File(s"$dir/events-$time.csv"), append = true)
      writer.writeRow(Seq(r.time, r.site, r.version,
        r.remoteAdress, r.userAgent, r.cookie, r.fingerprint,
        r.screen, r.event))
      writer.close()
    } catch {
      case t: Throwable =>
        return Failure(t)
    }
    Success(CSVWriterResponse())
  }
}
