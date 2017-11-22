package com.letgo.quasar.query

import com.letgo.quasar.error.DomainError

abstract class Query {
  type QueryResponse
  type QueryError <: DomainError
}
