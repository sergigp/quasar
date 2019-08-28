package com.sergigp.quasar.event

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

import org.slf4j.Logger

class AsyncEventBus(logger: Logger)(implicit ec: ExecutionContext) extends EventBus[Future] {

  private var handlers = Map.empty[Class[_], Event => Future[Unit]]

  override def publish[E <: Event](event: E): Future[Unit] =
    handlers
      .get(event.getClass) match {
      case Some(handler) => handler(event)
      case None          => Future.failed(EventHandlerNotFound(event.getClass.getSimpleName))
    }

  override def subscribe[E <: Event: ClassTag](handler: E => Future[Unit]): Unit = {
    val classTag = implicitly[ClassTag[E]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: Event => Future[Unit] = (t: Event) => handler(t.asInstanceOf[E])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  case class EventHandlerNotFound(eventName: String) extends Exception(s"handler for $eventName not found")
}
