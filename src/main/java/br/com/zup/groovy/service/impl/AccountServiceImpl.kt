package br.com.zup.groovy.service.impl

import br.com.zup.groovy.model.Account
import br.com.zup.groovy.model.AccountCreatedEvent
import br.com.zup.groovy.model.AccountId
import br.com.zup.groovy.service.AccountPublisher
import br.com.zup.groovy.service.AccountService

class AccountServiceImpl (private val publisher: AccountPublisher) : AccountService {

    override fun getBalance(id: AccountId): Int {
        return -1
    }

    override fun save(account: Account): AccountId =
            publisher.publish(AccountCreatedEvent(account.id, account.name))

    override fun save(accounts: List<Account>): List<AccountId> {
        if (accounts.isEmpty()) throw IllegalArgumentException("empty account list")
        return accounts.map { save(it) }
    }

}
