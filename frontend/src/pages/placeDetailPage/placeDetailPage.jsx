import React, { useCallback, useEffect, useState } from "react";
import DetailHeader from "../../components/detailHeader/detailHeader";
import styles from "./placeDetailPage.module.css";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import { StarFilled } from "@ant-design/icons";
import { Drawer } from "antd";
import ReviewForm from "../../components/reviewComponents/reviewForm/reviewForm";
import ReviewList from "../../components/reviewComponents/reviewList/reviewList";
import ImagesZoom from "../../components/reviewComponents/imagesZoom/imagesZoom";

const { kakao } = window;
const PlaceDetailPage = (props) => {
  const [reviewWrite, setReviewWrite] = useState(false);
  const [showImagesZoom, setShowImagesZoom] = useState(false);
  const [imageIndex, setImageIndex] = useState(0);
  const onZoom = useCallback(() => {
    setShowImagesZoom(true);
  }, []);
  const onCloseZoom = useCallback(() => {
    setShowImagesZoom(false);
  }, []);
  const onClickReviewWrite = useCallback(() => {
    setReviewWrite(true);
  }, []);
  const onCloseReviewWrite = useCallback(() => {
    setReviewWrite(false);
  }, []);
  const afterSliderChange = useCallback((index) => {
    setImageIndex(index);
  }, []);
  const images = [
    "https://blog.hmgjournal.com/images_n/contents/171013_N1.png",
    "https://blog.hmgjournal.com/images_n/contents/171013_N1.png",
    "https://blog.hmgjournal.com/images_n/contents/171013_N1.png",
    "https://blog.hmgjournal.com/images_n/contents/171013_N1.png",
  ];
  const settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };
  useEffect(() => {
    let markerPosition = new kakao.maps.LatLng(33.450701, 126.570667);
    let marker = {
      position: markerPosition,
    };
    let staticMapContainer = document.getElementById("staticMap"),
      staticMapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 4,
        marker: marker,
      };
    let staticMap = new kakao.maps.StaticMap(
      staticMapContainer,
      staticMapOption
    );
  }, []);
  return (
    <div className={styles.PlaceDetailPage}>
      <DetailHeader />
      <div className={styles.slideContainer} onClick={onZoom}>
        <Slider {...settings} afterChange={(index) => afterSliderChange(index)}>
          {images.map((imgSrc) => (
            <div className={styles.imageContainer}>
              <img className={styles.image} src={imgSrc} alt="placeImage" />
            </div>
          ))}
        </Slider>
        <div className={styles.imageCounter}>
          <span> {imageIndex + 1} / 21 </span>
        </div>
      </div>
      <div className={styles.body}>
        <div className={styles.body__header}>
          <h1 className={styles.body__placeName}>Place이름</h1>
          <div className={styles.body__placeInfo}>
            <div className={styles.body__rate}>
              <StarFilled style={{ color: "#eb2f96" }} />
              <span className={styles.body__score}>4</span>
              <span className={styles.body__reviewCount}>(6)</span>
            </div>
            <span className={styles.body__address}>장소 주소</span>
          </div>
        </div>
        <div className={styles.body__placeLocation}>
          <div className={styles.body__titleContainer}>
            <h2 className={styles.body__locationTitle}>위치</h2>
          </div>
          <div className={styles.body__addressContainer}>
            <span className={styles.body__placeAddress}>서울시 ~~~구 ~~~</span>
          </div>
          <div className={styles.body__mapContainer}>
            <div id="staticMap" className={styles.body__map}></div>
          </div>
        </div>
        <div className={styles.reviewList}>
          <ReviewList onClickReviewWrite={onClickReviewWrite} />
        </div>
      </div>
      <Drawer
        visible={reviewWrite}
        placement="bottom"
        height="100vh"
        bodyStyle={{ padding: 0 }}
        onClose={onCloseReviewWrite}
      >
        <ReviewForm />
      </Drawer>
      {showImagesZoom && (
        <ImagesZoom images={images} onClose={onCloseZoom} index={imageIndex} />
      )}
    </div>
  );
};

export default PlaceDetailPage;