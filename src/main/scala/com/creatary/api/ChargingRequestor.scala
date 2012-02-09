package com.creatary.api
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.Request
import com.creatary.internal.ErrorHandler
import com.creatary.internal.JsonHandler
import net.liftweb.json.ext.EnumNameSerializer
import net.liftweb.json.FieldSerializer

/**
 * @author lukaszjastrzebski
 *
 */
object ChargeRequestMethod extends Enumeration {
  type ChargeRequestMethod = Value
  val CODE, AMOUNT = Value
}

import ChargeRequestMethod._
sealed abstract class ChargeRequest(val method: ChargeRequestMethod) {
  def value: AnyRef
}
/**
 * @author lukaszjastrzebski
 *
 */
case class ChargeByAmount(amount: java.lang.Double) extends ChargeRequest(AMOUNT) {
  override def value = amount
}

case class ChargeByCode(charging_code: String) extends ChargeRequest(CODE) {
  override def value = charging_code
}

trait ChargingAddons extends JsonHandler {
  protected abstract override implicit def formats = super.formats + FieldSerializer[ChargeByAmount]() + FieldSerializer[ChargeByCode]()
}

/**
 * @author lukaszjastrzebski
 *
 */
trait ChargingRequestor { this: RequestSenderComponent =>

  def charge(chargeRequest: ChargeRequest, accessToken: String): Response = {
    require(chargeRequest != null, "chargeRequest cannot be null")
    require(accessToken != null, "accessToken cannot be null")
    require(chargeRequest.method != null, "method cannot be null")
    require(chargeRequest.value != null, "charging value parameter cannot be null")
    val request = Request("api/2/charge/request", Map("access_token" -> accessToken), Some(chargeRequest))
    sender.send(request)
  }

}