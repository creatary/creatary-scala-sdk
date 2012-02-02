package com.creatary.internal
import com.creatary.TestingEnvironment
import org.junit.Test

class RequestSenderTest extends TestingEnvironment {

  override val host = "host"
  val obj = new RequestSender(host)
  
  @Test
  def should_send_http_request {
    
  }
}