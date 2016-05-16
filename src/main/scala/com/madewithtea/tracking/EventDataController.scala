package com.madewithtea.tracking

import javax.inject.{Inject, Singleton}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

object EventDataController {

  object Protocol {

    case class EventDataRequest(time: Long,
                                site: Option[String],
                                version: Option[String],
                                remote: Option[String],
                                userAgent: Option[String],
                                cookie: Option[String],
                                fingerprint: Option[String],
                                screen: Option[String],
                                event: Option[String],
                                referer: Option[String]) extends TrackingRequest

  }
}

@Singleton
class EventDataController @Inject()(writer: CSVFileWriter)
  extends Controller with Logging {

  import Common.Protocol._
  import EventDataController.Protocol._

  def deserialize(request: Request): EventDataRequest = {
    EventDataRequest(Common.timestamp(),
      getSite(request), getVersion(request), getRemoteAddress(request),
      request.userAgent, getCookie(request), getFingerprint(request),
      getScreenRes(request), getEvent(request), request.referer)
  }

  get("/h", name = "pixel_endpoint") { request: Request =>
    writer.write(deserialize(request))
  }
}

