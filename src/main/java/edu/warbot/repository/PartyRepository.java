package edu.warbot.repository;

import edu.warbot.models.Party;
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
@Transactional(readOnly = true)
public class PartyRepository {
    @PersistenceContext
    private EntityManager entityManager;



    public Party findByName(String name)
    {
        try {
            return entityManager.createNamedQuery(Party.FIND_BY_NAME, Party.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Transactional
    public <S extends Party> S save(S s) {
        entityManager.persist(s);
        return s;
    }

    @Transactional
    public <S extends Party> Iterable<S> save(Iterable<S> party) {
        entityManager.persist(party);
        return party;
    }

    public Party findOne(Long aLong) {
        try {
        return (Party) entityManager.createQuery(
                "Select p from Party p where p.id = :id",
                Party.class)
                .setParameter("id",aLong)
                .getSingleResult();
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
        return  entityManager.createQuery(
                "Select p from Party p ",
                Party.class).getResultList();
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
        return parties;
    }

//    @Override
    public long count() {
        return (Long) entityManager.
                createQuery("Select count * FROM Party").getSingleResult();
    }

//    @Override
    @Transactional
    public void delete(Long aLong)
    {
        entityManager.createQuery("Delete FROM Party p WHERE p.id = :id")
                .setParameter("id",aLong).executeUpdate();
    }

//    @Override
    @Transactional
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
}
