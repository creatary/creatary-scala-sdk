package com.creatary.callback.lift

import net.liftweb.http.rest.RestHelper
import com.creatary.callback.OAuthCallback
import net.liftweb.common.Logger
import net.liftweb.http.S
import com.creatary.internal.JsonHandler
import net.liftweb.common.Full
import net.liftweb.json.JString
import akka.dispatch.Future
import akka.dispatch.Await
import akka.dispatch.{ ExecutionContext, Promise }
import akka.util.duration._
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.Executors



trait CreataryOAuthHelper extends RestHelper with OAuthCallback with TaskCompletionLogger {

  implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
  
  serve {
    case "creatary" :: "oauth" :: "callback" :: _ Post _ =>
      S.param("code") match {
        case Full(code) => {
          Future(onAccessToken(code)) onComplete(logCompletion)
        }
        case _ => info("no code parameter from creatary")
      }
      JString("ok")
  }
}