import './login.css';          
import ssafyLogo from '../assets/image/ssafy.png'; // 로고 이미지 경로 확인
import { useNavigate } from 'react-router-dom';

function LoginPage() {
  const navigate = useNavigate();
  return (
    <div className="container">
      {/* 로고 영역 */}
      <div className="logo-wrapper">
        <img src={ssafyLogo} alt="SSAFY Logo" className="logo-image" />
      </div>
      
      {/* 제목 */}
      <h2 className="title">계좌 개설</h2>
      
      {/* 입력창 */}
      <input
        type="email"
        className="input-field"
        placeholder="이메일을 입력하세요"
      />
      
      {/* 버튼 */}
      <button className="btn" onClick={() => navigate('/')}>계좌개설</button>
    </div>
  );
}

export default LoginPage;
