package buckpal.account.application.service;

import java.time.LocalDateTime;

import buckpal.account.application.port.in.GetAccountBalanceQuery;
import buckpal.account.application.port.out.LoadAccountPort;
import buckpal.account.domain.Account;
import buckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class GetAccountBalanceService implements GetAccountBalanceQuery {

	private final LoadAccountPort loadAccountPort;

	@Override
	public Money getAccountBalance(Account.AccountId accountId) {
		return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
			.calculateBalance();
	}
}