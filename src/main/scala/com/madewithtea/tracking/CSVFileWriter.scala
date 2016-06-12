package com.madewithtea.tracking

import java.io._
import javax.inject.Singleton
import com.github.tototoshi.csv.CSVWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Singleton
class CSVFileWriter {

  import ClientController.Protocol._
  import EventDataController.Protocol._

  val na = "N/A"

  private def escape(string: String) = string.replaceAllLiterally(",",";")
  private def writeEventDataRequest(writer: CSVWriter, request: EventDataRequest) = {
    writer.writeRow(Seq[String](request.time.toString,
      request.site.getOrElse(na),
      request.version.getOrElse(na),
      request.remote.getOrElse(na),
      request.userAgent.getOrElse(na),
      request.cookie.getOrElse(na),
      request.fingerprint.getOrElse(na),
      request.screen.getOrElse(na),
      request.referer.getOrElse(na),
      request.event.getOrElse(na)).map(escape))
  }

  private def writeClientFetch(writer: CSVWriter, request: ClientFetchRequest) = {
    writer.writeRow(Seq[String](request.time.toString,
      request.site.getOrElse(na),
      request.version.getOrElse(na),
      request.remote.getOrElse(na),
      request.referer.getOrElse(na),
      request.useragent.getOrElse(na)).map(escape))
  }

  def write(r: TrackingRequest) = {
    val fmt = DateTimeFormat.forPattern("yyyyMMdd")
    val time = DateTime.now().toString(fmt)
    val dir = Config.CSVDirectory
    r match {
      case request: ClientFetchRequest =>
        val writer = CSVWriter.open(new File(s"$dir/fetch-$time.csv"), append = true)
        writeClientFetch(writer, request)
        writer.close()
      case request: EventDataRequest =>
        val writer = CSVWriter.open(new File(s"$dir/events-$time.csv"), append = true)
        writeEventDataRequest(writer, request)
        writer.close()
    }
  }
}
