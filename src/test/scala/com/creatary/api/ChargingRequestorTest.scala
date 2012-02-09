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
  val byAmount = ChargeByAmount(12.2)
  val byCode = ChargeByCode("ablbla")
  val charger = new {override val host = "host"} with ChargingRequestor with TestingEnvironment 
    
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_charge_object {
    charger.charge(null, accessToken)
  }
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_accessToken_object {
    charger.charge(ChargeByCode("10"), null)
  }  
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_code_when_code {
    charger.charge(ChargeByCode(null), accessToken)
  }  
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_amount_when_amount {
    charger.charge(ChargeByAmount(null), accessToken)
  }  
  
  @Test
  def should_charge_by_amount {
    //given
    val request = Request(path, Map("access_token" -> accessToken), Some(byAmount))
    //when
    charger.charge(byAmount, accessToken)
    //then
    verify(charger.sender).send(Matchers.eq(request), Matchers.any())
  }   
  
  @Test
  def should_charge_by_code {
    //given
    val request = Request(path, Map("access_token" -> accessToken), Some(byCode))
    //when
    charger.charge(byCode, accessToken)
    //then
    verify(charger.sender).send(Matchers.eq(request), Matchers.any())
  }  
    
}