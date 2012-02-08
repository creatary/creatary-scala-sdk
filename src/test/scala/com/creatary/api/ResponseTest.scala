package com.creatary.api
import org.junit.Test
import java.text.SimpleDateFormat
import com.creatary.internal.JsonHandler
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import com.creatary.api.ChargeRequestMethod._
import net.liftweb.json.ext.EnumNameSerializer

trait ChargingAddons extends JsonHandler {
  protected abstract override implicit def formats = super.formats + new EnumNameSerializer(ChargeRequestMethod) + FieldSerializer[ChargeByAmount]() + FieldSerializer[ChargeByCode]()
}

class ResponseTest extends ChargingAddons {

  val smsResp = """ {"status": {"code":"0","message":"ok"}} """

  val locResp = """ {"status": {"code":"0","message":"ok"}, "body": {"lat":123.423}} """

  val tranResp = """ {"status": {"code":"0","message":"ok"}, "body": [{"picture":"xxx"}]} """

  val chargeReq1 = """{"method":"AMOUNT","amount":10.0}"""

  val chargeReq2 = """{"method":"CODE","charging_code":"xxx"}"""

  val parser = new JsonHandler {}

  @Test
  def should_deserialize = {
    println(write(ChargeByAmount(10.0)))
    println(write(ChargeByCode("xxx")))
    println(Serialization.read[ChargeByAmount](chargeReq1))
    println(Serialization.read[ChargeByCode](chargeReq2))
  }
}