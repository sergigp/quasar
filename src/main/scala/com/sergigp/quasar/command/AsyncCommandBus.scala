package com.sergigp.quasar.command

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

import org.slf4j.Logger

class AsyncCommandBus(
  logger: Logger,
  onSuccess: String => Unit = _ => (),
  onFailure: Throwable => Unit = _ => ()
)(implicit ec: ExecutionContext)
    extends CommandBus[Future] {

  private var handlers = Map.empty[Class[_], Command => Future[Any]]

  override def publish[C <: Command](command: C): Future[C#CommandReturnType] =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handleCommand(command, handler)
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

  private def handleCommand[C <: Command](command: C, handler: C => Future[Any]): Future[C#CommandReturnType] = {
    val asyncResult = handler(command).map(_.asInstanceOf[C#CommandReturnType]).map { result =>
      onSuccess(command.getClass.getSimpleName)
      result
    }
    asyncResult.failed.foreach(onFailure)
    asyncResult
  }

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
