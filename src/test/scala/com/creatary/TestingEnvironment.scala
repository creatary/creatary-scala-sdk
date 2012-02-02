package com.creatary
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.RequestExecutorComponent
import com.creatary.internal.HttpClientComponent
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.apache.http.client.HttpClient
import com.creatary.api.SmsSenderComponent
import com.creatary.api.LocationFetcherComponent
import com.creatary.api.ChargingRequestorComponent

/**
 * @author lukaszjastrzebski
 *
 */
trait TestingEnvironment extends ProductionEnvironment {

  override val httpClient = mock(classOf[HttpClient])
  override val executor = new ConfigurableHttp
  override val sender = mock(classOf[RequestSender])
  override val smser = mock(classOf[SmsSender])
  override val localizer = mock(classOf[LocationFetcher])
  override val charger = mock(classOf[ChargingRequestor])
}