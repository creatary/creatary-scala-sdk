package com.creatary.internal

import dispatch._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import org.apache.http.client.HttpClient

case class Request(url: String, accessToken: String, body: Option[AnyRef] = None)

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

  class RequestSender(host: String) extends ErrorHandler {
    private object Mime extends Enumeration {
      val JSON = Value("application/json")
    }

    def send(request: Request, func: JValue => AnyRef = _.extract[Response]) = {
      try {
        val uri = :/(host) / request.url
        val httpQuery = uri.secure <<? Map("access_token" -> request.accessToken)
        val response = request.body match {
          case None => executor(httpQuery as_str)
          case Some(body) =>
            val httpRequest = httpQuery << (write(body), Mime.JSON.toString())
            executor(httpRequest as_str)
        }
        func(parse(response))
      } catch {
        case e: StatusCode =>
          throwException(e)
      }

    }
  }

}
