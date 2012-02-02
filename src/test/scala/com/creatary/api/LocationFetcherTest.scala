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
class LocationFetcherTest extends ErrorHandler with TestingEnvironment  {

  override val host = "host"
    
  val path = "api/2/location/getcoord"
  val accessToken = "123"
  val ok = Status("0", "ok")
  val location = Location(1.0, 1.0, 1, 1)
    
  val obj = new LocationFetcher
    
  @Test
  def should_fetch_location {
    //given
    val request = Request(path, accessToken, None)
    val response = LocationResponse(ok, location)
    doReturn(response) when(sender) send(Matchers eq request, any())
    
    //when
    val result: LocationResponse = obj.retrieveLocation(accessToken).asInstanceOf[LocationResponse]
    //then
    assertThat(result, is(response))
  }
  
  @Test(expected = classOf[IllegalArgumentException])
  def should_throw_exception_no_access_token_passed {
    //given
    
    //when
    obj.retrieveLocation(null)
    //then
  }
}