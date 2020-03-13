package com.sergigp.quasar.event

import scala.reflect.ClassTag

trait EventBus[P[_]] extends EventPublisher[P] {
  def subscribe[E <: Event: ClassTag](handler: E => P[Unit]): Unit
}
