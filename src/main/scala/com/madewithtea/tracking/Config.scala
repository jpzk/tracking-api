package com.madewithtea.tracking

import java.io.File

import com.typesafe.config.ConfigFactory

object Config {
  val config = ConfigFactory.parseFile(new File("application.conf"))
  def CSVDirectory = config.getString("csv-directory")
}
