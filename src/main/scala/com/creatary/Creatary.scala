package com.creatary

import dispatch.Http
import com.creatary.api.LocationFetcherComponent
import com.creatary.api.ChargingRequestorComponent
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.RequestExecutorComponent
import com.creatary.internal.HttpClientComponent
import com.creatary.api.SmsSenderComponent
import org.apache.http.client.HttpClient

/**
 * @author lukaszjastrzebski
 *
 */
trait ProductionEnvironment extends SmsSenderComponent with LocationFetcherComponent
  with ChargingRequestorComponent with RequestSenderComponent 
  with RequestExecutorComponent with HttpClientComponent {
  
  val host: String
  val executor = new Http
  val sender = new RequestSender(host)
  val smser = new SmsSender
  val charger = new ChargingRequestor
  val localizer = new LocationFetcher
  val client: HttpClient = null
  
}

/**
 * @author lukaszjastrzebski
 *
 */
class Creatary(override val host: String) extends ProductionEnvironment
