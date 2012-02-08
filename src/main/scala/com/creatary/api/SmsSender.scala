package com.creatary.api
import com.creatary.internal.Request
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.ErrorHandler

/**
 * @author lukaszjastrzebski
 *
 */
case class Sms(body: String, from: Option[String] = None, transaction_id: Option[String] = None)

/**
 * @author lukaszjastrzebski
 *
 */
trait SmsSender { this: RequestSenderComponent =>

  def send(sms: Sms, accessToken: String) : Response = {
    require(sms != null, "sms cannot be null")
    require(accessToken != null, "access token is required")
    require(sms.body != null, "sms body is required")
    val request = Request("api/2/sms/send", Map("access_token" -> accessToken), Some(sms))
    sender.send(request)
  }
  
}