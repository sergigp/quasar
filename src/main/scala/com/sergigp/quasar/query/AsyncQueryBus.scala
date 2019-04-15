package com.sergigp.quasar.query

import scala.concurrent.Future
import scala.reflect.ClassTag

import org.slf4j.Logger

final class AsyncQueryBus[Q <: Query](logger: Logger) extends QueryBus[Future, Q] {

  private var handlers: Map[Class[_], Q => Future[Q#QueryResponse]] = Map.empty

  override def ask(query: Q): Future[Q#QueryResponse] =
    handlers
      .get(query.getClass) match {
      case Some(handler) => handler(query)
      case None          => Future.failed(QueryHandlerNotFound(query.getClass.getSimpleName))
    }

  override def subscribe[HT <: Q: ClassTag](handler: HT => Future[HT#QueryResponse]): Unit = {
    val classTag = implicitly[ClassTag[HT]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: Q => Future[Q#QueryResponse] = (t: Q) => handler(t.asInstanceOf[HT])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  private case class QueryHandlerNotFound(queryName: String) extends Exception(s"handler for $queryName not found")
}
