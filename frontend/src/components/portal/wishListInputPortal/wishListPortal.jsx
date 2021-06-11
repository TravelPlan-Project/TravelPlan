import React, { useEffect, useRef } from "react";
import Portal from "../portal";
import styles from "./wishListPortal.module.css";

const WishListPortal = ({ onClose, handleWishlistName }) => {
  const portalRef = useRef();
  let wishlistName;

  const handleClose = (event) => {
    if (
      portalRef.current &&
      !portalRef.current.contains(event.target) &&
      event.target.nodeName !== "SPAN" // span을 클릭할 때 리렌더링되는 이유로 예외케이스 추가
    ) {
      onClose();
    }
  };
  useEffect(() => {
    window.addEventListener("click", handleClose);
    return () => {
      window.removeEventListener("click", handleClose);
    };
  }, [handleClose]);
  return (
    <Portal>
      <div className={styles.portalContainer}>
        <div ref={portalRef} className={styles.portalBody}>
          <div className={styles.header}>
            <span className={styles.title}>찜 목록 이름을 입력해 주세요.</span>
          </div>
          <form className={styles.form}>
            <input className={styles.inputBar} autoFocus onInput={(e) => (wishlistName = e.target.value)} />
            <button
              onClick={(e) => {
                e.preventDefault();
                handleWishlistName(wishlistName);
                onClose();
              }}
              className={styles.inputButton}
            >
              확인
            </button>
          </form>
        </div>
      </div>
    </Portal>
  );
};

export default WishListPortal;
