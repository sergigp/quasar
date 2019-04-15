package com.sergigp.quasar.command

import scala.reflect.ClassTag

trait CommandBus[P[_], C] {
  def publish(command: C): P[Unit]

  def subscribe[HT <: C: ClassTag](handler: HT => P[Unit]): Unit
}
