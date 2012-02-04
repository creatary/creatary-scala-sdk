package com.creatary.api
import com.creatary.internal._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito._
import org.junit.Test
import org.junit.Before
import com.creatary.TestingEnvironment
import org.mockito.ArgumentCaptor

/**
 * @author lukaszjastrzebski
 *
 */
class SmsSenderTest {

  val smser = new {override val host = "host"} with SmsSender with TestingEnvironment
  
  val path = "api/2/sms/send"
  val accessToken = "123"
  val onlyBody = Sms("body", null, null)
  val fullSms = Sms("body", "from", "transaction_id")
  val withTransaction = Sms("body", null, "transaction_id")
  val withFrom = Sms("body", "from", null)

  @Test
  def should_call_sender_with_body_only_and_access_token {
    //given
    val request = Request(path, accessToken, Some(onlyBody))
    //when
    smser.send(Sms("body"), accessToken)
    //then
    verify(smser.sender) send (Matchers eq request, any())
  }

  @Test
  def should_call_sender_with_full_sms_parameters {
    //given
    val request = Request(path, accessToken, Some(fullSms))
    //when
    smser.send(Sms("body", "from", "transaction_id"), accessToken)
    //then
    verify(smser.sender).send(Matchers.eq(request), any())
  }

  @Test
  def should_call_sender_with_body_and_from_parameters {
    //given
    val request = Request(path, accessToken, Some(withFrom))
    //when
    smser.send(Sms("body", from = "from"), accessToken)
    //then
    verify(smser.sender).send(Matchers.eq(request), any())
  }

  @Test
  def should_call_sender_with_body_and_transaction_id_parameters {
    //given
    val request = Request(path, accessToken, Some(withTransaction))
    //when
    smser.send(Sms("body", transaction_id = "transaction_id"), accessToken)
    //then
    verify(smser.sender).send(Matchers.eq(request), any())
  }

  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_when_no_access_token_passed {
    smser.send(Sms("body"), null)
  }

  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_when_no_body_passed {
    smser.send(Sms(null), "123")
  }

}