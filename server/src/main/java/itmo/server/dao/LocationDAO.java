package itmo.server.dao;

import itmo.server.entities.Location;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Stateless
public class LocationDAO {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Transactional
    public Location findById(Integer id){
        return entityManager.find(Location.class, id);
    }

    @Transactional
    public void insert(Location location) {
        entityManager.persist(location);
    }


    public Location getByXYZ(float x, Double y, Float z){
        Query q = entityManager.createQuery("SELECT l FROM Location l WHERE l.x =:x AND l.y =:y AND l.z =:z");
        q.setParameter("x", x);
        q.setParameter("y", y);
        q.setParameter("z", z);
        try {
            Location location = (Location) q.getSingleResult();
            return location;
        }catch (NoResultException e){

        }
        return null;
    }
}

