package com.sergigp.quasar.command

import scala.reflect.ClassTag

trait CommandBus[P[_], C <: Command] {
  def publish(command: C): P[Either[C#CommandError, Unit]]

  def subscribe[HT <: C: ClassTag](handler: HT => P[Either[HT#CommandError, Unit]]): Unit
}
