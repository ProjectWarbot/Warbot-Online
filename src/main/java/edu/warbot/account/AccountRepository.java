package edu.warbot.account;

import javax.persistence.*;
import javax.inject.Inject;

import edu.warbot.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Repository
@Transactional(readOnly = true)
public class AccountRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Account save(Account account)
    {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        entityManager.persist(account);
        return account;
    }

    public Account findByEmail(String email) {
        try
        {
            return entityManager.createNamedQuery
                    (Account.FIND_BY_EMAIL, Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }
}
