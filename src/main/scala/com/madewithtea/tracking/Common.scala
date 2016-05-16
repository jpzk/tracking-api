package com.madewithtea.tracking

import com.twitter.finagle.http.Request
import org.joda.time.DateTime

class TrackingRequest

object Common {

  object Protocol {
    def getSite(request: Request) = request.params.get("s")
    def getVersion(request: Request) = request.params.get("v")
    def getCookie(request: Request) = request.params.get("u")
    def getFingerprint(request: Request) = request.params.get("fp")
    def getScreenRes(request: Request) = request.params.get("sr")
    def getEvent(request: Request) = request.params.get("e")
    def getRemoteAddress(request: Request) = request.headerMap
        .get(Config.remoteAddressHeader)
  }

  def timestamp() = DateTime.now().getMillis
}
