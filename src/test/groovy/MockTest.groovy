import br.com.zup.groovy.model.Account
import br.com.zup.groovy.model.AccountCreatedEvent
import br.com.zup.groovy.model.AccountId
import br.com.zup.groovy.service.AccountPublisher
import br.com.zup.groovy.service.AccountService
import br.com.zup.groovy.service.FinalClassSample
import br.com.zup.groovy.service.impl.AccountServiceImpl
import de.jodamob.kotlin.testrunner.OpenedClasses
import de.jodamob.kotlin.testrunner.OpenedPackages
import de.jodamob.kotlin.testrunner.SpotlinTestRunner
import org.junit.runner.RunWith
import org.spockframework.mock.CannotCreateMockException
import spock.lang.Specification

@RunWith(SpotlinTestRunner.class)
@OpenedClasses(FinalClassSample)
class MockTest extends Specification {

    def "mocking 1 result for getBalance() passing any argument"() {
        given: "a mock accountService instance"
        def accountService = Mock(AccountService)
        accountService.getBalance(_) >> 45

        when: "get balance invoke"
        def balance = accountService.getBalance(new AccountId("1"))

        then: "balance equals to 45"
        balance == 45
    }

    def "mocking 3 results for getBalance() passing any argument"() {
        given: "a mock accountService instance"
        def accountService = Mock(AccountService)
        accountService.getBalance(_) >>> [45, 450, 600]

        when: "get balance invoke 3 times"
        def a = accountService.getBalance(new AccountId("1"))
        def b = accountService.getBalance(new AccountId("1"))
        def c = accountService.getBalance(new AccountId("1"))

        then:
        a == 45
        b == 450
        c == 600
    }

    def "checking the order"() {
        given: "a mock accountService instance"
        def accountService = Mock(AccountService)
        accountService.getBalance(_) >>> [45, 450]

        when: "get balance invoke in this order"
        accountService.getBalance(new AccountId("2"))
        accountService.getBalance(new AccountId("1"))

        then:
        1 * accountService.getBalance(new AccountId("2"))

        then:
        1 * accountService.getBalance(new AccountId("1"))
    }

    def "mocking 1 result for getBalance() passing any argument directly"() {
        given: "a mock accountService instance"
        def accountService = Mock(AccountService)

        when: "get balance invoke"
        def balance = accountService.getBalance(new AccountId("1"))

        then:
        1 * accountService.getBalance(new AccountId("1")) >> 45
        balance == 45
    }

    def "mocking a publisher called by the service"() {
        given: "a accountService instance"
        def account = new Account(new AccountId("1"), "Darth Vader")
        def event = new AccountCreatedEvent(account.id, account.name)
        def publisher = Mock(AccountPublisher)
        def accountService = new AccountServiceImpl(publisher)

        when: "save invoke one time"
        def accountId = accountService.save(account)

        then: "publish should be invoke 1 time too"
        1 * publisher.publish(event) >> account.id
        accountId == account.id
    }

    def "mocking a publisher called by the service using with"() {
        given: "a accountService instance"
        def account = new Account(new AccountId("1"), "Darth Vader")
        def event = new AccountCreatedEvent(account.id, account.name)
        def publisher = Mock(AccountPublisher)
        def accountService = new AccountServiceImpl(publisher)

        when: "save invoke one time"
        def accountId = accountService.save(account)

        then: "publish should be invoke 1 time too"
        with(publisher) {
            1 * publish(event) >> account.id
        }
        accountId == account.id
    }

    def "checking if a publisher was called 3 times"() {
        given: "a accountService instance"
        def accounts = [new Account(new AccountId("1"), "Darth Vader"),
                        new Account(new AccountId("2"), "Darth Malgus"),
                        new Account(new AccountId("3"), "Darth Maul")]
        def publisher = Mock(AccountPublisher)
        def accountService = new AccountServiceImpl(publisher)

        when: "save invoke"
        accountService.save(accounts)

        then: "publisher should be invoke 3 times"
        3 * publisher.publish(_)
    }

    def "checking if a publisher was not invoked"() {
        given: "a accountService instance"
        def publisher = Mock(AccountPublisher)
        def accountService = new AccountServiceImpl(publisher)

        when: "save invoke"
        accountService.save([])

        then: "should throw a exception and publisher show not be invoke"
        thrown(IllegalArgumentException)
        0 * publisher.publish(_)
    }

    def "mocking 3 results for publisher"() {
        given: "a accountService instance"
        def accounts = [new Account(new AccountId("1"), "Darth Vader"),
                        new Account(new AccountId("2"), "Darth Malgus"),
                        new Account(new AccountId("3"), "Darth Maul")]
        def publisher = Mock(AccountPublisher)
        publisher.publish(_) >>> accounts.collect { it.id }
        def accountService = new AccountServiceImpl(publisher)

        when: "save invoke"
        def accountIds = accountService.save(accounts)

        then:
        accountIds == accounts.collect { it.id }
    }

    def "should mock a final method from Kotlin"() {
        given: "a final method"
        def finalClassSample = Mock FinalClassSample

        when:
        def r = finalClassSample.finalMethod(10)

        then:
        1 * finalClassSample.finalMethod(10) >> 20
        r == 20
    }

    def "should fail without mock"() {
        expect:
        try {
            def finalClassSample = Mock FinalClassSample
            finalClassSample.finalMethod(10)
        } catch (Exception error) {
            assert error instanceof CannotCreateMockException
        }
    }

}
