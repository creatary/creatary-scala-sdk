package com.creatary.api

/**
 * @author lukaszjastrzebski
 *
 */
case class Status(code: String, message: String)

/**
 * @author lukaszjastrzebski
 *
 */
case class Response(status: Status)


/**
 * @author lukaszjastrzebski
 *
 */
case class Consumer(key: String, secret: String)

