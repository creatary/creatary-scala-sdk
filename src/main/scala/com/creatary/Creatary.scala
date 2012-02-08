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

/**
 * @author lukaszjastrzebski
 *
 */
trait ProductionEnvironment extends RequestSenderComponent
  with RequestExecutor with HttpClientComponent {

  val host: String
  val executor = new Http
  val sender = new RequestSender(host)
  val httpClient: HttpClient = null

}

/**
 * @author lukaszjastrzebski
 *
 */
class Creatary(override val host: String, override val consumerCredentials: Consumer) 
extends SmsSender with LocationRetriever
  with ChargingRequestor with TransactionFetcher with ProductionEnvironment
