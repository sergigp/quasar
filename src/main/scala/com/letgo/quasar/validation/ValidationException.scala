package com.letgo.quasar.validation

import cats.data.NonEmptyList

case class ValidationException(errors: NonEmptyList[ValidationError]) extends Throwable
