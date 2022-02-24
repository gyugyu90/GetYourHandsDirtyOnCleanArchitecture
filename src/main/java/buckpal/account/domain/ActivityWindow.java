package buckpal.account.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class ActivityWindow {

	private List<Activity> activities;

	public ActivityWindow(List<Activity> activities) {
		this.activities = activities;
	}

	public ActivityWindow(Activity... activities) {
		this.activities = new ArrayList<>(Arrays.asList(activities));
	}

	public Money calculateBalance(Account.AccountId accountId) {
		Money depositBalance = activities.stream()
			.filter(a -> a.getTargetAccountId().equals(accountId))
			.map(Activity::getMoney)
			.reduce(Money.ZERO, Money::add);

		Money withdrawalBalance = activities.stream()
			.filter(a -> a.getSourceAccountId().equals(accountId))
			.map(Activity::getMoney)
			.reduce(Money.ZERO, Money::add);

		return Money.add(depositBalance, withdrawalBalance.negate());
	}

	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}

}
