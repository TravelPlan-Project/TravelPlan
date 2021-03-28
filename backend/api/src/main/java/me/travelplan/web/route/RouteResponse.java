package me.travelplan.web.route;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RouteResponse {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetOne {
        private String name;
        private Double x;
        private Double y;
        private List<RouteDto.RoutePlace> places;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetsWithOnlyName {
        List<RouteDto.RouteWithOnlyName> routes;
    }
}