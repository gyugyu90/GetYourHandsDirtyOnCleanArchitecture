package buckpal.account.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import buckpal.account.application.port.in.SendMoneyCommand;
import buckpal.account.application.port.out.AccountLock;
import buckpal.account.application.port.out.LoadAccountPort;
import buckpal.account.application.port.out.UpdateAccountStatePort;
import buckpal.account.application.service.MoneyTransferProperties;
import buckpal.account.application.service.SendMoneyService;
import buckpal.account.domain.Account;
import buckpal.account.domain.Account.AccountId;
import buckpal.account.domain.Money;

public class SendMoneyServiceTest {
	private final LoadAccountPort loadAccountPort =
		Mockito.mock(LoadAccountPort.class);

	private final AccountLock accountLock =
		Mockito.mock(AccountLock.class);

	private final UpdateAccountStatePort updateAccountStatePort =
		Mockito.mock(UpdateAccountStatePort.class);

	private final SendMoneyService sendMoneyService =
		new SendMoneyService(loadAccountPort, accountLock, updateAccountStatePort, moneyTransferProperties());

	@Test
	void transactionSucceeds() {
		Account sourceAccount = givenSourceAccount();
		Account targetAccount = givenTargetAccount();

		givenWithdrawalWillSucceed(sourceAccount);
		givenDepositWillSucceed(targetAccount);

		Money money = Money.of(500L);

		SendMoneyCommand command = new SendMoneyCommand(
			sourceAccount.getId(),
			targetAccount.getId(),
			money
		);

		boolean success = sendMoneyService.sendMoney(command);

		assertThat(success).isTrue();

		AccountId sourceAccountId = sourceAccount.getId();
		AccountId targetAccountId = targetAccount.getId();

		then(accountLock).should().lockAccount(eq(sourceAccountId));
		then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
		then(accountLock).should().releaseAccount(eq(sourceAccountId));

		then(accountLock).should().lockAccount(eq(targetAccountId));
		then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
		then(accountLock).should().releaseAccount(eq(targetAccountId));

		thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
	}

	private void givenWithdrawalWillSucceed(Account account) {
		given(account.withdraw(any(Money.class), any(AccountId.class)))
			.willReturn(true);
	}

	private void givenDepositWillSucceed(Account account) {
		given(account.deposit(any(Money.class), any(AccountId.class)))
			.willReturn(true);
	}

	private Account givenTargetAccount(){
		return givenAnAccountWithId(new AccountId(42L));
	}

	private Account givenSourceAccount(){
		return givenAnAccountWithId(new AccountId(41L));
	}

	private Account givenAnAccountWithId(AccountId id) {
		Account account = Mockito.mock(Account.class);
		given(account.getId()).willReturn(id);
		given(loadAccountPort.loadAccount(eq(account.getId()), any(LocalDateTime.class))).willReturn(account);
		return account;
	}

	private void thenAccountsHaveBeenUpdated(AccountId... accountIds){
		ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
		then(updateAccountStatePort).should(times(accountIds.length))
			.updateActivities(accountCaptor.capture());

		List<AccountId> updatedAccountIds = accountCaptor.getAllValues()
			.stream()
			.map(Account::getId)
			.collect(Collectors.toList());

		for(AccountId accountId : accountIds){
			assertThat(updatedAccountIds).contains(accountId);
		}
	}

	private MoneyTransferProperties moneyTransferProperties(){
		return new MoneyTransferProperties(Money.of(Long.MAX_VALUE));
	}
}
