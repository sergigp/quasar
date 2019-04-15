package com.sergigp.quasar.query

import scala.reflect.ClassTag

trait QueryBus[P[_], Q <: Query] {
  def ask(query: Q): P[Q#QueryResponse]

  def subscribe[HT <: Q: ClassTag](handler: HT => P[HT#QueryResponse]): Unit
}
