package edu.warbot.repository;

import edu.warbot.models.Map;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

/**
 * Created by quent on 23/04/2015.
 */@Repository
   public class MapRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Map findByName(String name)
    {
        try {
            return entityManager.createNamedQuery(Map.FIND_BY_NAME, Map.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public <S extends Map> S save(S s) {
        entityManager.persist(s);
        return s;
    }

    public Map findOne(Long aLong) {
        try {
            return (Map) entityManager.createQuery("Select m from Map m where m.id = :id", Map.class)
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
    public Iterable<Map> findAll() {
        return  entityManager.createQuery("Select m from Map m ", Map.class).getResultList();
    }

    //    @Override
    public long count() {
        return (Long) entityManager.createQuery("Select Count * FROM Map").getSingleResult();
    }

    //    @Override
    public void delete(Map map)
    {
        entityManager.createQuery("Delete FROM Map m WHERE m.id = :id")
                .setParameter("id", map.getId()).executeUpdate();
    }

}
