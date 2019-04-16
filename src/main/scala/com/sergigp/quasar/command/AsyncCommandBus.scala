package com.sergigp.quasar.command

import scala.concurrent.Future
import scala.reflect.ClassTag

import org.slf4j.Logger

class AsyncCommandBus[C <: Command](logger: Logger) extends CommandBus[Future, C] {

  private var handlers = Map.empty[Class[_], C => Future[Either[C#CommandError, Unit]]]

  override def publish(command: C): Future[Either[C#CommandError, Unit]] =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handler(command)
      case None          => Future.failed(CommandHandlerNotFound(command.getClass.getSimpleName))
    }

  override def subscribe[H <: C: ClassTag](handler: H => Future[Either[H#CommandError, Unit]]): Unit = {
    val classTag = implicitly[ClassTag[H]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: C => Future[Either[C#CommandError, Unit]] = (t: C) => handler(t.asInstanceOf[H])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
