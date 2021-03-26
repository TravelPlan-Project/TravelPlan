package me.travelplan.web;

import io.jsonwebtoken.lang.Collections;
import me.travelplan.MvcTest;
import me.travelplan.WithMockCustomUser;
import me.travelplan.service.file.File;
import me.travelplan.service.place.Place;
import me.travelplan.service.route.*;
import me.travelplan.web.route.RouteController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.travelplan.ApiDocumentUtils.getDocumentRequest;
import static me.travelplan.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
@Import({
        RouteMapperImpl.class
})
public class RouteControllerTest extends MvcTest {
    @MockBean
    RouteService routeService;

    @Test
    @WithMockCustomUser
    @DisplayName("경로 생성 테스트")
    public void putTest() throws Exception {
        String request = "{\n" +
                "  \"name\": \"나의 테스트 경로\",\n" +
                "  \"places\": [\n" +
                "    {\n" +
                "      \"id\" : 123,\n" +
                "      \"image\" : \"https://www.gn.go.kr/tour/images/tour/main_new/mvisual_img07.jpg\",\n" +
                "      \"name\" : \"강릉바닷가\",\n" +
                "      \"url\" : \"https://www.gn.go.kr/tour/index.do\",\n" +
                "      \"x\" : 37.748125,\n" +
                "      \"y\" : 128.878996,\n" +
                "      \"order\" : 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\" : 124,\n" +
                "      \"image\" : \"https://cf.bstatic.com/xdata/images/hotel/270x200/129750773.jpg?k=d338049190ff48b19261ee5f516ee563aaeb8aeb97c4774c1e171e402cf25891&o=\",\n" +
                "      \"name\" : \"강릉 어린이집\",\n" +
                "      \"url\" : \"https://kr.hotels.com/go/south-korea/kr-best-gangneung-things-to-do\",\n" +
                "      \"x\" : 37.748130,\n" +
                "      \"y\" : 128.8789333,\n" +
                "      \"order\" : 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ResultActions results = mockMvc.perform(
                put("/route")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(document("route-put",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("경로 이륾"),
                                fieldWithPath("places").type(JsonFieldType.ARRAY).description("장소들 정보"),
                                fieldWithPath("places[].id").type(JsonFieldType.NUMBER).description("카카오톡에서 제공한 장소 식별자"),
                                fieldWithPath("places[].image").type(JsonFieldType.STRING).description("장소 이미지 URL"),
                                fieldWithPath("places[].name").type(JsonFieldType.STRING).description("장소 이름"),
                                fieldWithPath("places[].url").type(JsonFieldType.STRING).description("장소 URL"),
                                fieldWithPath("places[].x").type(JsonFieldType.NUMBER).description("장소 X값"),
                                fieldWithPath("places[].y").type(JsonFieldType.NUMBER).description("장소 Y값"),
                                fieldWithPath("places[].order").type(JsonFieldType.NUMBER).description("장소들 정렬 순서 (사용할 필요가 있는지 검토 필요)")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("경로 식별자로 가져오기 테스트")
    public void getOneTest() throws Exception {
        Route route = Route.builder()
                .id(1L)
                .name("테스트 경로")
                .x(97.123)
                .y(124.124)
                .build();
        route.addPlace(RoutePlace.builder().order(1).place(
                Place.builder()
                        .id(12L)
                        .name("테스트 장소 이름")
                        .x(97.123)
                        .y(124.124)
                        .url("https://www.naver.com")
                        .image(File.builder().url("http://loremflickr.com/440/440").build())
                        .build()
        ).build());
        route.addPlace(RoutePlace.builder().order(2).place(
                Place.builder()
                        .id(12L)
                        .name("강릉 해돋이 마을")
                        .x(97.124)
                        .y(124.124)
                        .url("https://www.naver.com")
                        .image(File.builder().url("http://loremflickr.com/440/440").build())
                        .build()
        ).build());

        given(routeService.getOne(any())).willReturn(route);

        ResultActions results = mockMvc.perform(
                get("/route/{id}", 1)
        );

        results.andExpect(status().isOk())
                .andDo(document("route-getOne",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("경로 식별자")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("경로 이름"),
                                fieldWithPath("x").type(JsonFieldType.NUMBER).description("경로의 x (장소들의 중심좌표 x)"),
                                fieldWithPath("y").type(JsonFieldType.NUMBER).description("경로의 y (장소들의 중심좌표 y)"),
                                fieldWithPath("places").type(JsonFieldType.ARRAY).description("경로의 장소들"),
                                fieldWithPath("places[].id").type(JsonFieldType.NUMBER).description("장소 식별자"),
                                fieldWithPath("places[].url").type(JsonFieldType.STRING).description("장소 URL"),
                                fieldWithPath("places[].image").type(JsonFieldType.STRING).description("장소 이미지 URL"),
                                fieldWithPath("places[].name").type(JsonFieldType.STRING).description("장소 이름"),
                                fieldWithPath("places[].x").type(JsonFieldType.NUMBER).description("장소 X값"),
                                fieldWithPath("places[].y").type(JsonFieldType.NUMBER).description("장소 Y값"),
                                fieldWithPath("places[].order").type(JsonFieldType.NUMBER).description("장소 정렬 값 (사용할 필요가 있는지 검토해봐야함)")
                        )
                ));
    }
}
