package buckpal.account.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import buckpal.account.application.port.out.LoadAccountPort;
import buckpal.account.application.port.out.UpdateAccountStatePort;
import buckpal.account.domain.Account;
import buckpal.account.domain.Activity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
class AccountPersistenceAdapter implements
	LoadAccountPort,
	UpdateAccountStatePort {

	private final SpringDataAccountRepository accountRepository;
	private final ActivityRepository activityRepository;
	private final AccountMapper accountMapper;

	@Override
	public Account loadAccount(
		Account.AccountId accountId,
		LocalDateTime baselineDate) {

		AccountJpaEntity account =
			accountRepository.findById(accountId.getValue())
				.orElseThrow(EntityNotFoundException::new);

		List<ActivityJpaEntity> activities =
			activityRepository.findByOwnerSince(
				accountId.getValue(),
				baselineDate);

		Long withdrawalBalance = orZero(activityRepository
			.getWithdrawalBalanceUntil(
				accountId.getValue(),
				baselineDate));

		Long depositBalance = orZero(activityRepository
			.getDepositBalanceUntil(
				accountId.getValue(),
				baselineDate));

		return accountMapper.mapToDomainEntity(
			account,
			activities,
			withdrawalBalance,
			depositBalance);

	}

	private Long orZero(Long value){
		return value == null ? 0L : value;
	}


	@Override
	public void updateActivities(Account account) {
		for (Activity activity : account.getActivityWindow().getActivities()) {
			if (activity.getId() == null) {
				activityRepository.save(accountMapper.mapToJpaEntity(activity));
			}
		}
	}

}
