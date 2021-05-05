import { useState, useEffect } from "react";
import styles from "./usersRoutePage.module.css";
import UsersRouteMap from "../../components/map/usersRouteMap/usersRouteMap";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/userState";
import { usersRouteItems } from "../../recoil/routeAtom";
import Navbar from "./navbar/navbar";
import RouteCarousel from "./routeCarousel/routeCarousel";
import BottomDrawer from "./bottomDrawer/bottomDrawer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faList } from "@fortawesome/free-solid-svg-icons";
import ListModal from "./listModal/listModal";

// import { getRoute, postRoute, postEmpty } from "../../api/routeAPI";
const UsersRoutePage = () => {
  const [routes, setRoutes] = useRecoilState(usersRouteItems); // Todo : routeAPI로 불러오기
  const [myState, setMyState] = useRecoilState(userState);
  const [places, setPlaces] = useState([]);
  const [index, setIndex] = useState(0);
  const [modalToggle, setModalToggle] = useState(false);

  const handleChange = (index) => {
    setIndex(index);
  };

  const modalHandler = () => {
    setModalToggle(!modalToggle);
  };

  useEffect(() => {
    const places = Object.values(routes)[index].places;
    setPlaces(places);
  }, [index]);

  return (
    <div className={styles.wrapper}>
      <div className={styles.navbarContainer}>
        <Navbar />
      </div>
      <UsersRouteMap places={places} routes={routes} />
      <div className={styles.routeCarousel} onDragStart={(e) => e.preventDefault()}>
        <div className={styles.box1}>
          <div className={styles.searchHere}>
            <button className={styles.serachHereBtn} />
            <div className={styles.serachHereTitle}>현 지도에서 검색</div>
          </div>
          <button className={styles.listButton} onClick={modalHandler}>
            <FontAwesomeIcon icon={faList} color="white" size="lg" />
          </button>
        </div>
        <RouteCarousel routes={routes} handleChange={(e) => handleChange(e)} />
      </div>
      {modalToggle && (
        <div className={styles.modalContainer}>
          <ListModal places={places} index={index} modalHandler={modalHandler} />
        </div>
      )}
    </div>
  );
};

export default UsersRoutePage;
