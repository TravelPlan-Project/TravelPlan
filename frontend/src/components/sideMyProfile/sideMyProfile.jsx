import React, { useRef, useState } from "react";
import styles from "./sideMyProfile.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCog } from "@fortawesome/free-solid-svg-icons";

import { useRecoilState } from "recoil";
import { myRouteCart } from "../../recoil/routeAtom";
import RouteNameBox from "../routeNameBox/routeNameBox";
import { v4 as uuidv4 } from "uuid";
import Modal from "antd/lib/modal/Modal";

const SideMyProfile = (props) => {
  const [myRouteList, setMyRouteList] = useRecoilState(myRouteCart);
  const [modalVisible, setModalVisible] = useState(false);
  const inputRef = useRef();
  const handleOpen = () => {
    setModalVisible(true);
  };
  const handleClose = () => {
    setModalVisible(false);
  };
  const handleOk = (event) => {
    event.preventDefault();
    // 새로운 루트 추가할 때 api 서버로 송신
    setMyRouteList((prev) => {
      return {
        ...prev,
        [uuidv4()]: {
          routeName: inputRef.current.value,
          centerLocation: [], // centerLocation은 어떤기준으로 정해줄지 고민해볼 필요가 있다.
          places: [],
        },
      };
    });
    inputRef.current.value = "";
    handleClose();
  };
  return (
    <div className={styles.SideMyProfile}>
      <div className={styles.header}>
        <img
          className={styles.profileImage}
          src="data:image/jpeg;base64,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"
          alt="user profile image"
        />
        <span>User Nickname</span>
        <FontAwesomeIcon icon={faCog} className={styles.icon} />
      </div>
      <section className={styles.section}>
        <div className={styles.pinList}>
          <span>찜 목록</span>
        </div>
        <div className={styles.myRouteList}>
          <span>나의 루트</span>
          {myRouteList &&
            Object.keys(myRouteList).map((objKey, index) => (
              <RouteNameBox
                key={index}
                item={myRouteList[objKey]}
                objKey={objKey}
              />
            ))}
          <button className={styles.addBtn} onClick={handleOpen}>
            + 추가하기
          </button>
        </div>
      </section>
      <Modal
        title="새로운 여행 카트를 추가해요"
        style={{ marginTop: "10%" }}
        visible={modalVisible}
        onOk={handleOk}
        onCancel={handleClose}
        zIndex="1001"
      >
        <form className={styles.form} onSubmit={handleOk}>
          <input
            ref={inputRef}
            type="text"
            className={styles.input}
            placeholder="여행의 주제가 뭘까요?"
            autoFocus
          />
        </form>
      </Modal>
    </div>
  );
};

export default SideMyProfile;