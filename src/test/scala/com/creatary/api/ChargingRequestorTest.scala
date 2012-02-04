package com.creatary.api

import com.creatary.TestingEnvironment
import org.junit.Test
import com.creatary.api.ChargeRequestMethod._
import com.creatary.internal.Request
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.Matchers
/**
 * @author lukaszjastrzebski
 *
 */
class ChargingRequestorTest {

  val path = "api/2/charge/request"
  val accessToken = "123"
  val byAmount = ChargeRequest(AMOUNT, amount = "12.2")
  val byCode = ChargeRequest(CODE, charging_code = "ablbla")
  val charger = new {override val host = "host"} with ChargingRequestor with TestingEnvironment 
    
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_charge_object {
    charger.charge(null, accessToken)
  }
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_accessToken_object {
    charger.charge(ChargeRequest(CODE, charging_code = "10"), null)
  }  
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_method {
    charger.charge(ChargeRequest(null, "10"), accessToken)
  }  
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_code_when_code {
    charger.charge(ChargeRequest(CODE, amount = "10"), accessToken)
  }  
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_amount_when_amount {
    charger.charge(ChargeRequest(AMOUNT, charging_code = "12"), accessToken)
  }  
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_bad_method {
    charger.charge(ChargeRequest("blabla", charging_code = "12"), accessToken)
  }   
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_amount_and_code {
    charger.charge(ChargeRequest(AMOUNT, charging_code = "12", amount = "12"), accessToken)
  }   
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_when_amount_no_number {
    charger.charge(ChargeRequest(AMOUNT, amount = "ablbla"), accessToken)
  } 
  
  @Test
  def should_charge_by_amount {
    //given
    val request = Request(path, accessToken, Some(byAmount))
    //when
    charger.charge(byAmount, accessToken)
    //then
    verify(charger.sender).send(Matchers.eq(request), any())
  }   
  
  @Test
  def should_charge_by_code {
    //given
    val request = Request(path, accessToken, Some(byCode))
    //when
    charger.charge(byCode, accessToken)
    //then
    verify(charger.sender).send(Matchers.eq(request), any())
  }  
    
}