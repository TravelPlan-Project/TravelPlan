package me.travelplan.web.route;

import me.travelplan.security.userdetails.CustomUserDetails;
import me.travelplan.service.file.domain.File;
import me.travelplan.service.file.domain.FileServer;
import me.travelplan.service.file.domain.FileType;
import me.travelplan.service.place.domain.Place;
import me.travelplan.service.place.domain.PlaceCategory;
import me.travelplan.service.route.domain.Route;
import me.travelplan.web.common.UserDto;
import me.travelplan.web.route.dto.RouteDto;
import me.travelplan.web.route.dto.RouteRequest;
import me.travelplan.web.route.dto.RouteResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RouteMapper {
    RouteResponse.RouteId toRouteIdResponse(Route route);

    default Place toPlace(RouteRequest.AddPlace request) {
        return Place.builder()
                .id(request.getPlace().getId())
                .name(request.getPlace().getName())
                .url(request.getPlace().getName())
                .category(PlaceCategory.builder().id(request.getPlace().getCategory()).build())
                .thumbnail(File.builder()
                        .name(request.getPlace().getName())
                        .extension("")
                        .height(0)
                        .width(0)
                        .server(FileServer.EXTERNAL)
                        .size(0L).type(FileType.IMAGE)
                        .url(request.getPlace().getImage())
                        .build())
                .x(request.getPlace().getX())
                .y(request.getPlace().getY())
                .build();
    }

    default RouteResponse.GetOne toGetOneResponse(Route route) {
        List<RouteDto.RoutePlace> routePlaces = new ArrayList<>();

        route.getRoutePlaces().forEach(routePlace -> {
            RouteDto.RoutePlace.RoutePlaceBuilder routePlaceBuilder = RouteDto.RoutePlace.builder();
            routePlaceBuilder.order(routePlace.getOrder());

            Place place = routePlace.getPlace();
            RouteDto.RoutePlace.RoutePlaceBuilder builder = routePlaceBuilder
                    .id(place.getId())
                    .name(place.getName())
                    .x(place.getX())
                    .y(place.getY())
                    .address(place.getAddress())
                    .category(place.getCategory().getName())
                    .url(place.getUrl());
            //TODO 현재 구현에는 place에 image가 null로 들어가고 테스트에서는 image가 들어가기 때문에 조건문처리를 해놓음
            // place에 image가 확실히 들어가게 되면 조건문 제거
            if (place.getThumbnail() != null) {
                routePlaces.add(builder.image(place.getThumbnail().getUrl()).build());
            } else {
                routePlaces.add(builder.build());
            }
        });

        HashMap<String, Double> map = route.calculateCenterCoordinate();
        Double centerX = map.get("centerX");
        Double centerY = map.get("centerY");

        return RouteResponse.GetOne.builder()
                .name(route.getName())
                .centerX(centerX)
                .centerY(centerY)
                .score(route.getAverageReviewScore())
                .creator(UserDto.Response.from(route.getCreatedBy()))
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .reviewCount(route.getRouteReviews().size())
                .places(routePlaces)
                .build();
    }

    default List<RouteResponse.GetMine> toGetMineResponse(List<Route> routes) {
        return routes.stream().map(route -> RouteResponse.GetMine.builder()
                .id(route.getId())
                .name(route.getName()).image(route.getRoutePlaces().get(0).getPlace().getThumbnail().getUrl()).build())
                .collect(Collectors.toList());

    }

    default List<RouteResponse.GetListByRegion> toListResponse(List<Route> routes) {
        //TODO 업로드된 사진 추가
        List<RouteResponse.GetListByRegion> getList = new ArrayList<>();
        routes.forEach(route -> {
            List<RouteDto.PlaceWithIdAndNameAndOrder> place = new ArrayList<>();
            route.getRoutePlaces().forEach(routePlace -> {
                RouteDto.PlaceWithIdAndNameAndOrder placeWithIdAndNameAndOrder = RouteDto.PlaceWithIdAndNameAndOrder.builder()
                        .id(routePlace.getPlace().getId())
                        .name(routePlace.getPlace().getName())
                        .order(routePlace.getOrder())
                        .build();
                place.add(placeWithIdAndNameAndOrder);
            });

            RouteResponse.GetListByRegion list = RouteResponse.GetListByRegion.builder()
                    .id(route.getId())
                    .name(route.getName())
                    .region(route.getRegion())
                    .creator(UserDto.Response.from(route.getCreatedBy()))
                    .places(place)
                    .build();
            getList.add(list);
        });
        return getList;
    }

    default List<RouteResponse.GetListByCoordinate> toListResponse(List<Route> routes, CustomUserDetails customUserDetails) {
        List<RouteResponse.GetListByCoordinate> getList = new ArrayList<>();
        routes.forEach(route -> {
            List<RouteDto.RoutePlace> routePlaces = new ArrayList<>();
            route.getRoutePlaces().forEach(routePlace -> {
                RouteDto.RoutePlace.RoutePlaceBuilder routePlaceBuilder = RouteDto.RoutePlace.builder()
                        .id(routePlace.getPlace().getId())
                        .name(routePlace.getPlace().getName())
                        .address(routePlace.getPlace().getAddress())
                        .x(routePlace.getPlace().getX())
                        .y(routePlace.getPlace().getY())
                        .category(routePlace.getPlace().getCategory().getName())
                        .url(routePlace.getPlace().getUrl())
                        .order(routePlace.getOrder());
                //TODO 현재 구현에는 place에 image가 null로 들어가고 테스트에서는 image가 들어가기 때문에 조건문처리를 해놓음
                // place에 image가 확실히 들어가게 되면 조건문 제거
                if (routePlace.getPlace().getThumbnail() != null) {
                    routePlaces.add(routePlaceBuilder.image(routePlace.getPlace().getThumbnail().getUrl()).build());
                } else {
                    routePlaces.add(routePlaceBuilder.build());
                }
            });
            HashMap<String, Double> map = route.calculateCenterCoordinate();
            Double centerX = map.get("centerX");
            Double centerY = map.get("centerY");

            RouteResponse.GetListByCoordinate list = RouteResponse.GetListByCoordinate.builder()
                    .id(route.getId())
                    .name(route.getName())
                    .centerX(centerX)
                    .centerY(centerY)
                    .likes(route.getRouteLikes().size())
                    .isLike(route.isLike(customUserDetails))
                    .createdBy(route.getCreatedBy().getName())
                    .places(routePlaces)
                    .build();
            getList.add(list);
        });
        return getList;
    }
}
