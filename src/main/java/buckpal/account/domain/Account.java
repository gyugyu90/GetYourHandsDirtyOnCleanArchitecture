package buckpal.account.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

	private AccountId id;
	private Money baselineBalance;
	private ActivityWindow activityWindow;

	public static Account withoutId(Money baselineBalance, ActivityWindow activityWindow) {
		return new Account(null, baselineBalance, activityWindow);
	}

	public static Account withId(AccountId accountId, Money baselineBalance, ActivityWindow activityWindow) {
		return new Account(accountId, baselineBalance, activityWindow);
	}

	public Money calculateBalance() {
		return Money.add(
			this.baselineBalance,
			this.activityWindow.calculateBalance(this.id)
		);
	}

	public boolean withdraw(Money money, AccountId targetAccountId) {
		if (!mayWithdraw(money)) {
			return false;
		}

		var withdrawal = new Activity(
			this.id,
			this.id,
			targetAccountId,
			LocalDateTime.now(),
			money
		);

		this.activityWindow.addActivity(withdrawal);
		return true;
	}

	public boolean deposit(Money money, AccountId sourceAccountId) {
		Activity deposit = new Activity(
			this.id,
			sourceAccountId,
			this.id,
			LocalDateTime.now(),
			money
		);

		this.activityWindow.addActivity(deposit);
		return true;
	}

	private boolean mayWithdraw(Money money) {
		return Money.add(
			this.calculateBalance(),
			money.negate()
		).isPositive();
	}

	@Value
	public static class AccountId {
		Long value;
	}
}
