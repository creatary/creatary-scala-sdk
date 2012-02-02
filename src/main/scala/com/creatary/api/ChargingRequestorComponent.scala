package com.creatary.api
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.Request
import com.creatary.api.ChargeRequestMethod._

/**
 * @author lukaszjastrzebski
 *
 */
case class ChargeRequest(method: String, amount: String = null, charging_code: String = null)

/**
 * @author lukaszjastrzebski
 *
 */
object ChargeRequestMethod {
  val CODE = "CODE"
  val AMOUNT = "AMOUNT"
}
/**
 * @author lukaszjastrzebski
 *
 */
trait ChargingRequestorComponent { this: RequestSenderComponent =>

  val charger: ChargingRequestor

  class ChargingRequestor {
    def charge(chargeRequest: ChargeRequest, accessToken: String) = {
      require(chargeRequest != null, "chargeRequest cannot be null")
      require(accessToken != null, "accessToken cannot be null")
      require(chargeRequest.method != null, "method cannot be null")
      require((chargeRequest.method == CODE && chargeRequest.charging_code != null && chargeRequest.amount == null) 
          || (chargeRequest.method == AMOUNT && parsableToDouble(chargeRequest.amount) && chargeRequest.charging_code == null), 
          "code or amount must be supplied")
      val request = Request("api/2/charge/request", accessToken, Some(chargeRequest))
      sender.send(request)
    }
    
    private def parsableToDouble(amount: String) = {
      try{
    	amount.toDouble
    	true
      } catch {
        case e => false
      } 
    }
  }

}