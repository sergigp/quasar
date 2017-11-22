package com.letgo.quasar.query

trait QueryBus[P[_]] {
  def ask[Q <: Query](query: Q): P[Either[Q#QueryError, Q#QueryResponse]]

  def subscribe[Q <: Query](handler: QueryHandler[P, Q])
}
