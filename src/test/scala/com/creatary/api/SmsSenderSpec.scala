package com.creatary.api
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import com.creatary.internal.Request
import org.mockito.Matchers
import org.specs2.mock._
import com.creatary.TestingEnvironment

/**
 * @author lukaszjastrzebski
 *
 */
@RunWith(classOf[JUnitRunner])
class SmsSenderSpec extends Specification with TestingEnvironment with Mockito {

  override val host = "host"

  "SmsSender" should {
    val obj = new SmsSender
    val accessToken = "123"
    val path = "api/2/sms/send"
    val onlyBody = Sms("body", null, null)
    val fullSms = Sms("body", "from", "transaction_id")
    val withTransaction = Sms("body", null, "transaction_id")
    val withFrom = Sms("body", "from", null)

    "call sender with body only and access token" in {
      val request = Request(path, accessToken, Some(onlyBody))
      obj.send(Sms("body"), accessToken)
      there was one(sender).send(Matchers.eq(request), Matchers.any())
    }

    "call sender with full sms parameters" in {
      val request = Request(path, accessToken, Some(fullSms))
      obj.send(fullSms, accessToken)
      there was one(sender).send(Matchers.eq(request), Matchers.any())
    }

    "call sender with body and from parameters" in {
      val request = Request(path, accessToken, Some(withFrom))
      obj.send(Sms("body", from = "from"), accessToken)
      there was one(sender).send(Matchers.eq(request), Matchers.any())
    }

    "call sender with body and transaction_id parameters" in {
      val request = Request(path, accessToken, Some(withTransaction))
      obj.send(Sms("body", transaction_id = "transaction_id"), accessToken)
      there was one(sender).send(Matchers.eq(request), Matchers.any())
    }

    "throw exception when no access token passed" in {
      obj.send(onlyBody, null) must throwA[Exception]

    }

    "throw exception when no body passed" in {
      obj.send(Sms(null), accessToken) must throwA[Exception]
    }
  }

}