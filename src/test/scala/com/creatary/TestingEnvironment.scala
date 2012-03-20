/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.RequestExecutor
import com.creatary.internal.HttpClientComponent
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.apache.http.client.HttpClient

/**
 *
 */
trait TestingEnvironment extends RequestSenderComponent
  with RequestExecutor with HttpClientComponent with EnumerationsAddon {
  val host: String
  override val httpClient = mock(classOf[HttpClient])
  override val executor = new ConfigurableHttp
  override val sender = mock(classOf[RequestSender])
}