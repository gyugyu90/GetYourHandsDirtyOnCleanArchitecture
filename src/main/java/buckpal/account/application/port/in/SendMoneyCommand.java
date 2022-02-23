package buckpal.account.application.port.in;

import static java.util.Objects.*;

import javax.validation.constraints.NotNull;

import buckpal.common.SelfValidating;
import buckpal.account.domain.Account;
import buckpal.account.domain.Money;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

	@NotNull Account.AccountId sourceAccountId;
	@NotNull Account.AccountId targetAccountId;
	@NotNull Money money;

	public SendMoneyCommand(
		Account.AccountId sourceAccountId,
		Account.AccountId targetAccountId,
		Money money) {
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.money = money;

		requireNonNull(sourceAccountId);
		requireNonNull(targetAccountId);
		requireNonNull(money);
		this.validateSelf();
	}

}
