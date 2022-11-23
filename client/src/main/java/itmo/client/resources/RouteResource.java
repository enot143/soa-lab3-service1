package itmo.client.resources;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.server.dto.RouteDto;
import itmo.server.entities.Route;
import itmo.server.services.RouteService;
import org.wildfly.naming.client.WildFlyInitialContextFactory;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Path("/routes")
public class RouteResource {
    ObjectMapper mapper = new ObjectMapper();

    @EJB
    RouteService routeService;// = getFromEJBPool("java:global/server/RouteServiceImpl!itmo.server.services.RouteService");

    public RouteResource() throws NamingException {
    }

    private RouteService getFromEJBPool(String name) throws NamingException {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  WildFlyInitialContextFactory.class.getName());
        final Context context = new InitialContext(jndiProperties);
        return (RouteService) context.lookup(name);
    }


    HashMap<String, String> filterParameters = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/")
    public Route getRoute(@PathParam("id") Integer id) {
        return routeService.getRoute(id);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> getAllRoutes(@DefaultValue("+id") @QueryParam("sort") String sortList,
                                    @DefaultValue("1") @QueryParam("page") Integer page,
                                    @DefaultValue("10") @QueryParam("page_size") Integer page_size,
                                    @QueryParam("id") String id,
                                    @QueryParam("name") String name,
                                    @QueryParam("distance") String distance,
                                    @QueryParam("creationDate") String creationDate,
                                    @QueryParam("coordinates_id") String coordinatesId,
                                    @QueryParam("coordinates_x") String coordinatesX,
                                    @QueryParam("coordinates_y") String coordinatesY,
                                    @QueryParam("from_id") String fromId,
                                    @QueryParam("from_x") String fromX,
                                    @QueryParam("from_y") String fromY,
                                    @QueryParam("from_z") String fromZ,
                                    @QueryParam("to_id") String toId,
                                    @QueryParam("to_x") String toX,
                                    @QueryParam("to_y") String toY,
                                    @QueryParam("to_z") String toZ
                                 ) throws JsonProcessingException {
        putParameter("id", id);
        putParameter("name", name);
        putParameter("distance", distance);
        putParameter("creationDate", creationDate);
        putParameter("coordinates_id", coordinatesId);
        putParameter("coordinates_x", coordinatesX);
        putParameter("coordinates_y", coordinatesY);
        putParameter("from_id", fromId);
        putParameter("from_x", fromX);
        putParameter("from_y", fromY);
        putParameter("from_z", fromZ);
        putParameter("to_id", toId);
        putParameter("to_x", toX);
        putParameter("to_y", toY);
        putParameter("to_z", toZ);
        String jsonResult = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(filterParameters);
        return routeService.getAllRoutes(sortList, filterParameters, page, page_size);
    }

    private void putParameter(String k, String v) {
        if (v != null && !v.isEmpty()){
            filterParameters.put(k, v);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addRoute(@Valid RouteDto routeDto){
        routeService.addRoute(routeDto);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/")
    public void deleteRoute(@PathParam("id") Integer id) {
        routeService.deleteRoute(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/")
    public void updateRoute(@PathParam("id") Integer id, @Valid RouteDto routeDto){
        routeService.updateRoute(id, routeDto);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count/distance/equals/{distance}")
    public long countRoutesDistanceEq(@PathParam("distance") int distance){
        return routeService.countDistanceEquals(distance);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count/distance/greater/{distance}")
    public long countRoutesDistanceGt(@PathParam("distance") int distance){
        return routeService.countDistanceGreater(distance);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/name/contains/{substrInName}")
    public List<Route> countRoutesDistanceGt(@PathParam("substrInName") String substr){
        return routeService.findNameContainsSubstr(substr);
    }
}