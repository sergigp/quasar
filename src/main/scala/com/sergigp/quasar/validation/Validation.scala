package com.sergigp.quasar.validation

import cats.data.ValidatedNel

object Validation {
  type Validation[T] = ValidatedNel[ValidationError, T]
}
