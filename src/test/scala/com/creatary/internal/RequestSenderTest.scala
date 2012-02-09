package com.creatary.internal
import com.creatary.TestingEnvironment
import org.junit.Test
import com.creatary.api.Sms
import com.creatary.api.ChargeRequest
import com.creatary.api.ChargeRequestMethod._
import org.apache.http.message.BasicHttpResponse
import org.apache.http.ProtocolVersion
import org.apache.http.entity.StringEntity
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.Matchers
import org.apache.http.HttpHost
import org.apache.http.client.methods.HttpRequestBase
import org.mockito.ArgumentCaptor
import org.junit.Assert._
import org.hamcrest.CoreMatchers.is
import org.apache.http.client.methods.HttpPost
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.HttpGet
import com.creatary.api.LocationResponse
import net.liftweb.json.DefaultFormats
import com.creatary.api.ChargeByCode
import net.liftweb.json.Serialization
import com.creatary.api.Response
import com.creatary.api.Status
import net.liftweb.json.ext.EnumNameSerializer
import com.creatary.api.ChargeRequestMethod
import com.creatary.api.ChargingAddons

class RequestSenderTest extends TestingEnvironment with JsonHandler {

  override val host = "host"
  
  val obj = new RequestSender(host) with ChargingAddons

  @Test
  def should_send_http_request_with_content_sms {
    //given
    val sms = Sms("Hello")
    val request = Request("api", Map("access_token" -> "123"), Some(sms))
    val response = new BasicHttpResponse(new ProtocolVersion("http", 1, 1), 200, "")
    response.setEntity(new StringEntity("""{"status": {"code":"0","message":"ok"}}""", "application/json", "UTF-8"))
    when(httpClient.execute(any(classOf[HttpHost]), any(classOf[HttpRequestBase]))).thenReturn(response)
    //when
    obj.send(request)
    //then
    val host = ArgumentCaptor.forClass(classOf[HttpHost])
    val requestBaseCaptor = ArgumentCaptor.forClass(classOf[HttpRequestBase])
    verify(httpClient) execute (host.capture, requestBaseCaptor.capture)
    assertThat(host.getValue.toURI, is("https://host"))
    val requestBase = requestBaseCaptor.getValue.asInstanceOf[HttpPost]
    assertThat(requestBase.getMethod, is("POST"))
    assertThat(requestBase.getRequestLine.getUri, is("/api?access_token=123"))
    assertThat(EntityUtils.toString(requestBase.getEntity()), is("""{"body":"Hello"}"""))
  }

  @Test
  def should_send_http_error_occured {
    val sms = Sms("Hello")
    val request = Request("api", Map("access_token" -> "123"), Some(sms))
    val response = new BasicHttpResponse(new ProtocolVersion("http", 1, 1), 400, "")
    response.setEntity(new StringEntity("""{"status": {"code":"100","message":"ok"}}""", "application/json", "UTF-8"))
    when(httpClient.execute(any(classOf[HttpHost]), any(classOf[HttpRequestBase]))).thenReturn(response)
    //when
    try {
      obj.send(request)
      fail
    } catch {
      case e: CreataryException =>
        assertThat(e.response, is(Response(Status("100", "ok"))))
      case _ => fail
    }
  }

  @Test
  def should_send_http_request_without_content {
    //given
    val sms = Sms("Hello")
    val request = Request("api", Map("access_token" -> "123"))
    val response = new BasicHttpResponse(new ProtocolVersion("http", 1, 1), 200, "")
    response.setEntity(new StringEntity("""{"status": {"code":"0","message":"ok"},"body":{"latitude":1.493971,"longitude":39.287109,"accuracy":100,"timestamp":1328334368680}}""", "application/json", "UTF-8"))
    when(httpClient.execute(any(classOf[HttpHost]), any(classOf[HttpRequestBase]))).thenReturn(response)
    //when
    obj.send(request, Serialization.read[LocationResponse](_))
    //then
    val host = ArgumentCaptor.forClass(classOf[HttpHost])
    val requestBaseCaptor = ArgumentCaptor.forClass(classOf[HttpRequestBase])
    verify(httpClient) execute (host.capture, requestBaseCaptor.capture)
    assertThat(host.getValue.toURI, is("https://host"))
    val requestBase = requestBaseCaptor.getValue.asInstanceOf[HttpGet]
    assertThat(requestBase.getMethod, is("GET"))
    assertThat(requestBase.getRequestLine.getUri, is("/api?access_token=123"))
  }

  @Test
  def should_send_http_request_with_content_charge_request {
    //given
    val chargeReq = ChargeByCode("servicecode")
    val request = Request("api", Map("access_token" -> "123"), Some(chargeReq))
    val response = new BasicHttpResponse(new ProtocolVersion("http", 1, 1), 200, "")
    response.setEntity(new StringEntity("""{"status": {"code":"0","message":"ok"}}""", "application/json", "UTF-8"))
    when(httpClient.execute(any(classOf[HttpHost]), any(classOf[HttpRequestBase]))).thenReturn(response)
    //when
    obj.send(request)
    //then
    val host = ArgumentCaptor.forClass(classOf[HttpHost])
    val requestBaseCaptor = ArgumentCaptor.forClass(classOf[HttpRequestBase])
    verify(httpClient) execute (host.capture, requestBaseCaptor.capture)
    assertThat(host.getValue.toURI, is("https://host"))
    val requestBase = requestBaseCaptor.getValue.asInstanceOf[HttpPost]
    assertThat(requestBase.getMethod, is("POST"))
    assertThat(requestBase.getRequestLine.getUri, is("/api?access_token=123"))
    assertThat(EntityUtils.toString(requestBase.getEntity()), is("""{"method":"CODE","charging_code":"servicecode"}"""))
  }

}