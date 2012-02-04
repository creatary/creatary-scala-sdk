package com.creatary.internal;
import dispatch._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

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
trait ErrorHandler {
  protected implicit val formats = DefaultFormats

  def throwException[T](error: StatusCode) : T = {
    try {
      val response = parse(error.contents).extract[Response]
      throw new CreataryException(response)
    } catch {
      case e: CreataryException => throw e
      case _ => throwOAuthException(error)
    }
  }

  def throwOAuthException[T](error: StatusCode): T = {
    try {
      throw parse(error.contents).extract[OAuthException]
    } catch {
      case e: OAuthException => throw e
      case e => throw new CreataryException("cannot extract error", e)
    }

  }

}

