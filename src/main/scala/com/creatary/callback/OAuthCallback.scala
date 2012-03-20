/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.callback
import javax.ws.rs.POST
import javax.ws.rs.Path

/**
 * OAuth 2.0 callback 
 * @author lukaszjastrzebski
 *
 */
@Path("/oauth")
trait OAuthCallback {

  @POST
  @Path("/callback")
  def onAccessToken(accessToken: String) : AnyRef
}