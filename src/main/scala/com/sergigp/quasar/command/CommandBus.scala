package com.sergigp.quasar.command

import scala.concurrent.Future
import scala.reflect.ClassTag

trait CommandBus[P[_]] {
  def publish[C <: Command](command: C): Future[C#CommandReturnType]

  def subscribe[C <: Command: ClassTag](handler: C => Future[C#CommandReturnType]): Unit
}
