import './balance.css'; // 상단 영역 스타일
import AccountList from '../component/account-list'; // 아래 리스트 컴포넌트

function AccountPage() {
  return (
    <div className="account-container">
      {/* 상단 파란색 영역 */}
      <div className="account-header">
        <div className="balance-amount">10,000,000원</div>
        <div className="balance-number">9994761830318557</div>
      </div>

      {/* 하단 리스트 컴포넌트 */}
      <AccountList />
    </div>
  );
}

export default AccountPage;
