package com.sergigp.quasar.query

import scala.concurrent.Future

import cats.data.Validated.Valid
import com.sergigp.quasar.validation.ValidationException
import com.sergigp.quasar.validation.Validation.Validation
import org.slf4j.Logger

final class AsyncQueryBus(logger: Logger) extends QueryBus[Future] {

  private var handlers: Map[Class[_], QueryHandler[Future, _ <: Query]] =
    Map.empty

  override def ask[QueryType <: Query](
      query: QueryType
  ): Future[Either[QueryType#QueryError, QueryType#QueryResponse]] =
    handlers
      .get(query.getClass)
      .map(_.unsafe(query).asInstanceOf[Validation[
        Future[Either[QueryType#QueryError, QueryType#QueryResponse]]]])
      .getOrElse(
        Valid(
          Future.failed[Either[QueryType#QueryError, QueryType#QueryResponse]](
            QueryHandlerNotFound(query.getClass.getSimpleName)
          )
        )
      ).valueOr(validationErrors => Future.failed(ValidationException(validationErrors)))

  override def subscribe[Q <: Query](handler: QueryHandler[Future, Q]): Unit = {
    synchronized {
      if (handlers.contains(handler.queryClass.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        handlers = handlers + (handler.queryClass.runtimeClass -> handler)
      }
    }
  }

  private case class QueryHandlerNotFound(queryName: String) extends Exception(s"handler for $queryName not found")
}
