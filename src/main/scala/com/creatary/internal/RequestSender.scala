package com.creatary.internal

import dispatch._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import org.apache.http.client.HttpClient
import com.creatary.api.Consumer
import com.creatary.api.Response

case class Request(url: String, query: Map[String, String], body: Option[AnyRef] = None, credentials: Option[Consumer] = None)

trait HttpClientComponent {
  val httpClient: HttpClient
}

trait RequestExecutor { this: HttpClientComponent =>

  val executor: Http

  class ConfigurableHttp extends Http {
    override def make_client = httpClient
  }

}

trait RequestSenderComponent { this: RequestExecutor =>

  val sender: RequestSender
  class RequestSender(host: String) extends ErrorHandler with JsonHandler {
    private object Mime extends Enumeration {
      val JSON = Value("application/json")
    }

    def send[T](request: Request, parse: String => T = (param: String) => Serialization.read[Response](param)) : T = {
      try {
        val httpRequest = :/(host) / request.url <<? request.query secure
        val authReq = request.credentials match {
          case Some(consumer) => httpRequest as_! (consumer.key, consumer.secret)
          case None => httpRequest
        }
        val response = request.body match {
          case Some(body) =>
            executor(authReq << (write(body), Mime.JSON.toString()) as_str)
          case None =>
            executor(authReq as_str)
        }
        parse(response)
      } catch {
        case e: StatusCode =>
          throw parseException(e)
      }

    }
  }

}
