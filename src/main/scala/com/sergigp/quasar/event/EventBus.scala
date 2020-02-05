package com.sergigp.quasar.event

import scala.reflect.ClassTag

trait EventBus[P[_]] extends EventPublisher[P] {
  def subscribe[C <: Event: ClassTag](handler: C => P[Unit]): Unit
}
