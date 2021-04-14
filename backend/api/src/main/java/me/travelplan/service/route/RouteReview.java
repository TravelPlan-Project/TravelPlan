package me.travelplan.service.route;

import lombok.*;
import me.travelplan.config.jpa.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "routes_reviews")
public class RouteReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToMany(mappedBy = "routeReview", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<RouteReviewFile> routeReviewFiles = new ArrayList<>();

    public void setRoute(Route route) {
        this.route = route;
        route.getRouteReviews().add(this);
    }

    public void addRouteReviewFiles(List<RouteReviewFile> routeReviewFiles) {
        routeReviewFiles.forEach(reviewFile -> {
            this.routeReviewFiles.add(reviewFile);
            reviewFile.setRouteReview(this);
        });
    }

    public void update(Double score, String content) {
        this.score = score;
        this.content = content;
    }

    public static RouteReview create(Double score, String content, List<RouteReviewFile> routeReviewFiles, Route route) {
        RouteReview routeReview = RouteReview.builder()
                .score(score)
                .content(content)
                .build();
        routeReview.addRouteReviewFiles(routeReviewFiles);
        routeReview.setRoute(route);

        return routeReview;
    }
}