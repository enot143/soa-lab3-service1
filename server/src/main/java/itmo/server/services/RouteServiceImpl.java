package itmo.server.services;

import itmo.server.dao.CoordinateDAO;
import itmo.server.dao.LocationDAO;
import itmo.server.dao.RouteDAO;
import itmo.server.dto.CoordinateDto;
import itmo.server.dto.LocationDto;
import itmo.server.dto.RouteDto;
import itmo.server.entities.Coordinate;
import itmo.server.entities.Location;
import itmo.server.entities.Route;
import itmo.server.utils.FilterSortUtil;
import org.jboss.ejb3.annotation.Pool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionBean;
import javax.ejb.Stateless;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


@Stateless
@Remote(value = RouteService.class)
@Pool(value="mypool")
public class RouteServiceImpl implements RouteService {
    private static Logger log = Logger.getLogger(SessionBean.class.getName());

    @EJB
    RouteDAO routeDAO;
    @EJB
    CoordinateDAO coordinateDAO;
    @EJB
    LocationDAO locationDAO;


    private Coordinate findCoordinates(CoordinateDto coordinateDto) {
        Coordinate c = coordinateDAO.getByXY(coordinateDto.getX(), coordinateDto.getY());
        if (c == null) {
            Coordinate newCoordinate = new Coordinate();
            newCoordinate.setX(coordinateDto.getX());
            newCoordinate.setY(coordinateDto.getY());
            coordinateDAO.insert(newCoordinate);
            return newCoordinate;
        } else {
            return c;
        }
    }

    private Location findLocation(LocationDto locationDto) {
        if (locationDto.getId() != null) {
            Location l = locationDAO.findById(locationDto.getId());
            if (l == null) throw new NotFoundException("location with id = " + locationDto.getId() + " is not found");
            else return l;
        }
        Location l = locationDAO.getByXYZ(locationDto.getX(), locationDto.getY(), locationDto.getZ());
        if (l == null) {
            Location newLocation = new Location();
            newLocation.setX(locationDto.getX());
            newLocation.setY(locationDto.getY());
            newLocation.setZ(locationDto.getZ());
            locationDAO.insert(newLocation);
            return newLocation;
        } else {
            return l;
        }
    }


    private void addOrUpdateRoute(Route route, RouteDto routeDto) {
        route.setName(routeDto.getName());
        route.setCoordinates(findCoordinates(routeDto.getCoordinates()));
        Date d = new Date();
        route.setCreationDate(d);
        route.setFrom(findLocation(routeDto.getFrom()));
        route.setTo(findLocation(routeDto.getTo()));
        route.setDistance(routeDto.getDistance());
        routeDAO.save(route);
    }


    @Override
    public void addRoute(RouteDto routeDto) {
        Route route = new Route();
        addOrUpdateRoute(route, routeDto);
    }

    @Override
    public void deleteRoute(Integer id) {
        Route r = routeDAO.findById(id);
        if (r!= null) {
            routeDAO.remove(id);
            return;
        }
        throw new NotFoundException("route is not found");
    }

    @Override
    public Route getRoute(Integer id) {
        Route r = routeDAO.findById(id);
        if (r == null) {
            throw new NotFoundException("route is not found");
        } else {
            return r;
        }
    }


    @Override
    public List<Route> getAllRoutes(String sortingCriteria,
                                 HashMap<String, String> filterParameters,
                                 Integer page,
                                 Integer page_size) {
        List<Route> routes = routeDAO.sortAndFilter(FilterSortUtil.getOrdersList(sortingCriteria),
                FilterSortUtil.getFiltersList(filterParameters));
        return executePageRequest(page, page_size, routes);
    }


    private List<Route> executePageRequest(Integer page, Integer page_size, List<Route> routes) {
        List<Route> routeList = new ArrayList<>();
        int left = (page - 1) * page_size;
        if (left >= routes.size()) {
            return routeList;
        }
        int right = page * page_size;
        if (right > routes.size()) {
            right = routes.size();
        }
        for (int i = left; i < right; i++) {
            routeList.add(routes.get(i));
        }
        return routeList;
    }

    public void updateRoute(Integer id, RouteDto routeDto) {
        Route route = routeDAO.findById(id);
        if (route != null) {
            addOrUpdateRoute(route, routeDto);
        } else {
            throw new NotFoundException("route is not found");
        }
    }

    public long countDistanceEquals(double distance) {
        long c = routeDAO.countDistanceEquals(distance);
        return c;
    }

    public long countDistanceGreater(double distance) {
        long c = routeDAO.countDistanceGreater(distance);
        return c;
    }

    public List<Route> findNameContainsSubstr(String substr) {
        HashMap<String, String> h = new HashMap<>();
        h.put("name", "like:" + substr);
        List<Route> routes = routeDAO.sortAndFilter(FilterSortUtil.getOrdersList("+id"),
                FilterSortUtil.getFiltersList(h));
        return routes;
    }

    @PreDestroy
    public void destroyInit() {
        log.info(String.format("%s\n", "================method @PreDestroy call================"));
    }

    @PostConstruct
    public void postConstructInit() {
        log.info(String.format("%s\n", "================method @PostConstruct call================"));
    }
}
