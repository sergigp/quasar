package com.sergigp.quasar.command

import scala.reflect.ClassTag

trait CommandBus[P[_]] {
  def publish[C <: Command](command: C): P[C#CommandReturnType]

  def subscribe[C <: Command: ClassTag](handler: C => P[C#CommandReturnType]): Unit
}
