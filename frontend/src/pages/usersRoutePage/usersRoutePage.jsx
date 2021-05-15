import { useState, useEffect } from "react";
import styles from "./usersRoutePage.module.css";
import { useLocation } from "react-router-dom";
import UsersRouteMap from "../../components/map/usersRouteMap/usersRouteMap";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/userState";
import { usersRouteItems } from "../../recoil/routeAtom";
import Navbar from "./navbar/navbar";
import RouteCarousel from "./routeCarousel/routeCarousel";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faList, faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import PlaceListModal from "./placeListModal/placeListModal";
//Todo
//route 드래그할 때 마다 루트 10개씩 가져옴 -> setRoutes

const UsersRoutePage = () => {
  const [routes, setRoutes] = useRecoilState(usersRouteItems); // Todo : routeAPI로 불러오기
  const [myState, setMyState] = useRecoilState(userState);
  const [places, setPlaces] = useState([]);
  const [index, setIndex] = useState(0);
  const [map, setMap] = useState();
  const [modalToggle, setModalToggle] = useState(false);
  const [searchHere, setSearchHere] = useState(false);
  const location = useLocation();

  const mapHandler = (map) => setMap(map);
  const routesHandler = (routes) => setRoutes(routes);
  const indexHandler = (index) => setIndex(index);
  const modalHandler = () => setModalToggle(!modalToggle);
  const zoomHandler = (level) => map.setLevel(level, { animate: { duration: 120 } });
  const searchHereHandler = (e) => {
    //Todo
    e.target.style = searchHere ? "background-color: white;" : "background-color: black;";
    setSearchHere(!searchHere);
  };

  useEffect(() => {
    setPlaces(Object.values(routes)[index].places);
  }, [index]);

  useEffect(() => {}, [searchHere]);

  return (
    <div className={styles.wrapper}>
      <div className={styles.navbarContainer}>
        <Navbar />
      </div>
      <UsersRouteMap mapHandler={mapHandler} routesHandler={routesHandler} places={places} routes={routes} />
      <div className={styles.routeCarousel} onDragStart={(e) => e.preventDefault()}>
        <div className={styles.box1}>
          <div className={styles.searchHere}>
            <button onClick={searchHereHandler} />
            <div>현 지도에서 검색</div>
          </div>
          <div className={styles.box2}>
            <button
              className={styles.plusButton}
              onClick={() => {
                map && zoomHandler(map.getLevel() - 1);
              }}
            >
              <FontAwesomeIcon icon={faPlus} color="#7B8293" size="lg" />
            </button>
            <button
              className={styles.minusButton}
              onClick={() => {
                map && zoomHandler(map.getLevel() + 1);
              }}
            >
              <FontAwesomeIcon icon={faMinus} color="#7B8293" size="lg" />
            </button>
            <button className={styles.modalButton} onClick={modalHandler}>
              <FontAwesomeIcon icon={faList} color="white" size="lg" />
            </button>
          </div>
        </div>
        <RouteCarousel routes={routes} indexHandler={(e) => indexHandler(e)} />
      </div>

      {modalToggle && (
        <div className={styles.modalContainer}>
          <PlaceListModal routes={routes} modalHandler={modalHandler} />
        </div>
      )}
    </div>
  );
};

export default UsersRoutePage;
