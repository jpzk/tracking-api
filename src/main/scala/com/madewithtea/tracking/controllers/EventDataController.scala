package com.madewithtea.tracking.controllers

import javax.inject.{Inject, Singleton}
import com.madewithtea.tracking.services.{CouldNotWriteValues, CSVFileWriter, WarehouseService}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import org.joda.time.DateTime
import scala.util.{Failure, Success}

@Singleton
class EventDataController @Inject()(client: CSVFileWriter)
  extends Controller with Logging {

  val warehouseService = new WarehouseService(client)

  get("/h", name = "old_pixel_endpoint") { request: Request =>
    track(request)
  }

  get("/track", name = "pixel_endpoint") { request: Request =>
    track(request)
  }

  /**
    * Convert GET parameters to EventDataRequest object
    *
    * @param request
    * @return
    */
  def deserialize(request: Request): EventDataRequest = {
    val na = "N/A"
    val site = request.getParam("s", na) // if not available: N/A
    val siteversion = request.getParam("v", na)
    val cookie = request.getParam("u", na)
    val fingerprint = request.getParam("fp", na)
    val screen = request.getParam("s", na)
    val event = request.getParam("e", na)
    val time = DateTime.now().getMillis

    EventDataRequest(time, site, siteversion,
      cookie, fingerprint, screen, event)
  }

  def track(request: Request) = {
    warehouseService(deserialize(request)).flatMap { promise =>
      promise match {
        case Success(result) => {
          response.ok.body("").toFuture
        }
        case Failure(e: CouldNotWriteValues) =>
          warn("Could not write values; ask client for resending")
          response.internalServerError.toFuture
        case Failure(e) =>
          warn("Some unexpected error when writing values")
          response.internalServerError.toFuture
      }
    }
  }


}

