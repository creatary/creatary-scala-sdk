/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.api
import com.creatary.internal.Request
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.ErrorHandler
import net.liftweb.json.Serialization.read
import com.creatary.internal.JsonHandler
/**

 *
 */
case class Location(latitude: Double, longitude: Double, accuracy: java.lang.Integer, timestamp: java.lang.Integer)

case class LocationResponse(status: Status, body: Location)

/**
 * Fetching location of subscriber API
 * 
 * @author lukaszjastrzebski
 *
 */
trait LocationRetriever extends JsonHandler { this: RequestSenderComponent =>

  def findLocation(accessToken: String) = {
    require(accessToken != null, "access_token is required")
    val request = Request("api/2/location/getcoord", Map("access_token" -> accessToken))
    sender.send(request, read[LocationResponse](_))
    
  }

}