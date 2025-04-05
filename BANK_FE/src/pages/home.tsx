import ReactDOM from 'react-dom/client';
import './home.css';
import { useNavigate } from 'react-router-dom';
import ssafyLogo from '../assets/image/ssafy.png';

function App() {
const navigate = useNavigate();
  return (
    <div className="container">
      {/* 로고 영역 */}
      <div className="logo-wrapper">
        <img src={ssafyLogo} alt="SSAFY Logo" className="logo-image" />
        <h2 className='logo-name'>BANK</h2>
      </div>

      {/* 버튼 영역 */}
      <div className="button-container">
        <button className="btn" onClick={() => navigate('/login')}>로그인</button>
        <button className="btn" onClick={() => navigate('/account')}>계좌개설</button>
      </div>
    </div>
  );
}

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(<App />);

export default App;
