package com.creatary
import com.creatary.api.Sms
import com.creatary.api.Response
import com.creatary.api.Status
import com.creatary.api.ChargeRequest
import com.creatary.api.ChargeRequestMethod._
import com.creatary.internal.CreataryException
import com.creatary.api.SearchRequest
import com.creatary.api.ChargeByCode
import com.creatary.api.Consumer

object CreataryScalaDemo extends App {

  val creatary = new Creatary("telcoassetmarketplace.com", new Consumer("j17z3thnvwvj6uoy", "e7obz84fcog5ziso"))
  val access_token = "830d41d2d6d085e2b7126ac4a40418a2"

//  val response = creatary.searchTransaction()
//  println(response.body)
  //sending sms
  try {
    val sms = Sms("Hello world")
    creatary.send(sms, access_token)
  } catch {
    case e: CreataryException => println("sms cannot be send: " + e.response)
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
    val chargeReq = ChargeByCode("10")
    creatary.charge(chargeReq, access_token)
  } catch {
    case e: CreataryException => println("cannot charge subscriber: " + e.response)
  }
}