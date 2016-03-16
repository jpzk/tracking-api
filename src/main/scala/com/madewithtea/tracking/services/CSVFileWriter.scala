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
  val day = DateTime.now().dayOfMonth()
  var writer = openWriter()

  /**
    * Write the CSV header in new file
    *
    * @param writer
    */
  def writeHeader(writer: CSVWriter) = {
    writer.writeRow(Seq("timestamp","clientid","payloadid","cpuusage"))
  }

  /**
    * Open the file for a given day
    *
    * @return
    */
  def openWriter() = {
    val fmt = DateTimeFormat.forPattern("yyyyMMdd")
    val time = DateTime.now().toString(fmt)
    val dir = Config.CSVDirectory
    val writer = CSVWriter.open(new File(s"$dir/events-$time.csv"))
    writeHeader(writer)
    writer
  }

  /**
    * Adds the values to the CSV file of given hour, if raised exceptions
    * sends back Failure, this will result into resending on the client side
    *
    * @param r
    * @return
    */
  def addValues(r: EventDataRequest): Try[CSVWriterResponse] = {
    try {
      if (DateTime.now().dayOfMonth() != day) {
        writer.close()
        writer = openWriter()
      }
      writer.writeRow(Seq(r.time, r.siteid, r.siteversion, r.cookie, r.fingerprint,
        r.screen, r.event))
      writer.flush()
    } catch { case t: Throwable =>
      return Failure(t)
    }
    Success(CSVWriterResponse())
  }
}
