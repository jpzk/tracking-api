package com.madewithtea.tracking.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class ClientController extends Controller {
  get("/user.min.js", name = "fetch_client_endpoint") { request: Request =>
    val site = request.params.get("s")
    val version = request.params.get("v")
    if(site.isDefined && version.isDefined)
      ClientView(site.get, version.get)
    else
      response.badRequest()
  }
}
