package com.madewithtea.tracking.controllers

import com.madewithtea.tracking.Config
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.joda.time.DateTime

class ClientController extends Controller {

  def informInflux(request: Request) = {
    import com.paulgoldbaum.influxdbclient._
    val influxdb = InfluxDB.connect(Config.InfluxDB, 8086)
    val db = influxdb.selectDatabase(Config.InfluxDBDatabase)
    val point = new Point("fetch", DateTime.now().getMillis)
    db.write(point)
  }

  get("/user.min.js", name = "fetch_client_endpoint") { request: Request =>
    informInflux(request)
    val site = request.params.get("s")
    val version = request.params.get("v")
    if(site.isDefined && version.isDefined)
      ClientView(site.get, version.get)
    else
      response.badRequest()
  }
}
