package itmo.server.dao;

import itmo.server.entities.Coordinate;
import itmo.server.entities.Location;
import itmo.server.entities.Route;
import itmo.server.utils.Filter;
import itmo.server.utils.SortOrder;
import itmo.server.utils.Table;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RouteDAO {
    Root<Route> root;
    Join<Route, Coordinate> coordinateJoin;
    Join<Route, Location> fromJoin;
    Join<Route, Location> toJoin;

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Transactional
    public Route findById(Integer id) {
        Route r = entityManager.find(Route.class, id);
        return r;
    }

    @Transactional
    public void save(Route route) {
        entityManager.persist(route);
    }

    @Transactional
    public void remove(Integer id) {
        entityManager.remove(entityManager.find(Route.class, id));
    }

    @SuppressWarnings("unchecked")
    public List<Route> getAll() {
        List<Route> routesList = entityManager.createQuery("SELECT r FROM Route r").getResultList();
        return routesList;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Route> sortAndFilter(List<SortOrder> orders, List<Filter> filters) {
        Expression e;
        Table table;
        String nameOfColumn;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Route> cq = cb.createQuery(Route.class);
        root = cq.from(Route.class);
        coordinateJoin = root.join("coordinates");
        fromJoin = root.join("from");
        toJoin = root.join("to");
        //filter
        ArrayList<Predicate> predicates = new ArrayList<>();
        for (Filter filter : filters) {
            table = filter.getDef().getTableClass();
            nameOfColumn = filter.getDef().getNameOfColumn();
            e = getExpression(table, nameOfColumn);
            switch (filter.getOperation()){
                case LIKE:
                    predicates.add(cb.like(e, "%" + filter.getVar1() + "%"));
                    break;
                case BETWEEN:
                    if (!filter.getDef().getNameOfColumn().equals("creationDate"))
                        predicates.add(cb.between(e, Double.parseDouble(filter.getVar1()), Double.parseDouble(filter.getVar2())));
                    else predicates.add(cb.between(e, toDate(filter.getVar1()), toDate(filter.getVar2())));
                    break;
                case EQ:
                    predicates.add(cb.equal(e, filter.getVar1()));
                    break;
            }
        }
        cq.select(root).where(predicates.toArray(new Predicate[0]));
        //sort
        for (SortOrder sortOrder : orders) {
            table = sortOrder.getDef().getTableClass();
            nameOfColumn = sortOrder.getDef().getNameOfColumn();
            e = getExpression(table, nameOfColumn);
            if (sortOrder.isAsc()) {
                cq.orderBy(cb.asc(e));
            } else {
                cq.orderBy(cb.desc(e));
            }
        }
        Query query = entityManager.createQuery(cq);
        return (List<Route>) query.getResultList();
    }

    //todo
    private LocalDate toDate(String s){
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Expression getExpression(Table table, String nameOfColumn) {
        switch (table){
            case ROUTE:
                return root.get(nameOfColumn);
            case COORDINATE:
                return coordinateJoin.get(nameOfColumn);
            case FROM:
                return fromJoin.get(nameOfColumn);
            case TO:
                return toJoin.get(nameOfColumn);
        }
        throw new NotFoundException("???");
    }

    @Transactional
    public long countDistanceEquals(double distance){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Route> root = cq.from(Route.class);
        cq.where(cb.equal(root.get("distance"), distance));
        cq.select(cb.count(root));
        Query query = entityManager.createQuery(cq);
        return (long) query.getSingleResult();
    }

    @Transactional
    public long countDistanceGreater(double distance) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Route> root = cq.from(Route.class);
        cq.where(cb.greaterThan(root.get("distance"), distance));
        cq.select(cb.count(root));
        Query query = entityManager.createQuery(cq);
        return (long) query.getSingleResult();
    }
}


