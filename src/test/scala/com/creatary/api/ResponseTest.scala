/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.api
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers.is
import net.liftweb.json._
import net.liftweb.json.Serialization._
import com.creatary.EnumerationsAddon

class ResponseTest extends ChargingAddons with EnumerationsAddon {

  val smsResp = """ {"status": {"code":"0","message":"ok"}} """

  val locResp = """ {"status": {"code":"0","message":"ok"}, "body": {"lat":123.423}} """

  val tranResp = """ {"status": {"code":"0","message":"ok"}, "body": [{"picture":"xxx"}]} """

  val chargeReq1 = """{"method":"AMOUNT","amount":10.0}"""

  val chargeReq2 = """{"method":"CODE","charging_code":"xxx"}"""

  @Test
  def should_deserialize_by_amount {
    val result = read[ChargeByAmount](chargeReq1)
    assertThat(result, is(ChargeByAmount(10.0)))
  }
  
  @Test
  def should_deserialize_by_code {
    val result = read[ChargeByCode](chargeReq2)
    assertThat(result, is(ChargeByCode("xxx")))
  }

  @Test
  def should_serialize_by_amount {
    val json = write(ChargeByAmount(10.0))
    assertThat(json, is(chargeReq1))
  }

  @Test
  def should_serialize_by_code {
    val json = write(ChargeByCode("xxx"))
    assertThat(json, is(chargeReq2))
  }
}