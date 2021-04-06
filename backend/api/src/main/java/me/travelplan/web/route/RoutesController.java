package me.travelplan.web.route;

import lombok.RequiredArgsConstructor;
import me.travelplan.security.userdetails.CurrentUser;
import me.travelplan.security.userdetails.CustomUserDetails;
import me.travelplan.service.route.Route;
import me.travelplan.service.route.RouteMapper;
import me.travelplan.service.route.RouteService;
import me.travelplan.web.common.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/routes")
public class RoutesController {
    private final RouteService routeService;
    private final RouteMapper routeMapper;

    @GetMapping
    public Page<RouteResponse.GetList> getList(PageRequest pageRequest,
                                               @RequestParam("maxX") Double maxX,
                                               @RequestParam("maxY") Double maxY,
                                               @RequestParam("minX") Double minX,
                                               @RequestParam("minY") Double minY,
                                               RouteRequest.GetList request) {
        System.out.println(pageRequest.getPage());
        System.out.println(pageRequest.getSize());
        System.out.println("maxX" + maxX);
        System.out.println("maxX" + request.getMaxX());
        System.out.println("minX" + request.getMinX());
        System.out.println("maxY" + request.getMaxY());
        System.out.println("minY" + request.getMinY());
        Page<Route> list = routeService.getList(maxX, minX, maxY, minY, pageRequest.of());
        List<Route> content = list.getContent();
        List<RouteResponse.GetList> getList = routeMapper.toGetListResponse(content);

        return new PageImpl<>(getList, pageRequest.of(), list.getTotalElements());
//        return null;
    }

    @GetMapping("my")
    public RouteResponse.GetsWithOnlyName getMine(@CurrentUser CustomUserDetails user) {
        return routeMapper.toGetsWithOnlyNameResponse(routeService.getByUser(user.getUser()));
    }
}
