package com.letgo.quasar.example.modules.product.application.add

import com.letgo.quasar.command.CommandHandler

class ProductAddCommandHandler[P[_]](userAdder: UserAdder) extends CommandHandler[P, ProductAddCommand] {
  override def handle(command: ProductAddCommand) = ???
}
