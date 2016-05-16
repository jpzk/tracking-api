package com.madewithtea.tracking

import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.response.Mustache

@Mustache("user")
case class ClientView(site: String, version: String)

object ClientController {
  object Protocol {
    case class ClientFetchRequest(time: Long,
                                  site: Option[String],
                                  version: Option[String],
                                  referer: Option[String],
                                  useragent: Option[String],
                                  remote: Option[String]) extends TrackingRequest
  }
}

/**
  * Serves the tracking code
  */
class ClientController @Inject()(writer: CSVFileWriter) extends Controller {

  import Common.Protocol._
  import ClientController.Protocol._

  def deserialize(request: Request): ClientFetchRequest = {
    ClientFetchRequest(Common.timestamp(), getSite(request),
      getVersion(request), request.userAgent, request.referer,
      getSite(request))
  }

  get("/user.min.js", name = "fetch_client_endpoint") { request: Request =>
    val clientRequest = deserialize(request)
    writer.write(clientRequest)
    if (clientRequest.site.isDefined && clientRequest.version.isDefined) {
      ClientView(clientRequest.site.get, clientRequest.version.get)
    } else
      response.badRequest()
  }
}
