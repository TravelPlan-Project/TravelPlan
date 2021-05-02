package me.travelplan.service.place;

import lombok.*;
import me.travelplan.config.jpa.BaseEntity;
import me.travelplan.service.file.File;
import me.travelplan.service.kakaomap.KakaoMapPlace;
import me.travelplan.service.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "places")
public class Place extends BaseEntity {
    @Id
    private Long id;
    private String name;
    private String url;
    private Double x;
    private Double y;
    private String address;
    @Enumerated(EnumType.STRING)
    private DetailStatus detailStatus;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File thumbnail;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PlaceCategory category;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<PlaceReview> reviews;

    @OneToMany(mappedBy = "place")
    @Builder.Default
    private List<PlaceLike> placeLikes = new ArrayList<>();

    /**
     * Detail
     */
    @OneToMany(mappedBy = "place")
    private List<PlaceImage> images;
    private String openHour;

    public boolean isLike(User user) {
        return this.placeLikes.stream().anyMatch(placeLike -> placeLike.getCreatedBy().getId().equals(user.getId()));
    }

    public Place setFromKakaoMapPlace(KakaoMapPlace kakaoMapPlace) {
        this.detailStatus = DetailStatus.COMPLETE;
        this.thumbnail = File.createExternalImage(kakaoMapPlace.getThumbnail());
        try {
            this.openHour = kakaoMapPlace.getOpenHour().get(0).getTimeList().get(0).getTimeSE();
        } catch (Exception ex) {
            // ignore
        }

        return this;
    }
}
