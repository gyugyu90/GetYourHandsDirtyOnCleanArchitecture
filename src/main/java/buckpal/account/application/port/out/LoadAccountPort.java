package buckpal.account.application.port.out;

import java.time.LocalDateTime;

import buckpal.account.domain.Account;

public interface LoadAccountPort {
	Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate);
}
