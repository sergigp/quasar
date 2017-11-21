package com.letgo.chat.lib.cqrs.validation

import cats.data.ValidatedNel

object Validation {
  type Validation[T] = ValidatedNel[ValidationError, T]
}
