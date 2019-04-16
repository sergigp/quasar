package com.sergigp.quasar.query

import scala.reflect.ClassTag

trait QueryBus[P[_]] {
  def ask[Q <: Query](query: Q): P[Q#QueryResponse]

  def subscribe[Q <: Query: ClassTag](handler: Q => P[Q#QueryResponse]): Unit
}
