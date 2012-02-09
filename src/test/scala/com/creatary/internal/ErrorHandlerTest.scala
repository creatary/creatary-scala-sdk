package com.creatary.internal
import dispatch.StatusCode
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import com.creatary.api.Response
import com.creatary.api.Status

class ErrorHandlerImpl extends ErrorHandler

class ErrorHandlerTest {

  val unknownError = Response(Status("-1", "cannot extract error"))
  val errorResponse = Response(Status("100", "invalid_request"))

  val obj = new ErrorHandlerImpl

  @Test
  def should_not_parse_error {
    val exception = obj.parseException(StatusCode(400, "")).asInstanceOf[CreataryException]
    assertThat(exception.response, is(unknownError))
  }

  @Test
  def should_not_parse_error_trying_oauth {
    val exception = obj.parseOAuthException(StatusCode(400, "")).asInstanceOf[CreataryException]
    assertThat(exception.response, is(unknownError))
  }

  @Test
  def should_parse_error_trying_oauth {
    val exception = obj.parseOAuthException(StatusCode(400, """{"error":"invalid_request"}"""))
    assertThat(exception, is(OAuthException("invalid_request").asInstanceOf[Exception]))
  }

  @Test
  def should_forward_parsing_error {
    val exception = obj.parseException(StatusCode(400, """{"error":"invalid_request"}"""))
    assertThat(exception, is(OAuthException("invalid_request").asInstanceOf[Exception]))
  }

  @Test
  def should_parse_error {
    val exception = obj.parseException(StatusCode(400, "{\"status\":{\"code\":\"100\",\"message\":\"invalid_request\"}}"))
    assertThat(exception, is(CreataryException(errorResponse).asInstanceOf[Exception]))
  }
}