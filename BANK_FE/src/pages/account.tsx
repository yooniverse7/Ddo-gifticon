import React, { useState } from 'react';
import axios from 'axios';
import './login.css';
import ssafyLogo from '../assets/image/ssafy.png';
import { useNavigate } from 'react-router-dom';

function CreateAccountPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");

  const handleCreateAccount = async () => {
    try {
      const response = await axios.post("https://j12e106.p.ssafy.io/bank/create-account", {
        email: email,
      });
      console.log("계좌 개설 응답:", response.data);
      // API 응답 코드가 "200"인 경우 성공으로 간주
      if (response.data && response.data.code === "200") {
        alert("계좌 생성이 완료되었습니다.");
        navigate('/');
      } else {
        alert("계좌 생성에 실패했습니다: " + response.data.message);
      }
    } catch (error) {
      console.error("계좌 개설 요청 실패:", error);
      alert("계좌 개설 요청 중 에러가 발생했습니다.");
    }
  };

  return (
    <div className="container">
      {/* 로고 영역 */}
      <div className="logo-wrapper">
        <img src={ssafyLogo} alt="SSAFY Logo" className="logo-image" />
      </div>
      
      {/* 제목 */}
      <h2 className="title">계좌 개설</h2>
      
      {/* 이메일 입력창 */}
      <input
        type="email"
        className="input-field"
        placeholder="이메일을 입력하세요"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      
      {/* 계좌 개설 버튼 */}
      <button className="btn" onClick={handleCreateAccount}>
        계좌개설
      </button>
    </div>
  );
}

export default CreateAccountPage;
