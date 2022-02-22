package buckpal.application.service;

import java.time.LocalDateTime;

import buckpal.application.port.in.GetAccountBalanceQuery;
import buckpal.application.port.out.LoadAccountPort;
import buckpal.domain.Account;
import buckpal.domain.Money;
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