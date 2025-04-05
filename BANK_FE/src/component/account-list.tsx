import './account-list.css'; // 리스트 전용 스타일

function AccountList() {
  return (
    <div className="account-list-container">
      {/* 예시로 '총 13건' 표시 */}
      <div className="account-summary">
        총 13건
      </div>

      {/* 개별 항목 예시 */}
      <div className="account-item">
        <div className="item-date">3.2 22:04</div>
        <div className="item-desc">입금(랜덤단어)</div>
        <div className="item-amount plus">+1원</div>
      </div>

      <div className="account-item">
        <div className="item-date">3.2 22:04</div>
        <div className="item-desc">출금</div>
        <div className="item-amount minus">-13,000원</div>
      </div>

      {/* 필요 시 반복 렌더링 (map) */}
      {/* ... */}
    </div>
  );
}

export default AccountList;
