package me.travelplan.web;

import me.travelplan.MvcTest;
import me.travelplan.WithMockCustomUser;
import me.travelplan.service.file.File;
import me.travelplan.service.place.Place;
import me.travelplan.service.place.PlaceCategory;
import me.travelplan.service.route.Route;
import me.travelplan.service.route.RouteMapperImpl;
import me.travelplan.service.route.RoutePlace;
import me.travelplan.service.route.RouteService;
import me.travelplan.web.route.RoutesController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static me.travelplan.ApiDocumentUtils.getDocumentRequest;
import static me.travelplan.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoutesController.class)
@Import({
        RouteMapperImpl.class
})
public class RoutesControllerTest extends MvcTest {
    @MockBean
    private RouteService routeService;

    @Test
    @WithMockCustomUser
    @DisplayName("내 경로들 가져오기 테스트(이름만)")
    public void getMyTest() throws Exception {
        List<Route> routes = new ArrayList<>();
        routes.add(Route.builder().id(1L).name("첫번째 경로").build());
        routes.add(Route.builder().id(2L).name("두번째 경로").build());

        given(routeService.getByUser(any())).willReturn(routes);

        ResultActions results = mockMvc.perform(
                get("/routes/my")
        );

        results.andExpect(status().isOk())
                .andDo(document("routes-getMy",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("routes").type(JsonFieldType.ARRAY).description("경로들"),
                                fieldWithPath("routes[].id").type(JsonFieldType.NUMBER).description("경로 식별자"),
                                fieldWithPath("routes[].name").type(JsonFieldType.STRING).description("경로 이름")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("현재 지도 안에 포함되어 있는 경로 가져오기")
    public void getListTest() throws Exception {
        List<Route> routes = new ArrayList<>();
        routes.add(Route.builder().id(1L).name("첫번째 경로").build());
        routes.add(Route.builder().id(2L).name("두번째 경로").build());
        Route route = Route.builder()
                .id(1L)
                .maxX(97.123)
                .maxY(124.124)
                .minX(97.124)
                .minY(124.123)
                .name("세번째 경로")
                .build();
        route.addPlace(RoutePlace.builder().order(1).place(
                Place.builder()
                        .id(12L)
                        .name("테스트 장소 이름")
                        .x(97.123)
                        .y(124.123)
                        .url("https://www.naver.com")
                        .category(PlaceCategory.builder().id("CE7").name("카페").build())
                        .image(File.builder().url("http://loremflickr.com/440/440").build())
                        .build()
        ).build());
        route.addPlace(RoutePlace.builder().order(2).place(
                Place.builder()
                        .id(12L)
                        .name("강릉 해돋이 마을")
                        .x(97.124)
                        .y(124.124)
                        .category(PlaceCategory.builder().id("CE7").name("카페").build())
                        .url("https://www.naver.com")
                        .image(File.builder().url("http://loremflickr.com/440/440").build())
                        .build()
        ).build());
        routes.add(route);
        given(routeService.getList(any(), any(), any(), any(), any())).willReturn(new PageImpl<>(routes, PageRequest.of(0, 10), routes.size()));

        ResultActions results = mockMvc.perform(
                get("/routes")
                        .param("page", "0")
                        .param("size", "10")
                        .param("maxX", "37.5")
                        .param("minX", "36.5")
                        .param("maxY", "123.5")
                        .param("minY", "123.2")

        );

        results
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("routes-getList",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지"),
                                parameterWithName("size").description("조회할 경로수"),
                                parameterWithName("maxX").description("현지도에서 가장 큰 x좌표(가장 오른쪽 x좌표)"),
                                parameterWithName("minX").description("현지도에서 가장 작은 x좌표(가장 왼쪽 x좌표)"),
                                parameterWithName("maxY").description("현지도에서 가장 큰 y좌표(가장 위의 y좌표)"),
                                parameterWithName("minY").description("현지도에서 가장 작은 y좌표(가장 아래의 y좌표)")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("경로 이름"),
                                fieldWithPath("content[].places").type(JsonFieldType.ARRAY).description("장소들 정보"),
                                fieldWithPath("content[].places[].id").type(JsonFieldType.NUMBER).description("카카오톡에서 제공한 장소 식별자"),
                                fieldWithPath("content[].places[].image").type(JsonFieldType.STRING).description("장소 이미지 URL"),
                                fieldWithPath("content[].places[].name").type(JsonFieldType.STRING).description("장소 이름"),
                                fieldWithPath("content[].places[].url").type(JsonFieldType.STRING).description("장소 URL"),
                                fieldWithPath("content[].places[].x").type(JsonFieldType.NUMBER).description("장소 X값"),
                                fieldWithPath("content[].places[].y").type(JsonFieldType.NUMBER).description("장소 Y값"),
                                fieldWithPath("content[].places[].category").type(JsonFieldType.STRING).description("카테고리 분류 코드"),
                                fieldWithPath("content[].places[].order").type(JsonFieldType.NUMBER).description("장소들 정렬 순서 (사용할 필요가 있는지 검토 필요)"),
                                fieldWithPath("totalElements").description("전체 개수"),
                                fieldWithPath("last").description("마지막 페이지인지 식별"),
                                fieldWithPath("totalPages").description("전체 페이지")
                        )
                ));
    }
}
