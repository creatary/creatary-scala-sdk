package com.creatary.internal;
import dispatch._
import java.text.SimpleDateFormat
import net.liftweb.json._
import com.creatary.api.Response
import com.creatary.api.Status

/**
 * @param error
 * @param cause
 */
class CreataryException(val response: Response, cause: Throwable = null) extends RuntimeException(response.status.message, cause) {
  def this(error: String, cause: Throwable) = this(Response(Status("-1", error)), cause)
}

/**
 * @author lukaszjastrzebski
 *
 */
class OAuthException(val error: String) extends RuntimeException(error)

/**
 * @author lukaszjastrzebski
 *
 */
trait ErrorHandler extends JsonHandler {

  def parseException(error: StatusCode) : Exception = {
    try {
      val response = parse(error.contents).extract[Response]
      new CreataryException(response)
    } catch {
      case _ => parseOAuthException(error)
    }
  }

  def parseOAuthException(error: StatusCode): Exception = {
    try {
      parse(error.contents).extract[OAuthException]
    } catch {
      case e => new CreataryException("cannot extract error", e)
    }

  }

}

