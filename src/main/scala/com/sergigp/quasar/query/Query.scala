package com.sergigp.quasar.query

import com.sergigp.quasar.error.DomainError

abstract class Query {
  type QueryResponse
  type QueryError <: DomainError
}
