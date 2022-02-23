package buckpal.application.port.out;

import java.time.LocalDateTime;

import buckpal.domain.Account;

public interface LoadAccountPort {
	Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate);
}
