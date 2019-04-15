package com.sergigp.quasar.command

import scala.concurrent.Future
import scala.reflect.ClassTag

import org.slf4j.Logger

class AsyncCommandBus[CT <: Command](logger: Logger) extends CommandBus[Future, CT] {

  private var handlers = Map.empty[Class[_], CT => Future[Unit]]

  override def publish(command: CT): Future[Unit] =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handler(command)
      case None          => Future.failed(CommandHandlerNotFound(command.getClass.getSimpleName))
    }

  override def subscribe[HT <: CT: ClassTag](handler: HT => Future[Unit]): Unit = {
    val classTag = implicitly[ClassTag[HT]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: CT => Future[Unit] = (t: CT) => handler(t.asInstanceOf[HT])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
