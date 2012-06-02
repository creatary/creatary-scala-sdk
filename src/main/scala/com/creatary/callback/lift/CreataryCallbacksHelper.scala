/**
 * Copyright 2012 Nokia Siemens Networks
 */
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
import akka.dispatch.ExecutionContext
import java.util.concurrent.Executors
import akka.dispatch.Future
import net.liftweb.json.JValue
import net.liftweb.http.Req

/**
 * Lift based implementation of rest service for creatary api
 * @author lukaszjastrzebski
 *
 */
trait CreataryCallbacksHelper extends RestHelper with ChargingCallback
  with SmsCallback with SubscriberLifecycleCallback with TaskCompletionLogger with JsonHandler {

  protected trait AnyResponse {
    def testResponse_?(r: Req): Boolean = true
  }

  protected lazy val JsonContentPost = new TestPost[JValue] with AnyResponse with JsonBody

  override implicit def formats = super.formats +
    new EnumerationSerializer(TransactionStatus, TransactionType, TransactionDirection, SubscriptionChannel, InvokerType,
      SubscriberLifecycleType)

  implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  serve {
    case "creatary" :: "sms" :: "callback" :: _ JsonContentPost json =>
      Future(onSms(json._1.extract[IncomingSms])) onComplete (logCompletion)
      JString("ok")

    case "creatary" :: "lifecycle" :: "callback" :: _ JsonContentPost json =>
      Future(onUnsubscribe(json._1.extract[UnsubscribeMessage])) onComplete (logCompletion)
      JString("ok")

    case "creatary" :: "charging" :: "callback" :: _ JsonContentPost json =>
      Future(onChargedSubscriptionFee(json._1.extract[Transaction])) onComplete (logCompletion)
      JString("ok")
  }
}