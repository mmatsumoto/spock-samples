package br.com.zup.groovy.service

import br.com.zup.groovy.model.Account
import br.com.zup.groovy.model.AccountId

interface AccountService {
    fun save(account: Account): AccountId
    fun save(accounts: List<Account>): List<AccountId>
    fun getBalance(id: AccountId): Int
}
