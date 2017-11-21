package com.letgo.chat.lib.cqrs.command

trait CommandBus[P[_]] {
  def publish[C <: Command](command: C): P[Either[C#CommandError, Unit]]

  def subscribe[C <: Command](handler: CommandHandler[P, C])
}
