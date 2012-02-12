package com.creatary.callback.lift
import net.liftweb.http.rest.RestHelper
import net.liftweb.http.S
import net.liftweb.common.Full
import com.creatary.callback.SubscriberLifecycleCallback
import com.creatary.callback.SmsCallback
import com.creatary.callback.ChargingCallback
import com.creatary.callback.OAuthCallback
import net.liftweb.common.Logger
import net.liftweb.json.JString
import com.creatary.callback._
import com.creatary.api._
import com.creatary.internal.EnumerationSerializer
import com.creatary.internal.JsonHandler

trait CreataryRestService extends RestHelper with OAuthCallback with ChargingCallback
  with SmsCallback with SubscriberLifecycleCallback with Logger with JsonHandler {

  override implicit def formats = super.formats +
    new EnumerationSerializer(TransactionStatus, TransactionType, TransactionDirection, SubscriptionChannel, InvokerType,
        SubscriberLifecycleType)

  serve {
    case "creatary" :: "oauth" :: "callback" :: _ Post _ =>
      S.param("code") match {
        case Full(code) => {
          onAccessToken(code)
        }
        case _ => info("no code parameter from creatary")
      }
      JString("ok")
    case "creatary" :: "sms" :: "callback" :: _ JsonPost json =>
      onSms(json._1.extract[IncomingSms])
      JString("ok")

    case "creatary" :: "lifecycle" :: "callback" :: _ JsonPost json =>
      onUnsubscribe(json._1.extract[UnsubscribeMessage])
      JString("ok")

    case "creatary" :: "charging" :: "callback" :: _ JsonPost json =>
      onChargedSubscriptionFee(json._1.extract[Transaction])
      JString("ok")
  }
}