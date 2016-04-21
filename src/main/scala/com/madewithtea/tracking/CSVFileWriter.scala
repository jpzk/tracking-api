package com.madewithtea.tracking

import java.io._
import javax.inject.Singleton
import com.github.tototoshi.csv.CSVWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Singleton
class CSVFileWriter {

  import EventDataController.Protocol._

  val na = "N/A"

  def addValues(r: EventDataRequest): Unit = {
    val fmt = DateTimeFormat.forPattern("yyyyMMdd")
    val time = DateTime.now().toString(fmt)
    val dir = Config.CSVDirectory
    val writer = CSVWriter.open(new File(s"$dir/events-$time.csv"), append = true)
    writer.writeRow(Seq(r.time,
      r.site.getOrElse(na),
      r.version.getOrElse(na),
      r.remoteAdress,
      r.userAgent.getOrElse(na),
      r.cookie.getOrElse(na),
      r.fingerprint.getOrElse(na),
      r.screen.getOrElse(na),
      r.referer.getOrElse(na),
      r.event.getOrElse(na)))
    writer.close()
  }
}
