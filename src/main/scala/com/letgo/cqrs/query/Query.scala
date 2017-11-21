package com.letgo.cqrs.query

import com.letgo.cqrs.error.DomainError

abstract class Query {
  type QueryResponse
  type QueryError <: DomainError
}
