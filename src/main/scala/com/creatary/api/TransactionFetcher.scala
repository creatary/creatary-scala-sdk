package com.creatary.api
import com.creatary.internal.RequestSenderComponent
import com.creatary.internal.RequestExecutor
import java.util.Date
import java.lang.Long
import java.lang.Integer
import java.lang.Double
import java.text.SimpleDateFormat
import com.creatary.internal.ErrorHandler
import com.creatary.internal.Request
import com.creatary.internal.JsonHandler
import net.liftweb.json.Serialization.read
import net.liftweb.json.ext.EnumNameSerializer

object TransactionStatus extends Enumeration {
  type TransactionStatus = Value
  val TIMEOUT, CANCELED, COMMITTED, INPROGRESS = Value
}

object TransactionType extends Enumeration {
  type TransactionType = Value
  val SMS, MMS, LOCATION, CHARGING, COVERAGE, PAYMENT = Value
}

object TransactionDirection extends Enumeration {
  type TransactionDirection = Value
  val MO, MT = Value
}

import TransactionStatus._
import TransactionType._
import TransactionDirection._
case class SearchRequest(from: Option[Date] = None, to: Option[Date] = None, status: Option[TransactionStatus] = None,
  `type`: Option[TransactionType] = None, direction: Option[TransactionDirection] = None,
  correlationId: Option[Long] = None, operator: Option[String] = None, subscriber: Option[String] = None,
  limit: Option[Integer] = None)

case class Transaction(uuid: String,
  direction: TransactionDirection, start_timestamp: Date, status: TransactionStatus,
  `type`: TransactionType, earningAmount: Option[Double] = None, earningCurrency: Option[String] = None, 
  operator: Option[String] = None, subscriber: Option[String] = None,
  applicationName: Option[String] = None, failureCause: Option[String] = None, end_timestamp: Option[Date] = None,
  correlationId: Option[Long] = None, subscriberChargeAmountCurrencyCode: Option[String] = None,
  subscriberChargeAmount: Option[Double] = None, subscriberTaxAmountCharged: Option[Double] = None,
  applicationNumber: Option[String] = None, additionalDeveloperData: Option[String] = None)

case class TransactionResponse(status: Status, body: List[Transaction])

/**
 * @author lukaszjastrzebski
 *
 */
trait TransactionFetcher extends JsonHandler { this: RequestSenderComponent =>

  val host: String
  val consumerCredentials: Consumer

  def searchTransaction(search: Option[SearchRequest] = None) = {

    val query = search match {
      case Some(params) => buildMap(params)
      case None => Map[String, String]()
    }
    val request = Request("api/1/transactions", query, credentials = Some(consumerCredentials))
    sender.send(request, read[TransactionResponse](_))

  }

  def buildMap(search: SearchRequest): Map[String, String] = {
    var result = Map[String, String]()
    result = addIfNotNull(result, "from", formatDate(search.from))
    result = addIfNotNull(result, "to", formatDate(search.to))
    result = addIfNotNull(result, "status", search.status)
    result = addIfNotNull(result, "type", search.`type`)
    result = addIfNotNull(result, "direction", search.direction)
    result = addIfNotNull(result, "correlationId", search.correlationId)
    result = addIfNotNull(result, "operator", search.operator)
    result = addIfNotNull(result, "subscriber", search.subscriber)
    result = addIfNotNull(result, "limit", search.limit)
    result
  }

  private def addIfNotNull(result: Map[String, String], key: String, value: Option[AnyRef]) = {
    value match {
      case Some(content) => result + (key -> content.toString())
      case None => result
    }
  }

  def formatDate(date: Option[Date]): Option[String] = {
    date match {
      case Some(content) => Some(defaultDateFormatter.format(content))
      case None => None
    }
  }

}