package itmo.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import itmo.server.dto.RouteDto;
import itmo.server.entities.Route;

import java.util.HashMap;
import java.util.List;

//@Remote
public interface RouteService {
    void addRoute(RouteDto routeDto);
    void deleteRoute(Integer id);
    Route getRoute(Integer id);
    List<Route> getAllRoutes(String sortingCriteria, HashMap<String, String> filterParameters,
                             Integer page, Integer page_size) throws JsonProcessingException;
    void updateRoute(Integer id, RouteDto routeDto);
    long countDistanceEquals(double distance);
    long countDistanceGreater(double distance);
    List<Route> findNameContainsSubstr(String substr);
}
