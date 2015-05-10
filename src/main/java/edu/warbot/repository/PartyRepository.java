package edu.warbot.repository;

import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import edu.emory.mathcs.backport.java.util.Collections;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sebastien Beugnon
 */
@Repository
@Transactional
public class PartyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Party findByName(String name)
    {
        try {
            Party p = entityManager.createNamedQuery(Party.FIND_BY_NAME, Party.class)
                    .setParameter("name", name)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),p);
            return p;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public <S extends Party> S save(S s) {
        if(s.getId()==null)
            entityManager.persist(s);
        else
            entityManager.merge(s);
        return s;
    }

    public Party findOne(Long aLong) {
        try {
            Party p = entityManager.createQuery(
                    "Select p from Party p where p.id = :id",
                    Party.class)
                    .setParameter("id",aLong)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),p);
            return p;
        } catch (PersistenceException e) {
            return null;
        }
    }

    //    @Override
    public boolean exists(Long aLong) {
        return findOne(aLong)!=null;
    }

    //    @Override
    public Iterable<Party> findAll() {
        List<Party> list =  entityManager.createQuery(
                "Select p from Party p ",
                Party.class).getResultList();
        LazyLoadingUtil.deepHydrate(entityManager.unwrap(org.hibernate.Session.class),list);
        return list;
    }

    //    @Override
    public Iterable<Party> findAll(Iterable<Long> iterable) {
        List<Party> parties = new ArrayList<>();

        for(Long l : iterable)
        {
            Party p = findOne(l);
            if(p!=null)
                parties.add(p);
        }
        LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),parties);
        return parties;
    }

    //    @Override
    public long count() {
        return (Long) entityManager.
                createQuery("Select count * FROM Party").getSingleResult();
    }

    //    @Override
    public void delete(Long aLong)
    {
        entityManager.createQuery("Delete FROM Party p WHERE p.id = :id")
                .setParameter("id",aLong).executeUpdate();
    }

    //    @Override
    public void delete(Party party)
    {
        entityManager.createQuery("Delete FROM Party p WHERE p.id = :id")
                .setParameter("id",party.getId()).executeUpdate();
    }

    //    @Override
    public void delete(Iterable<? extends Party> iterable)
    {
        //NEVER DEFINED
    }

    //    @Override
    public void deleteAll() {
        //NEVER DEFINED
    }

    public List<Party> findByCreator(Account account) {
        try {
            List<Party> list =  entityManager.createQuery(
                    "Select p from Party p where p.creator = :creator",
                    Party.class)
                    .setParameter("creator",account)
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),list);
            return list;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }
}
