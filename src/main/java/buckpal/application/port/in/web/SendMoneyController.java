package buckpal.application.port.in.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import buckpal.application.port.in.SendMoneyCommand;
import buckpal.application.port.in.SendMoneyUseCase;
import buckpal.common.WebAdapter;
import buckpal.domain.Account;
import buckpal.domain.Money;
import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
class SendMoneyController {

	private final SendMoneyUseCase sendMoneyUseCase;

	@PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
	void sendMoney(
		@PathVariable("sourceAccountId") Long sourceAccountId,
		@PathVariable("targetAccountId") Long targetAccountId,
		@PathVariable("amount") Long amount) {

		SendMoneyCommand command = new SendMoneyCommand(
			new Account.AccountId(sourceAccountId),
			new Account.AccountId(targetAccountId),
			Money.of(amount));

		sendMoneyUseCase.sendMoney(command);
	}

}
