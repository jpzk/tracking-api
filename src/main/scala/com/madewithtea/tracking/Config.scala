package com.madewithtea.tracking

import java.io.File

import com.typesafe.config.ConfigFactory

object Config {
  val config = ConfigFactory.parseFile(new File("application.conf"))
  lazy val remoteAddressHeader = config.getString("http.remote_address_header")
  lazy val CSVDirectory = config.getString("csv.directory")
  lazy val influxDB = config.getString("influxdb.host")
  lazy val influxDBDatabase = config.getString("influxdb.database")
}
