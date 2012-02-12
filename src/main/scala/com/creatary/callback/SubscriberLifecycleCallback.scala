package com.creatary.callback
import javax.ws.rs.Path
import javax.ws.rs.POST
import javax.ws.rs.Consumes
import javax.ws.rs.core.MediaType._

object SubscriberLifecycleType extends Enumeration {
  type SubscriberLifecycleType = Value
  val UNSUBSCRIBE, TOKEN_EXPIRED, SUBSCRIBER_LEAVES = Value
}

object SubscriptionChannel extends Enumeration {
  type SubscriptionChannel = Value
  val WEB, SMS, REST = Value
}

object InvokerType extends Enumeration {
  type InvokerType = Value
  val DEVELOPER, SUBSCRIBER = Value
}

import SubscriberLifecycleType._
import SubscriptionChannel._
import InvokerType._

case class UnsubscribeMessage(channel: SubscriptionChannel, invoker: InvokerType, applicationName: String,
    notificationType: SubscriberLifecycleType, accessTokens: Option[List[String]], reason: Option[String])

@Path("/lifecycle")
trait SubscriberLifecycleCallback {

  @POST
  @Path("/callback")
  @Consumes(Array(APPLICATION_JSON))
  def onUnsubscribe(unsubscribeMessage: UnsubscribeMessage) : AnyRef
}