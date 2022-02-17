package buckpal.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Activity {
	
	@Getter
	ActivityId id;

	@Getter
	@NonNull
	Account.AccountId ownerAccountId;

	@Getter
	@NonNull
	Account.AccountId sourceAccountId;

	@Getter
	@NonNull
	Account.AccountId targetAccountId;

	@Getter
	@NonNull
	LocalDateTime timestamp;

	@Getter
	@NonNull
	Money money;

	public Activity(
		@NonNull Account.AccountId ownerAccountId,
		@NonNull Account.AccountId sourceAccountId,
		@NonNull Account.AccountId targetAccountId,
		@NonNull LocalDateTime timestamp,
		@NonNull Money money) {
		this.id = null;
		this.ownerAccountId = ownerAccountId;
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.timestamp = timestamp;
		this.money = money;
	}

	@Value
	public static class ActivityId {
		Long value;
	}
	
}
