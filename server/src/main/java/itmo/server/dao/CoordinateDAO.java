package itmo.server.dao;

import itmo.server.entities.Coordinate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@AllArgsConstructor
@NoArgsConstructor
@Stateless
public class CoordinateDAO {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Transactional
    public Coordinate findById(Integer id){
        return entityManager.find(Coordinate.class, id);
    }

    @Transactional
    public void insert(Coordinate coordinate) {
        entityManager.persist(coordinate);
    }

    public Coordinate getByXY(double x, Float y){
        Query q = entityManager.createQuery("SELECT c FROM Coordinate c WHERE c.x =:x AND c.y =:y");
        q.setParameter("x", x);
        q.setParameter("y", y);
        try{
            Coordinate coordinate = (Coordinate) q.getSingleResult();
            return coordinate;
        }catch (NoResultException nre){

        }
        return null;
    }
}
