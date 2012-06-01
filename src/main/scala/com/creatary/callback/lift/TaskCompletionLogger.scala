package com.creatary.callback.lift

import net.liftweb.common.Logger

trait TaskCompletionLogger extends Logger {

  def logCompletion[T]: PartialFunction[Either[Throwable, T], Unit] = {
    case Right(value) =>
      info("finished Processing callback" + value)
    case Left(exception) =>
      error("Cannot process the message " + exception.getMessage)
  }
}