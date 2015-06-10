package edu.warbot.online.repository;

import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import edu.warbot.online.models.Account;
import org.hibernate.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
@Transactional(readOnly = true)
public class AccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Account save(Account account) {

        if (account.getId() == null) {
            entityManager.persist(account);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        } else
            entityManager.merge(account);
        LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), account);
        return account;
    }

    public Account findByEmail(String email) {
        try {
            return entityManager.createNamedQuery
                    (Account.FIND_BY_EMAIL, Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public Iterable<Account> findAll() {
        return entityManager.createQuery(
                "Select a from Account a ",
                Account.class).getResultList();
    }

    public Account findOne(Long id) {
        try {
            return entityManager.createQuery
                    ("Select a FROM Account a WHERE a.id LIKE :id", Account.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }
}
