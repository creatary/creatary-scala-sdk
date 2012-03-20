/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.api
import com.creatary.internal._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito._
import org.junit.Test
import org.junit.Before
import com.creatary.TestingEnvironment
import org.mockito.ArgumentCaptor

class SmsSenderTest {

  val smser = new {override val host = "host"} with SmsSender with TestingEnvironment
  
  val path = "api/2/sms/send"
  val accessToken = "123"
  val onlyBody = Sms("body", None, None)
  val fullSms = Sms("body", Some("from"), Some("transaction_id"))
  val withTransaction = Sms("body", None, Some("transaction_id"))
  val withFrom = Sms("body", Some("from"), None)

  @Test
  def should_call_sender_with_body_only_and_access_token {
    //given
    val request = Request(path, Map("access_token" -> accessToken), Some(onlyBody))
    //when
    smser.send(Sms("body"), accessToken)
    //then
    verify(smser.sender) send (Matchers eq request, Matchers.any())
  }

  @Test
  def should_call_sender_with_full_sms_parameters {
    //given
    val request = Request(path, Map("access_token" -> accessToken), Some(fullSms))
    //when
    smser.send(Sms("body", Some("from"), Some("transaction_id")), accessToken)
    //then
    verify(smser.sender).send(Matchers.eq(request), Matchers.any())
  }

  @Test
  def should_call_sender_with_body_and_from_parameters {
    //given
    val request = Request(path, Map("access_token" -> accessToken), Some(withFrom))
    //when
    smser.send(Sms("body", from = Some("from")), accessToken)
    //then
    verify(smser.sender).send(Matchers.eq(request), Matchers.any())
  }

  @Test
  def should_call_sender_with_body_and_transaction_id_parameters {
    //given
    val request = Request(path, Map("access_token" -> accessToken), Some(withTransaction))
    //when
    smser.send(Sms("body", transaction_id = Some("transaction_id")), accessToken)
    //then
    verify(smser.sender).send(Matchers.eq(request), Matchers.any())
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