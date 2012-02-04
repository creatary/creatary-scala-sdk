package com.creatary
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.RequestExecutor
import com.creatary.internal.HttpClientComponent
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.apache.http.client.HttpClient

/**
 * @author lukaszjastrzebski
 *
 */
trait TestingEnvironment extends ProductionEnvironment {

  override val httpClient = mock(classOf[HttpClient])
  override val executor = new ConfigurableHttp
  override val sender = mock(classOf[RequestSender])
}