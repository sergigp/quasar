package com.sergigp.quasar.event

import scala.reflect.ClassTag

trait EventBus[P[_]] {
  def publish[E <: Event](command: E): P[E#EventReturnType]

  def subscribe[C <: Event: ClassTag](handler: C => P[C#EventReturnType]): Unit
}
