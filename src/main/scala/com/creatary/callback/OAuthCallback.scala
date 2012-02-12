package com.creatary.callback
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/oauth")
trait OAuthCallback {

  @POST
  @Path("/callback")
  def onAccessToken(accessToken: String) : AnyRef
}