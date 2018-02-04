package com.sergigp.quasar.query

import scala.language.higherKinds
import scala.reflect.ClassTag

import com.sergigp.quasar.validation.Validation.Validation

abstract class QueryHandler[P[_], Q <: Query](implicit val queryClass: ClassTag[Q]) {
  def unsafe(query: Query): Validation[P[Either[Q#QueryError, Q#QueryResponse]]] = handle(query.asInstanceOf[Q])

  def handle(query: Q): Validation[P[Either[Q#QueryError, Q#QueryResponse]]]
}
