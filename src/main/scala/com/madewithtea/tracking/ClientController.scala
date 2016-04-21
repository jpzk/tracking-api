package com.madewithtea.tracking

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

object ClientController {
  object Protocol {
    case class ClientFetchRequest(site: Option[String],
                                  version: Option[String],
                                  referer: Option[String],
                                  useragent: Option[String],
                                  remote: Option[String])
  }
}

class ClientController extends Controller {

  import ClientController.Protocol._

  def deserialize(request: Request): ClientFetchRequest = {
    ClientFetchRequest(request.params.get("s"),
      request.params.get("v"),
      request.userAgent,
      request.referer,
      request.headerMap.get(Config.remoteAddressHeader))
  }

  get("/user.min.js", name = "fetch_client_endpoint") { request: Request =>
    val clientRequest = deserialize(request)
    if (clientRequest.site.isDefined && clientRequest.version.isDefined) {
      ClientView(clientRequest.site.get, clientRequest.version.get)
    } else
      response.badRequest()
  }
}
