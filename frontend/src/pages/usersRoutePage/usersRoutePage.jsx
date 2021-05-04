import { useState, useEffect } from "react";
import styles from "./usersRoutePage.module.css";
import UsersRouteMap from "../../components/map/usersRouteMap/usersRouteMap";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/userState";
import { usersRouteItems } from "../../recoil/routeAtom";
import RouteCarousel from "./routeCarousel/routeCarousel";
import BottomDrawer from "./bottomDrawer/bottomDrawer";

// import { getRoute, postRoute, postEmpty } from "../../api/routeAPI";
const UsersRoutePage = () => {
  const [routes, setRoutes] = useRecoilState(usersRouteItems); // Todo : routeAPI로 불러오기
  const [myState, setMyState] = useRecoilState(userState);
  const [places, setPlaces] = useState([]);
  const [index, setIndex] = useState(0);

  const handleChange = (index) => {
    setIndex(index);
  };

  useEffect(() => {
    const places = Object.values(routes)[index].places;
    setPlaces(places);
  }, [index]);

  return (
    <div className={styles.wrapper}>
      <UsersRouteMap places={places} routes={routes} />
      <div className={styles.routeCarousel} onDragStart={(e) => e.preventDefault()}>
        <RouteCarousel routes={routes} handleChange={(e) => handleChange(e)} />
      </div>
      <div className={styles.bottomDrawer}>
        <BottomDrawer routes={routes} index={index} />
      </div>
    </div>
  );
};

export default UsersRoutePage;
