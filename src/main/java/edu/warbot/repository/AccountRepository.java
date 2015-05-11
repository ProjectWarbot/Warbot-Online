package edu.warbot.repository;

import javax.persistence.*;
import javax.inject.Inject;

import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import edu.warbot.models.Account;
import org.hibernate.Session;
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

        if(account.getId()==null) {
            entityManager.persist(account);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        else
            entityManager.merge(account);
        LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),account);
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

    public Iterable<Account> findAll() {
        return  entityManager.createQuery(
                "Select a from Account a ",
                Account.class).getResultList();
    }
}
