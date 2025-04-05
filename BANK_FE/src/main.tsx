import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/home.tsx';     // 메인 페이지
import Login from './pages/login.tsx'; // 로그인 페이지
import Balance from './pages/balanc.tsx'; // 계좌 개설 페이지
import Account from './pages/account.tsx'

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/balance" element={<Balance />} />
      <Route path="/account" element={<Account />} />
    </Routes>
  </BrowserRouter>
);
