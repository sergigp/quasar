package com.letgo.cqrs.test.stub

import java.util.UUID

object UuidStringStub {
  def random: String = UUID.randomUUID().toString
}
