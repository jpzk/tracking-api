package com.madewithtea.tracking.controllers

case class ClientFetchRequest(site: Option[String], version: Option[String],
                              referer: Option[String], useragent: Option[String],
                              remote: String)
