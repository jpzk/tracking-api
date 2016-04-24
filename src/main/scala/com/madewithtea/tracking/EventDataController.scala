package com.madewithtea.tracking

import javax.inject.{Inject, Singleton}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import org.joda.time.DateTime

object EventDataController {

  object Protocol {

    case class EventDataRequest(time: Long,
                                site: Option[String],
                                version: Option[String],
                                remoteAdress: Option[String],
                                userAgent: Option[String],
                                cookie: Option[String],
                                fingerprint: Option[String],
                                screen: Option[String],
                                event: Option[String],
                                referer: Option[String])

  }

}

@Singleton
class EventDataController @Inject()(client: CSVFileWriter)
  extends Controller with Logging {

  import EventDataController.Protocol._

  get("/h", name = "pixel_endpoint") { request: Request =>
    track(request)
  }

  def deserialize(request: Request): EventDataRequest = {
    val ip = request.headerMap.get(Config.remoteAddressHeader)
    val ua = request.userAgent
    val site = request.params.get("s")
    val siteversion = request.params.get("v")
    val cookie = request.params.get("u")
    val fingerprint = request.params.get("fp")
    val screen = request.params.get("s")
    val event = request.params.get("e")
    val time = DateTime.now().getMillis
    val referer = request.referer

    EventDataRequest(time, site, siteversion,
      ip, ua, cookie, fingerprint, screen, event, referer)
  }

  def track(request: Request) = {
    val eventDataRequest = deserialize(request)
    client.addValues(eventDataRequest)
  }
}

