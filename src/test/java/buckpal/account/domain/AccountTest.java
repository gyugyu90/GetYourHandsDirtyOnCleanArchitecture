package buckpal.account.domain;

import static buckpal.account.domain.AccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountTest {

	@Test
	void withdrawalSucceeds() {
		Account.AccountId accountId = new Account.AccountId(1L);
		Account account = defaultAccount()
			.withAccountId(accountId)
			.withBaselineBalance(Money.of(555L))
			.withActivityWindow(new ActivityWindow(
				ActivityTestData.defaultActivity()
					.withTargetAccount(accountId)
					.withMoney(Money.of(999))
					.build(),
				ActivityTestData.defaultActivity()
					.withTargetAccount(accountId)
					.withMoney(Money.of(1))
					.build()
			))
			.build();

		boolean success = account.withdraw(Money.of(555L), new Account.AccountId(99L));

		assertTrue(success);
		assertThat(account.getActivityWindow().getActivities()).hasSize(3);
		assertEquals(account.calculateBalance(), Money.of(1000));
	}
}