package com.creatary.callback
import javax.ws.rs.Path
import javax.ws.rs.POST
import javax.ws.rs.Consumes
import javax.ws.rs.core.MediaType._

case class IncomingSms(to: String, body: String, access_token: String, transaction_id: Option[String], token_secret: Option[String])

@Path("/sms")
trait SmsCallback {

  @POST
  @Path("/callback")
  @Consumes(Array(APPLICATION_JSON))
  def onSms(incoming: IncomingSms) : AnyRef
}