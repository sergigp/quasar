package com.letgo.chat.lib.cqrs.validation

import cats.data.NonEmptyList

case class ValidationException(errors: NonEmptyList[ValidationError]) extends Throwable
