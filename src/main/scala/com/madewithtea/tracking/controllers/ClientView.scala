package com.madewithtea.tracking.controllers

import com.twitter.finatra.response.Mustache

@Mustache("user")
case class ClientView(site: String, version: String)
