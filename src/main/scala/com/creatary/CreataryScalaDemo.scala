package com.creatary
import com.creatary.api.Sms
import com.creatary.internal.Response
import com.creatary.internal.Status
import com.creatary.api.LocationResponse
import com.creatary.api.ChargeRequest
import com.creatary.api.ChargeRequestMethod._
import com.creatary.internal.CreataryException

object CreataryScalaDemo extends App {

  val creatary = new Creatary("telcoassetmarketplace.com")
  val access_token = "valid_access_token"

  //sending sms
  try {
    val sms = Sms("Hello world")
    creatary.send(sms, access_token)
  } catch {
    case e: CreataryException => println("something wrong")
  }

  //fetching location
  val location = try {
    creatary.findLocation(access_token)
  } catch {
    case e: CreataryException => None
  }

  println(location)

  //requesting charging
  try {
    val chargeReq = ChargeRequest(CODE, charging_code = "10")
    creatary.charge(chargeReq, access_token)
  } catch {
    case e: CreataryException => println("something wrong")
  }
}