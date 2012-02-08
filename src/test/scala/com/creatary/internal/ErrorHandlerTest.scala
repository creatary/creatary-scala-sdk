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
    try {
    	obj.parseException(StatusCode(400, ""))
    	fail
    } catch {
      case e: CreataryException => assertThat(e.response, is(unknownError))
      case _ => fail
    }
  }
  
  @Test
  def should_not_parse_error_trying_oauth {
    try {
    	obj.parseOAuthException(StatusCode(400, ""))
    	fail
    } catch {
      case e: CreataryException => assertThat(e.response, is(unknownError))
      case _ => fail
    }
  }  
  
  @Test
  def should_parse_error_trying_oauth {
    try {
    	obj.parseOAuthException(StatusCode(400, """{"error":"invalid_request"}"""))
    	fail
    } catch {
      case e: OAuthException => assertThat(e.error, is("invalid_request"))
      case e => fail
    }
  }
  
  @Test
  def should_forward_parsing_error {
    try {
    	obj.parseException(StatusCode(400, """{"error":"invalid_request"}"""))
    	fail
    } catch {
      case e: OAuthException => assertThat(e.error, is("invalid_request"))
      case e => fail
    }
  }
  
  @Test
  def should_parse_error {
    try {
    	obj.parseException(StatusCode(400, "{\"status\":{\"code\":\"100\",\"message\":\"invalid_request\"}}"))
    	fail
    } catch {
      case e: CreataryException => assertThat(e.response, is(errorResponse))
      case e => fail
    }
  }  
}