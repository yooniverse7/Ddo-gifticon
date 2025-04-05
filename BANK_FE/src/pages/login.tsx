import React, { useState } from 'react';
import axios from 'axios';
import './login.css';
import ssafyLogo from '../assets/image/ssafy.png';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");

  const handleLogin = async () => {
    try {
      const response = await axios.post("https://j12e106.p.ssafy.io/bank/login", {
        email: email,
      });

      if (response.data.code === "200") {
        const { userId } = response.data.data;
        console.log("로그인 응답:", response.data);
        localStorage.setItem("userId", userId);
        // 이후 필요한 로직 실행 (예: 페이지 이동 등)
        navigate('/balance');
      }

    } catch (error) {
      console.error("로그인 요청 실패:", error);
      // 실패 시 에러 메시지 처리
    }
  };

  return (
    <div className="container">
      {/* 로고 영역 */}
      <div className="logo-wrapper">
        <img src={ssafyLogo} alt="SSAFY Logo" className="logo-image" />
      </div>
      
      {/* 제목 */}
      <h2 className="title">로그인</h2>
      
      {/* 입력창 */}
      <input
        type="email"
        className="input-field"
        placeholder="이메일을 입력하세요"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      
      {/* 버튼 */}
      <button className="btn" onClick={handleLogin}>
        로그인
      </button>
    </div>
  );
}

export default LoginPage;
