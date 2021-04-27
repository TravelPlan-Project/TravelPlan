import React, { useCallback, useRef, useState } from "react";
import styles from "./loginBody.module.css";
import { onLogin } from "../../api/authAPI";
import { Spin } from "antd";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/userState";
import { useHistory } from "react-router-dom";

const LoginBody = ({ onClickRegister, onClose }) => {
  const formRef = useRef();
  const emailRef = useRef();
  const passwordRef = useRef();
  const [loading, setLoading] = useState(false);
  const [userStates, setUserStates] = useRecoilState(userState);
  const [loginFail, setLoginFail] = useState(false);
  const history = useHistory();

  const updateUserLogin = (isLogin, email, name, loginFail) => {
    setUserStates((prev) => {
      const updated = { ...prev };
      updated["isLogin"] = isLogin;
      updated["email"] = email;
      updated["name"] = name;
      return updated;
    });
  };

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      setLoading(true);
      const email = emailRef.current.value;
      const password = passwordRef.current.value;
      const [isLogin, userEmail, userName] = onLogin(email, password);
      updateUserLogin(isLogin, userEmail, userName);
      setLoading(false);
      if (isLogin) {
        formRef.current.reset();
        onClose();
      } else {
        setLoginFail(true);
      }
    },
    [onClose, updateUserLogin]
  );

  return (
    <form ref={formRef} className={styles.loginForm} onSubmit={handleSubmit}>
      {loginFail && (
        <div className={styles.loginFailBox}>
          <span className={styles.loginFail}>
            죄송합니다. 로그인에 실패했습니다.
          </span>
          <span className={styles.loginFail2}>올바르게 입력해주세요.</span>
        </div>
      )}
      <input
        ref={emailRef}
        type="email"
        className={styles.inputBar}
        placeholder="이메일"
        required
      />
      <input
        ref={passwordRef}
        type="password"
        className={styles.inputBar}
        placeholder="비밀번호"
        required
      />
      <button className={styles.loginBtn}>로그인</button>
      <div className={styles.authManage}>
        <h2 className={styles.lostPassword}>혹시 비밀번호를 잊어버리셨나요?</h2>
        <label className={styles.noAccount}>계정이 없으신가요?</label>
        <span className={styles.loginOrRegister} onClick={onClickRegister}>
          회원가입
        </span>
      </div>
      {loading && (
        <div className={styles.loadingBody}>
          <Spin tip="Logging in..."></Spin>
        </div>
      )}
    </form>
  );
};

export default LoginBody;
