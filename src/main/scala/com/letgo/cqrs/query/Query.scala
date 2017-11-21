package com.letgo.chat.lib.cqrs.query

import com.letgo.chat.lib.cqrs.error.DomainError

abstract class Query {
  type QueryResponse
  type QueryError <: DomainError
}
