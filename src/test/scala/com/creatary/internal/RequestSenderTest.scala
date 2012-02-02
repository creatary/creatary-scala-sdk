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

class RequestSenderTest extends TestingEnvironment {

  protected implicit val formats = DefaultFormats
  override val host = "host"
  val obj = new RequestSender(host)

  @Test
  def should_send_http_request_with_content_sms {
    //given
    val sms = Sms("Hello")
    val request = Request("api", "123", Some(sms))
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
    assertThat(EntityUtils.toString(requestBase.getEntity()), is("{\"body\":\"Hello\",\"from\":null,\"transaction_id\":null}"))
  }

  @Test
  def should_send_http_error_occured {
    val sms = Sms("Hello")
    val request = Request("api", "123", Some(sms))
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
    val request = Request("api", "123")
    val response = new BasicHttpResponse(new ProtocolVersion("http", 1, 1), 200, "")
    response.setEntity(new StringEntity("""{"status": {"code":"0","message":"ok"},"body":{"latitude":"","longitude":"","accuracy":"","timestamp":""}}""", "application/json", "UTF-8"))
    when(httpClient.execute(any(classOf[HttpHost]), any(classOf[HttpRequestBase]))).thenReturn(response)
    //when
    obj.send(request, _.extract[LocationResponse])
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
    val chargeReq = ChargeRequest(CODE, charging_code = "servicecode")
    val request = Request("api", "123", Some(chargeReq))
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
    assertThat(EntityUtils.toString(requestBase.getEntity()), is("{\"method\":\"CODE\",\"amount\":null,\"charging_code\":\"servicecode\"}"))
  }

}