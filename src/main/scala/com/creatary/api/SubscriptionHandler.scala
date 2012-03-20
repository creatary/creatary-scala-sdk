/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.api
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.Request
import com.creatary.internal.RequestMethod.DELETE

/**
 * Subscription charging API
 * 
 * @author lukaszjastrzebski
 *
 */
trait SubscriptionHandler { this: RequestSenderComponent =>

  def unsubscribe(accessToken: String) = {
    require(accessToken != null, "access token is required")
    val request = Request("api/2/subscriberlifecycle/unsubscribe", Map("access_token" -> accessToken), None, method = Some(DELETE))
    sender.send(request)
  }
}