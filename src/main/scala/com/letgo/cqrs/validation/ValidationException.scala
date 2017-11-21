package com.letgo.cqrs.validation

import cats.data.NonEmptyList

case class ValidationException(errors: NonEmptyList[ValidationError]) extends Throwable
