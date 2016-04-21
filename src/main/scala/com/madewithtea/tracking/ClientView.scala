package com.madewithtea.tracking

import com.twitter.finatra.response.Mustache

@Mustache("user")
case class ClientView(site: String, version: String)
