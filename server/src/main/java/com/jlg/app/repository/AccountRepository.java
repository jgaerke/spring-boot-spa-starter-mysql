package com.jlg.app.repository;

import com.jlg.app.model.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccountRepository extends PagingAndSortingRepository<Account, String> {

  Optional<Account> findOneByEmail(String email);

  Optional<Account> findOneByPasswordResetToken(String passwordResetToken);
}