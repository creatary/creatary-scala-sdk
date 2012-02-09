package com.creatary.api
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers.is
import com.creatary.TestingEnvironment
import net.liftweb.json._
import net.liftweb.json.Serialization._
import java.text.SimpleDateFormat
import com.creatary.EnumerationsAddon
import com.creatary.internal.Request
import org.mockito.Mockito._
import org.mockito.Matchers
import java.util.Date

class TransactionFetcherTest extends EnumerationsAddon {

  val ok_result = """
  {"type" : "SMS",
    "status" : "COMMITTED",
    "direction" : "MT",
    "operator" : "yyy",
    "correlationId" : -1,
    "subscriber" : "xxx",
    "applicationName" : "Test application",
    "start_timestamp" : "2011-10-13 16:05:14",
    "end_timestamp" : "2011-10-13 16:05:15",
    "uuid" : "576ca95e-8f6e-4f3d-b219-a61ffc33723c",
    "subscriberChargeAmountCurrencyCode" : "USD",
    "subscriberChargeAmount" : 0.0,
    "subscriberTaxAmountCharged" : 0.0,
    "earningAmount" : 0.0,
    "earningCurrency" : "USD",
    "applicationNumber" : "555" 
  }"""
  
  val failure_result = """
  {"type" : "LOCATION",
    "status" : "CANCELED",
    "failureCause" : "ServiceUnavailableException",
    "direction" : "MT",
    "operator" : "yyy",
    "correlationId" : -1,
    "subscriber" : "xxx",
    "applicationName" : "Test application",
    "start_timestamp" : "2011-08-31 20:40:46",
    "end_timestamp" : "2011-08-31 20:40:48",
    "uuid" : "0e06bcbd-298b-4f94-88a9-f4ef44e55982",
    "earningAmount" : 0.0,
    "earningCurrency" : "USD",
    "applicationNumber" : "122"
  }"""
    
  val search = new {
    override val host = "host" 
  	override val consumerCredentials = Consumer("12", "12")} 
  with TransactionFetcher with TestingEnvironment
  
  val dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz")
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val path = "api/1/transactions"
  
  @Test
  def should_search_all_transactions {
	//given
    val request = Request(path, Map[String, String](), None, Some(Consumer("12", "12")))
    //when
    search.searchTransaction()
    //then
    verify(search.sender) send (Matchers eq request, Matchers.any())
  }
  
  @Test
  def should_search_transactions_by_date {
    //given
    val startDate = new Date()
    val request = Request(path, Map("from" -> dateFormat.format(startDate)), None, Some(Consumer("12", "12")))
    
    //when
    search.searchTransaction(Some(SearchRequest(from = Some(startDate))))
    //then
    verify(search.sender) send (Matchers eq request, Matchers.any())
  }
  
  @Test
  def should_search_transactions_by_enums {
    //given
    val request = Request(path, Map("status" -> "COMMITTED"), None, Some(Consumer("12", "12")))
    //when
    search.searchTransaction(Some(SearchRequest(status = Some(TransactionStatus.COMMITTED))))
    //then
    verify(search.sender) send (Matchers eq request, Matchers.any())
  }
  
  @Test
  def should_get_empty_transaction {
    //given
    val body = Transaction(uuid = "123", direction = TransactionDirection.MO, start_timestamp = new Date(), 
        status = TransactionStatus.COMMITTED, `type` = TransactionType.CHARGING)
    val response = TransactionResponse(Status("0", "ok"), body :: Nil)
    doReturn(response) when(search.sender) send (Matchers.any(), Matchers.any())
    //when
    val result = search.searchTransaction()
    //then
    assertThat(result, is(response))
  }
  
  @Test
  def should_deserialize_failure_result {
    val transaction = Transaction(
	    `type` = TransactionType.LOCATION, status = TransactionStatus.CANCELED, 
	    direction = TransactionDirection.MT, operator = Some("yyy"), 
	    correlationId = Some(-1), subscriber = Some("xxx"), applicationName = Some("Test application"), 
	    start_timestamp = dateParser.parse("2011-08-31 20:40:46UTC"), 
	    end_timestamp = Some(dateParser.parse("2011-08-31 20:40:48UTC")), subscriberChargeAmountCurrencyCode = None, 
	    uuid = "0e06bcbd-298b-4f94-88a9-f4ef44e55982",
	    subscriberChargeAmount = None, subscriberTaxAmountCharged = None, earningAmount = Some(0), 
	    earningCurrency = Some("USD"), applicationNumber = Some("122"), failureCause = Some("ServiceUnavailableException"))
	val result = read[Transaction](failure_result)
	assertThat(result, is(transaction))
  }
  
  @Test
  def should_deserialize_ok_result {
	val transaction = Transaction(
	    `type` = TransactionType.SMS, status = TransactionStatus.COMMITTED, 
	    direction = TransactionDirection.MT, operator = Some("yyy"), 
	    correlationId = Some(-1), subscriber = Some("xxx"), applicationName = Some("Test application"), 
	    start_timestamp = dateParser.parse("2011-10-13 16:05:14UTC"), 
	    end_timestamp = Some(dateParser.parse("2011-10-13 16:05:15UTC")), subscriberChargeAmountCurrencyCode = Some("USD"), 
	    uuid = "576ca95e-8f6e-4f3d-b219-a61ffc33723c",
	    subscriberChargeAmount = Some(0), subscriberTaxAmountCharged = Some(0), earningAmount = Some(0), 
	    earningCurrency = Some("USD"), applicationNumber = Some("555"), failureCause = None)
	val result = read[Transaction](ok_result)
	assertThat(result, is(transaction))
  }
}