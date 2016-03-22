package com.madewithtea.tracking.controllers

import com.madewithtea.tracking.InfluxSink
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class ClientController extends Controller {

  def deserialize(request: Request): ClientFetchRequest = {
    val site = request.params.get("s")
    val version = request.params.get("v")
    ClientFetchRequest(site,
      version,
      request.userAgent,
      request.remoteAddress.toString)
  }

  /**
    * This endpoint serves the JS tracking script
    */
  get("/user.min.js", name = "fetch_client_endpoint") { request: Request =>
    val clientRequest = deserialize(request)
    if(clientRequest.site.isDefined && clientRequest.version.isDefined) {
      InfluxSink.writeFetch(clientRequest)
      ClientView(clientRequest.site.get, clientRequest.version.get)
    } else
      response.badRequest()
  }
}
