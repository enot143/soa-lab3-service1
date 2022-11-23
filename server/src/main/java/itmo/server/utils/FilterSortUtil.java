package itmo.server.utils;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class FilterSortUtil {

    private static final ArrayList<CriteriaDef> whiteList = new ArrayList<>();
    static {
        whiteList.add(new CriteriaDef("id", "id", Table.ROUTE));
        whiteList.add(new CriteriaDef("name", "name", Table.ROUTE));
        whiteList.add(new CriteriaDef("creationDate", "creationDate", Table.ROUTE));
        whiteList.add(new CriteriaDef("distance", "distance", Table.ROUTE));
        whiteList.add(new CriteriaDef("coordinates_id", "id", Table.COORDINATE));
        whiteList.add(new CriteriaDef("coordinates_x", "x", Table.COORDINATE));
        whiteList.add(new CriteriaDef("coordinates_y", "y", Table.COORDINATE));
        whiteList.add(new CriteriaDef("from_id", "id", Table.FROM));
        whiteList.add(new CriteriaDef("from_x", "x", Table.FROM));
        whiteList.add(new CriteriaDef("from_y", "y", Table.FROM));
        whiteList.add(new CriteriaDef("from_z", "z", Table.FROM));
        whiteList.add(new CriteriaDef("to_id", "id", Table.TO));
        whiteList.add(new CriteriaDef("to_x", "x", Table.TO));
        whiteList.add(new CriteriaDef("to_y", "y", Table.TO));
        whiteList.add(new CriteriaDef("to_z", "z", Table.TO));
    }


    public static List<SortOrder> getOrdersList(String sortingCriteria) {
        Set<String> sortingFileds = new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sortingCriteria, ""), ",")));

        List<SortOrder> sortingOrders = sortingFileds.stream().map(FilterSortUtil::getOrder)
                .collect(Collectors.toList());
        return sortingOrders;
    }


    public static ArrayList<Filter> getFiltersList(HashMap<String, String> filterParameters){
        ArrayList<Filter> filtersList = new ArrayList<>();
        filterParameters.forEach((key, value) ->{
            Filter filter = new Filter();
            ArrayList<String> splitString = new ArrayList<>(Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(value, ""), ":")));
            if (splitString.size() == 1){
                throw new BadRequestException("Unexpected filter");
            }
            if (!(splitString.size() == 2 || splitString.size() == 3)){
                throw new BadRequestException("Too many arguments in filter");
            }
            String operation = splitString.get(0);
            if (Filter.Operation.contains(operation)) {
                for (CriteriaDef s : whiteList) {
                    if (s.getCriteria().equals(key)) {
                        filter.setDef(s);
                    }
                }
            } else throw new BadRequestException("Filter operation doesn't exist");
            filter.setOperation(Filter.Operation.fromString(operation));
            if (filter.getOperation() == Filter.Operation.BETWEEN && filter.getDef().getNameOfColumn().equals("name")){
                throw new BadRequestException("Filter between is not supported for strings");
            }
            if ((filter.getOperation() == Filter.Operation.LIKE ||
                    filter.getOperation() == Filter.Operation.EQ) &&
                    !filter.getDef().getNameOfColumn().equals("name")){
                throw new BadRequestException("Filter like/equals is supported only for strings");
            }
            if (filter.getOperation() == Filter.Operation.BETWEEN && splitString.size() != 3) {
                throw new BadRequestException("Not enough arguments in filter");
            }
            if (filter.getOperation() != Filter.Operation.BETWEEN && splitString.size() == 3){
                throw new BadRequestException("Too many arguments in filter");
            }
            filter.setVar1(splitString.get(1));

            if (filter.getOperation() == Filter.Operation.BETWEEN){
                if (!filter.getDef().getNameOfColumn().equals("creationDate")){
                    checkStringCast(filter.getVar1());
                } else {
                    checkDateCast(filter.getVar1());
                }
            }
            if (splitString.size() == 3) {
                filter.setVar2(splitString.get(2));
                if (!filter.getDef().getNameOfColumn().equals("creationDate")){
                    checkStringCast(filter.getVar1());
                } else {
                    checkDateCast(filter.getVar2());
                }
            }
            filtersList.add(filter);
        });
        return filtersList;
    }


    protected static void checkDateCast(String s){
        try{
            LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (DateTimeParseException exception) {
            throw new BadRequestException("Unable to cast string in filter to date");
        }
    }
    protected static void checkStringCast(String s){
        try{
            Double.parseDouble(s);
        }catch (NumberFormatException exception) {
            throw new BadRequestException("Unable to cast string in filter to number");
        }
    }

    protected static SortOrder getOrder(String value) {
        SortOrder sortOrder;
        if (StringUtils.startsWith(value, "-")) {
            sortOrder = getSortOrder(value, "-");
            sortOrder.setAsc(false);
            return sortOrder;
        } else if (StringUtils.startsWith(value, "+")){
            sortOrder = getSortOrder(value, "+");
            sortOrder.setAsc(true);
            return sortOrder;
        }  else if (StringUtils.startsWith(value, " ")){
            sortOrder = getSortOrder(value, " ");
            sortOrder.setAsc(true);
            return sortOrder;
        } else throw new BadRequestException("unexpected symbols in the URL query");
    }

    protected static SortOrder getSortOrder(String value, String separator){
        String sortCriteria = StringUtils.substringAfter(value, separator);
        for (CriteriaDef s : whiteList){
            if (s.getCriteria().equals(sortCriteria)){
                return new SortOrder(s);
           }
        }
        throw new BadRequestException("this sorting criteria doesn't exist");
    }

}
