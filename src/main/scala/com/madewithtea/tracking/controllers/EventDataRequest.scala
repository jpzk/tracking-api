package com.madewithtea.tracking.controllers

case class EventDataRequest(time: Long, siteid: String, siteversion: String,
                            cookie: String, fingerprint: String, screen: String, event: String)

