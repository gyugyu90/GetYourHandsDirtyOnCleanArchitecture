package buckpal.application.port.in;

import buckpal.domain.Account;
import buckpal.domain.Money;

public interface GetAccountBalanceQuery {
	Money getAccountBalance(Account.AccountId accountId);
}
