package br.com.zup.groovy.model

data class AccountId(val id: String)

data class Account(val id: AccountId, val name: String)

data class AccountCreatedEvent(val id: AccountId, val name: String)
