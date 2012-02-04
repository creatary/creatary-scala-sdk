package com.creatary.api
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import com.creatary.internal.Request
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.specs2.mock._
import com.creatary.TestingEnvironment
import com.creatary.TestingEnvironment
import com.creatary.internal.Response
import com.creatary.internal.Status
import scala.Function1
import net.liftweb.json.JsonAST.JValue

/**
 * @author lukaszjastrzebski
 *
 */
@RunWith(classOf[JUnitRunner])
class SmsSenderSpec extends Specification with Mockito {

  "SmsSender" should {
    val smser = new {override val host = "host"} with SmsSender with TestingEnvironment
    val accessToken = "123"
    val onlyBody = Sms("body", null, null)
    
    "throw exception when no access token passed" in {
      smser.send(onlyBody, null) must throwA[Exception]

    }

    "throw exception when no body passed" in {
      smser.send(Sms(null), accessToken) must throwA[Exception]
    }
  }

}