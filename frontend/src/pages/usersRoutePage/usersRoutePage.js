import { useState } from "react";
import styles from "./usersRoutePage.module.css";
import UsersRouteMap from "../../components/usersRouteMap/usersRouteMap";
import { useRecoilState } from "recoil";
import { usersRouteItems } from "../../recoil/routeAtom";
import RouteList from "../../components/routeList/routeList";
import Navbar from "../../components/common/navbar/navbar";

const UsersRoutePage = () => {
  //Todo: 한글로 변경해야함
  const area = window.location.href.split("/")[3];

  const [clickedCardName, setClickedCardName] = useState("");
  const [routeItems, setRouteItems] = useRecoilState(usersRouteItems);
  const [markers, setMarkers] = useState([]);

  const handleMarkers = (places) => setMarkers(places);
  const updateCardName = (name) => setClickedCardName(name);

  return (
    <div className={styles.wrapper}>
      <Navbar title={`${area} 추천 루트`} />
      <div className={styles.mainContainer}>
        <section className={styles.cardContainer}>
          <RouteList
            handleMarkers={handleMarkers}
            clickedCardName={clickedCardName}
            updateCardName={updateCardName}
            routeItems={routeItems}
          />
        </section>
        <div className={styles.mapContainer}>
          <UsersRouteMap markers={markers} />
        </div>
      </div>
    </div>
  );
};

export default UsersRoutePage;
