package com.creatary.api
import com.creatary.internal.Request
import com.creatary.internal.Response
import com.creatary.internal.RequestSenderComponent

/**
 * @author lukaszjastrzebski
 *
 */
case class Sms(body: String, from: String = null, transaction_id: String = null)

/**
 * @author lukaszjastrzebski
 *
 */
trait SmsSenderComponent { this: RequestSenderComponent =>

  val smser: SmsSender
  
  class SmsSender {
	  def send(sms: Sms, accessToken: String) = {
	    require(sms != null, "sms cannot be null")
	    require(accessToken != null, "access token is required")
	    require(sms.body != null, "sms body is required")
	    val request = Request("api/2/sms/send", accessToken, Some(sms))
	    sender.send(request)
	  }
  }
  
}