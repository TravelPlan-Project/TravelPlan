import React, { useCallback, useState } from "react";
import styles from "./hotplacePage.module.css";
import HotplaceMap from "../../components/map/hotplaceMap/hotplaceMap";
import PortalCart from "../../containers/portalCart/portalCart";
import useInput from "../../hooks/useInput";

const HotplacePage = () => {
  const [cartPortal, setCartPortal] = useState(false);
  const [place, setPlace] = useState({});
  const [inputKeyword, handleInputKeyword] = useInput();
  const [searchPlace, setSearchPlace] = useState("");
  const handleCartPortal = useCallback(() => {
    setCartPortal(!cartPortal);
  }, []);
  const clickedPlace = useCallback((place) => {
    setPlace(place);
  }, []);
  const handleSubmit = useCallback(
    (e) => {
      e.preventDefault();
      if (inputKeyword === searchPlace) {
        setSearchPlace(inputKeyword + " ");
      } else {
        setSearchPlace(inputKeyword);
      }
    },
    [inputKeyword, searchPlace]
  );
  return (
    <div className={styles.HotplacePage}>
      <header className={styles.navbar}>navbar</header>
      <div className={styles.body}>
        <div className={styles.searchContainer}>
          <div className={styles.search}>
            <form className={styles.form} onSubmit={handleSubmit}>
              <input
                placeholder="Search place..."
                onChange={handleInputKeyword}
                value={inputKeyword || ""}
              />
              <button type="submit">검색</button>
            </form>
          </div>
          <div className={styles.searchList}>
            <ul></ul>
          </div>
        </div>
        <div className={styles.mapContainer}>
          <HotplaceMap
            handleCartPortal={handleCartPortal}
            clickedPlace={clickedPlace}
            searchPlace={searchPlace}
          />
        </div>
      </div>
      {cartPortal && (
        <PortalCart place={place} handleCartPortal={handleCartPortal} />
      )}
    </div>
  );
};

export default HotplacePage;
