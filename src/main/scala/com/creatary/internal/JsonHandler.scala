/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.internal
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import java.text.SimpleDateFormat
import net.liftweb.json.ext.EnumSerializer
import net.liftweb.json.ext.EnumNameSerializer

/**
 * Json settings for lift json parser
 * @author lukaszjastrzebski
 *
 */
trait JsonHandler {
  
  import com.creatary.api.ChargeRequestMethod
  
  protected val defaultDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  
  protected implicit def formats : Formats = new DefaultFormats {
    override def dateFormatter = defaultDateFormatter
  } 
  
}