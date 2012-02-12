package com.creatary

import dispatch.Http
import com.creatary.api.LocationRetriever
import com.creatary.api.ChargingRequestor
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.RequestExecutor
import com.creatary.internal.HttpClientComponent
import com.creatary.api.SmsSender
import org.apache.http.client.HttpClient
import com.creatary.api.TransactionFetcher
import com.creatary.api.Consumer
import com.creatary.api.ChargingAddons
import com.creatary.internal.EnumerationSerializer
import com.creatary.internal.JsonHandler
import com.creatary.api.TransactionStatus
import com.creatary.api.TransactionType
import com.creatary.api.TransactionDirection
import com.creatary.api.ChargeRequestMethod
import com.creatary.api.SubscriptionHandler

trait EnumerationsAddon extends JsonHandler {
  protected abstract override implicit def formats = super.formats +
    new EnumerationSerializer(TransactionStatus, TransactionType, TransactionDirection, ChargeRequestMethod)
}

/**
 * @author lukaszjastrzebski
 *
 */
trait ProductionEnvironment extends RequestSenderComponent
  with RequestExecutor with HttpClientComponent with EnumerationsAddon {

  val host: String
  val executor = new Http
  val sender: RequestSender = new RequestSender(host) with ChargingAddons
  val httpClient: HttpClient = null

}

/**
 * @author lukaszjastrzebski
 *
 */
class Creatary(override val host: String, override val consumerCredentials: Consumer)
  extends SmsSender with LocationRetriever
  with ChargingRequestor with TransactionFetcher with SubscriptionHandler with ProductionEnvironment
