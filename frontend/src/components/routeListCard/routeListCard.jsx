import React from "react";
import PlaceNameBox from "../placeNameBox/placeNameBox";
import styles from "./routeListCard.module.css";
const RouteListCard = ({
  item,
  handleMarkers,
  clickedCardName,
  updateCardName,
}) => {
  const handleClick = () => {
    handleMarkers(item.places);
    updateCardName(item.routeName);
  };
  return (
    <div className={styles.RouteListCard} onClick={handleClick}>
      <div className={styles.routeName}>
        <span>{item.routeName}</span>
      </div>
      {clickedCardName === item.routeName &&
        item.places.map((v, index) => <PlaceNameBox key={index} item={v} />)}
    </div>
  );
};

export default RouteListCard;
