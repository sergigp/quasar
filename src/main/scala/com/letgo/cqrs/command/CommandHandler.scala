package com.letgo.chat.lib.cqrs.command

import scala.reflect.ClassTag

import com.letgo.chat.lib.cqrs.validation.Validation.Validation

abstract class CommandHandler[P[_], C <: Command](implicit val commandClass: ClassTag[C]) {

  def unsafe(c: Command): Validation[P[Either[C#CommandError, Unit]]] = handle(c.asInstanceOf[C])

  def handle(command: C): Validation[P[Either[C#CommandError, Unit]]]
}
