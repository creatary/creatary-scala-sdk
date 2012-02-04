package com.creatary
import com.creatary.api.Sms
import com.creatary.internal.Response
import com.creatary.internal.Status
import com.creatary.api.LocationResponse
import com.creatary.api.ChargeRequest
import com.creatary.api.ChargeRequestMethod._

object CreataryScalaDemo extends App {

  val creatary = new Creatary("telcoassetmarketplace.com")
  val access_token = "valid_access_token"
  //sending sms
  val sms = Sms("Hello world")

  creatary.send(sms, access_token) match {
    case Response(Status("0", _)) => println("ok")
    case _ => println("something wrong")
  }

  //fetching location
  val location =
    creatary.findLocation(access_token) match {
      case LocationResponse(Status("0", _), loc) => loc
      case _ => None
    }
  
  println(location)
  
  //requesting charging
  val chargeReq = ChargeRequest(CODE, charging_code = "10")
  
  creatary.charge(chargeReq, access_token) match {
    case Response(Status("0", _)) => println("ok")
    case _ => println("something wrong")
  }  
}