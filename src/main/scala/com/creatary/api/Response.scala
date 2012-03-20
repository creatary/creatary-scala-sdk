/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.api
/**

 *
 */
case class Status(code: String, message: String)

/**
 * Response message towards developer
 * 
 * @author lukaszjastrzebski
 *
 */
case class Response(status: Status)

/**
 * Application consumer/secret
 * @author lukaszjastrzebski
 *
 */
case class Consumer(key: String, secret: String)

