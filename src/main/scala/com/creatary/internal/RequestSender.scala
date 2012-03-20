/**
 * Copyright 2012 Nokia Siemens Networks
 */
package com.creatary.internal

import dispatch._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import org.apache.http.client.HttpClient
import com.creatary.api.Consumer
import com.creatary.api.Response

object RequestMethod extends Enumeration {
  type RequestMethod = Value
  val GET, POST, PUT, DELETE = Value
}
import RequestMethod._

case class Request(url: String, query: Map[String, String], body: Option[AnyRef] = None, credentials: Option[Consumer] = None,
  method: Option[RequestMethod] = None)

trait HttpClientComponent {
  val httpClient: HttpClient
}

trait RequestExecutor { this: HttpClientComponent =>

  val executor: Http

  class ConfigurableHttp extends Http {
    override def make_client = httpClient
  }

}

/**
 * Request executor using dispatbinder lib
 * @author lukaszjastrzebski
 *
 */
trait RequestSenderComponent { this: RequestExecutor =>

  val sender: RequestSender

  class RequestSender(host: String) extends ErrorHandler with JsonHandler {
    private object Mime extends Enumeration {
      val JSON = Value("application/json")
    }

    def send[T](request: Request, parse: String => T = (param: String) => Serialization.read[Response](param)): T = {
      try {
        val httpRequest = :/(host) / request.url <<? request.query secure
        val fullHttpRequest = appendRequestMethod(request.method, appendCredentials(request.credentials, httpRequest))
        parse(executeRequest(request, fullHttpRequest))
      } catch {
        case e: StatusCode =>
          throw parseException(e)
      }

    }

    private def appendCredentials(credentials: Option[Consumer], httpRequest: dispatch.Request): dispatch.Request = {
      val result = credentials match {
        case Some(consumer) => httpRequest as_! (consumer.key, consumer.secret)
        case None => httpRequest
      }
      result
    }

    private def appendRequestMethod(method: Option[RequestMethod], httpRequest: dispatch.Request): dispatch.Request = {
      val result = method match {
        case Some(DELETE) => httpRequest DELETE
        case Some(PUT) => httpRequest PUT
        case Some(POST) => httpRequest POST
        case None => httpRequest
      }
      result
    }

    private def executeRequest(request: com.creatary.internal.Request, fullHttpRequest: dispatch.Request): RequestSenderComponent.this.executor.HttpPackage[String] = {
      val response = request.body match {
        case Some(body) =>
          executor(fullHttpRequest << (write(body), Mime.JSON.toString()) as_str)
        case None =>
          executor(fullHttpRequest as_str)
      }
      response
    }
  }

}
