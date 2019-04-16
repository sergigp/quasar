package com.sergigp.quasar.command

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

import org.slf4j.Logger

class AsyncCommandBus(logger: Logger)(implicit ec: ExecutionContext) extends CommandBus[Future] {

  private var handlers = Map.empty[Class[_], Command => Future[Any]]

  override def publish[C <: Command](command: C): Future[C#CommandReturnType] =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handler(command).map(_.asInstanceOf[C#CommandReturnType])
      case None          => Future.failed(CommandHandlerNotFound(command.getClass.getSimpleName))
    }

  override def subscribe[C <: Command: ClassTag](handler: C => Future[C#CommandReturnType]): Unit = {
    val classTag = implicitly[ClassTag[C]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: Command => Future[Any] = (t: Command) => handler(t.asInstanceOf[C])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
