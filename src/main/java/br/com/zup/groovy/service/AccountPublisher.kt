package br.com.zup.groovy.service

import br.com.zup.groovy.model.AccountCreatedEvent
import br.com.zup.groovy.model.AccountId

interface AccountPublisher {
    fun publish(event: AccountCreatedEvent): AccountId
}