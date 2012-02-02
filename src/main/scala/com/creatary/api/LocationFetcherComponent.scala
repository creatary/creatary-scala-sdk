package com.creatary.api
import com.creatary.internal.Status
import com.creatary.internal.Request
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.ErrorHandler

/**
 * @author lukaszjastrzebski
 *
 */
case class Location(latitude: Double, longitude: Double, accuracy: java.lang.Integer, timestamp: java.lang.Integer)

/**
 * @author lukaszjastrzebski
 *
 */
case class LocationResponse(status: Status, body: Location)

/**
 * @author lukaszjastrzebski
 *
 */
trait LocationFetcherComponent { this: RequestSenderComponent =>

  val localizer: LocationFetcher
  
  class LocationFetcher extends ErrorHandler {
    def retrieveLocation(accessToken: String) = {
      require(accessToken != null, "access_token is required")
      val request = Request("api/2/location/getcoord", accessToken)
      sender.send(request, _.extract[LocationResponse])
    }

  }

}