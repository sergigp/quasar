package com.sergigp.quasar.event

import scala.reflect.ClassTag

trait EventBus[P[_]] {
  def publish[E <: Event](event: E): P[Unit]

  def subscribe[C <: Event: ClassTag](handler: C => P[Unit]): Unit
}
