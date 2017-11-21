package com.letgo.cqrs.command

import scala.concurrent.Future

import cats.data.Validated.Valid
import cats.implicits._
import ch.qos.logback.classic.Logger

import com.letgo.cqrs.validation.ValidationException
import com.letgo.cqrs.validation.Validation.Validation

class AsyncCommandBus(logger: Logger) extends CommandBus[Future] {

  private var handlers: Map[Class[_], CommandHandler[Future, _ <: Command]] = Map.empty

  override def publish[C <: Command](
    command: C
  ): Future[Either[C#CommandError, Unit]] =
    handlers
      .get(command.getClass)
      .map(_.unsafe(command).asInstanceOf[Validation[Future[Either[C#CommandError, Unit]]]])
      .getOrElse(
        Valid(
          Future.failed[Either[C#CommandError, Unit]](
            CommandHandlerNotFound(command.getClass.getSimpleName)
          )
        )
      ).valueOr(validationErrors => Future.failed(ValidationException(validationErrors)))

  override def subscribe[C <: Command](handler: CommandHandler[Future, C]): Unit =
    synchronized {
      if (handlers.contains(handler.commandClass.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        handlers = handlers + (handler.commandClass.runtimeClass -> handler)
      }
    }

  private case class CommandHandlerNotFound(commandName: String) extends Throwable
}
