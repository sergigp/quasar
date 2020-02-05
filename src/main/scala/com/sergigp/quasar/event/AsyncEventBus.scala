package com.sergigp.quasar.event

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

import org.slf4j.Logger

final class AsyncEventBus(
  logger: Logger,
  onSuccess: String => Unit = _ => (),
  onFailure: Throwable => Unit = _ => (),
  onHandlerNotFound: String => Unit = _ => (),
)(implicit ec: ExecutionContext)
    extends EventBus[Future] {

  private var handlers = Map.empty[Class[_], Event => Future[Unit]]

  override def publish[E <: Event](event: E): Future[Unit] =
    handlers
      .get(event.getClass) match {
      case Some(handler) => handleEvent(event, handler)
      case None          =>
        onHandlerNotFound(event.getClass.getSimpleName)
        Future.unit
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

  private def handleEvent[E <: Event](event: E, handler: E => Future[Unit]): Future[Unit] = {
    val asyncResult = handler(event).map { result =>
      onSuccess(event.getClass.getSimpleName)
      result
    }
    asyncResult.failed.foreach(onFailure)
    asyncResult
  }
}
