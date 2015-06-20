package edu.warbot.online.repository;

import edu.warbot.online.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a where a.email = ?1")
    Account findByEmail(String email);

}
