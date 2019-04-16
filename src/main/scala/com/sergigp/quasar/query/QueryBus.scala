package com.sergigp.quasar.query

import scala.reflect.ClassTag

trait QueryBus[P[_], Q <: Query] {
  def ask(query: Q): P[Either[Q#QueryError, Q#QueryResponse]]

  def subscribe[HT <: Q: ClassTag](handler: HT => P[Either[HT#QueryError, HT#QueryResponse]]): Unit
}
