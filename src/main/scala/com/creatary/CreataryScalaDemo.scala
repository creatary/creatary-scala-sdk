/**
 * Copyright 2012 Nokia Siemens Networks 
 */
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

/**
 * Demo object to represent SDK usage
 * @author lukaszjastrzebski
 *
 */
object CreataryScalaDemo extends App {

  val creatary = new Creatary("telcoassetmarketplace.com", new Consumer("key", "secret"))
  val access_token = "token"

  //sending sms
  try {
    val sms = Sms("Hello world")
    val smsResponse = creatary.send(sms, access_token)
    println(smsResponse)
  } catch {
    case e: CreataryException => 
      println("sms cannot be send: " + e.response)
      e.printStackTrace()
  }

  //fetching location
  val location = try {
    creatary.findLocation(access_token)
  } catch {
    case e: CreataryException =>
      e.printStackTrace()
      None
  }

  println(location)

  //requesting charging
  try {
    val chargeReq = ChargeByCode("1")
    val chargeResponse = creatary.charge(chargeReq, access_token)
    println(chargeResponse)
  } catch {
    case e: CreataryException => 
      println("cannot charge subscriber: " + e.response)
      e.printStackTrace()
  }
  
  //fetching last transactions
  val response = creatary.searchTransaction()
  println(response)
}