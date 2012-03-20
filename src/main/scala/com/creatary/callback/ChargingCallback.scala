/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.callback
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Consumes
import javax.ws.rs.core.MediaType._
import com.creatary.api.Transaction

/**
 * Charging callback registerable as a jaxrs service
 * @author lukaszjastrzebski
 *
 */
@Path("/charging")
trait ChargingCallback {

  @POST
  @Path("/callback")
  @Consumes(Array(APPLICATION_JSON))
  def onChargedSubscriptionFee(transaction: Transaction) : AnyRef
}