package buckpal.account.application.port.in;

import buckpal.account.domain.Account;
import buckpal.account.domain.Money;

public interface GetAccountBalanceQuery {
	Money getAccountBalance(Account.AccountId accountId);
}
