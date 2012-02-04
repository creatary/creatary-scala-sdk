package com.creatary.api
import com.creatary.TestingEnvironment
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers.is
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.Matchers
import com.creatary.internal.Request
import com.creatary.internal.Status
import com.creatary.internal.ErrorHandler
import net.liftweb.json.JValue
/**
 * @author lukaszjastrzebski
 *
 */
class LocationRetrieverTest extends ErrorHandler {

  val path = "api/2/location/getcoord"
  val accessToken = "123"
  val ok = Status("0", "ok")
  val location = Location(1.0, 1.0, 1, 1)
    
  val localiser = new {override val host = "host"} with LocationRetriever with TestingEnvironment
    
  @Test
  def should_fetch_location {
    //given
    val request = Request(path, accessToken, None)
    val response = LocationResponse(ok, location)
    doReturn(response) when(localiser.sender) send(Matchers eq request, any())
    
    //when
    val result: LocationResponse = localiser.findLocation(accessToken).asInstanceOf[LocationResponse]
    //then
    assertThat(result, is(response))
  }
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_access_token_passed {
    //given
    
    //when
    localiser.findLocation(null)
    //then
  }
}