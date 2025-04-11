![first-screen.png](exec%2Freadme_assets%2Ffirst-screen.png)
<br />

## 📌 목차
1. [프로젝트 소개](#-프로젝트-소개)
2. [팀 소개](#dddd)
3. [주요 기능](#-주요-기능)
4. [시연 영상](#-시연-영상)
5. [주요 기술](#-주요-기술)
6. [기술 아키텍처](#-기술-아키텍처처)
7. [프로젝트 구조](#-프로젝트-구조)
8. [산출물](#-산출물)
   <br />

## 🚀 프로젝트 소개

***SSAFY 12기 2학기 공통 프로젝트***

> ⌛ 프로젝트 기간 : 2025.02.28 ~ 2025.04.11 (6주)

> 📆 상세 기간 : 기획 2주 + 개발 3주 + 버그 해결 1주

> 🔗 [노션 링크](https://relic-sea-1e3.notion.site/1a412a0174e780b4870bd63cd477cac6)

> 📲 [배포 URL - 모바일](https://j12e106.p.ssafy.io)

> 📝 [발표 자료](https://www.canva.com/design/DAGj4Cf_xWE/_Rcu9Lm_w1HBxPY1ytvvGQ/view?utm_content=DAGj4Cf_xWE&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h840401bd48)


### 
**✨ 로컬 기프티콘 개인화 서비스**<br />
비대면 선물 문화가 일상이 된 시대에, 누구나 손쉽게 원하는 매장의 기프티콘을 생성하고 선물할 수 있는 자체 페이 기반 서비스를 구축하는 것을 목표로 합니다.
본 프로젝트는 기존처럼 기업이 아닌 일반 개인이 직접 기프티콘을 제작하고, 친구나 지인에게 선물할 수 있는 환경을 제공하여 소상공인 매장의 접근성과 선물 문화의 다양성을 동시에 확대합니다.
이를 통해 사용자는 복잡한 절차 없이 간편하게 결제 수단을 선물하고, 가맹점은 별도의 인프라 없이 디지털 고객을 확보할 수 있는 상호 혜택 구조를 실현하고자 합니다.

**😻 NFC 결제**<br />

**💪 이런 사람 사용해보세요!**<br />
1️⃣ 부모님께 효도하고 싶어요!<br />
2️⃣ 생일인 친구에게 특별한 선물하고 싶어요!<br />
3️⃣ 친구들과 맛집을 공유하고 싶어요!<br />
4️⃣ 나만의 메뉴를 만들고 싶어요!<br />


## 👥 팀 소개
<table style="text-align: center;" width="100%">
  <tr>
    <th style="text-align: center;" width="16.66%"><img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/member/cheon.png" width="150" height="150"/></th>
    <th style="text-align: center;" width="16.66%"><img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/member/min.png" width="150" height="150"/></th>
    <th style="text-align: center;" width="16.66%"><img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/member/jeong.png" width="150" height="150"/></th>
    <th style="text-align: center;" width="16.66%"><img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/member/lee.png" width="150" height="150"/></th>
    <th style="text-align: center;" width="16.66%"><img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/member/choi.png" width="150" height="150"/></th>
    <th style="text-align: center;" width="16.66%"><img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/member/sin.png" width="150" height="150"/></th>
  </tr>
  <tr>
    <td style="text-align: center;" width="16.66%">천세윤<br/><a href="https://github.com/yooniverse7">@yooniverse7</a></td>
    <td style="text-align: center;" width="16.66%">민상기<br/><a href="https://github.com/Steadystudy">@Steadystudy</a></td>
    <td style="text-align: center;" width="16.66%">정영한<br/><a href="https://github.com/ynghan">@ynghan</a></td>
    <td style="text-align: center;" width="16.66%">이상혁<br/><a href="https://github.com/leesanghyeok523">@leesanghyeok523</a></td>
    <td style="text-align: center;" width="16.66%">최진문<br/><a href="https://github.com/jinmoon23">@jinmoon23</a></td>
    <td style="text-align: center;" width="16.66%">신주환<br/><a href="https://github.com/yurai770">@yurai770</a></td>
  </tr>
  <tr>
    <td style="text-align: center;" width="16.66%">백엔드 개발 </br> (팀장)</td>
    <td style="text-align: center;" width="16.66%">프론트 개발</td>
    <td style="text-align: center;" width="16.66%">인프라 개발</td>
    <td style="text-align: center;" width="16.66%">백엔드 개발</td>
    <td style="text-align: center;" width="16.66%">프론트 개발</td>
    <td style="text-align: center;" width="16.66%">백엔드 개발</td>
  </tr>
  <tr>
    <td style="text-align: center;" width="16.66%">페이 도메인, 은행 서버 및 금융망 API, 포스 시스템</td>
    <td style="text-align: center;" width="16.66%">지도 및 NFC, QR 결제 담당</td>
    <td style="text-align: center;" width="16.66%">인프라 CI/CD 구축, 기프티콘 API, 지라 관리</td>
    <td style="text-align: center;" width="16.66%">맛집 API, 크롤링, Redis, S3, 소셜로그인, AI 서빙 (Stable diffusion)</td>
    <td style="text-align: center;" width="16.66%">React-Native 관련 통신 및 기프티콘 생성, 마이페이지 담당</td>
    <td style="text-align: center;" width="16.66%">NFC 기능, Spring Security, POS 기기 구현</td>
  </tr>
</table>


## 🚀 주요 기능
<details>
<summary><strong>기프티콘 커스텀</strong></summary>

<table style="text-align: center;" width="100%">
  <tr>
    <th style="text-align: center;" width="25%">기프티콘 커스텀 페이지</th>
    <th style="text-align: center;" width="25%">받은 선물함 페이지</th>
    <th style="text-align: center;" width="25%">보낸 선물함 페이지</th>
    <th style="text-align: center;" width="25%">기프티콘 지도 페이지</th>
  </tr>
  <tr>
    <td style="text-align: center;" width="25%"><img height="400" alt="기프티콘 커스텀 페이지" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/custom/custom-gift.jpg" ></td>
    <td style="text-align: center;" width="25%"><img height="400" alt="받은 선물함 페이지" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/custom/recieved-gift.jpg"></td>
    <td style="text-align: center;" width="25%"><img height="400" alt="보낸 선물함 페이지" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/custom/send-gift.jpg"></td>
    <td style="text-align: center;" width="25%"><img height="400" alt="기프티콘 지도 페이지" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/custom/gifticon-map.jpg"></td>
  </tr>
</table>
</details>

<details>
<summary><strong>결제 기능</strong></summary>

<table style="text-align: center;" width="100%">
  <tr>
    <th style="text-align: center;" width="33%">결제 방식 선택 페이지</th>
    <th style="text-align: center;" width="33%">NFC</th>
    <th style="text-align: center;" width="33%">QR 코드</th>
  </tr>
  <tr>
    <td style="text-align: center;" width="33%"><img height="400" alt="결제 방식 선택 페이지" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/pay/nfc_or_qrcode.jpg"></td>
    <td style="text-align: center;" width="33%"><img height="400" alt="NFC" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/pay/nfc.jpg"></td>
    <td style="text-align: center;" width="33%"><img height="400" alt="QRCODE" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/pay/qrcode.jpg"></td>
 </tr>
</table>

</details>

<details>
<summary><strong>맛집 등록</strong></summary>

<table style="text-align: center;" width="100%">
  <tr>
    <th style="text-align: center;" width="20%">맛집 지도 화면</th>
    <th style="text-align: center;" width="20%">맛집 등록 화면</th>
    <th style="text-align: center;" width="20%">나만의 메뉴</th>
    <th style="text-align: center;" width="20%">또갈집 지도 화면</th>
    <th style="text-align: center;" width="20%">또갈집 확인 화면</th>
  </tr>
  <tr>
    <td style="text-align: center;" width="20%"><img height="400" alt="맛집 지도 화면" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/ddostore_register/dostore-map.jpg" ></td>
    <td style="text-align: center;" width="20%"><img height="400" alt="맛집 등록 화면" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/ddostore_register/dostore-register.jpg"></td>
    <td style="text-align: center;" width="20%"><img height="400" alt="나만의 메뉴" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/ddostore_register/ddostore-add-menu.jpg"></td>
    <td style="text-align: center;" width="20%"><img height="400" alt="또갈집 지도 화면" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/ddostore_register/ddostore-map.jpg"></td>
   <td style="text-align: center;" width="20%"><img height="400" alt="또갈집 확인 화면" src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/ddostore_register/ddostore-check.jpg"></td>
  </tr>
</table>
</details>


## 🔧 주요 기술
![skill.png](exec%2Freadme_assets%2Fskill.png)


## 🗺️ 기술 아키텍처
![ddo-store-architecture](./exec/readme_assets/ddo-store-architecture.png)



## 📂 프로젝트 구조
<details>
  <summary><strong>Back 폴더 구조 보기</strong></summary>
  <pre>
📦 main  
 ┣ 📂 java  
 ┃ ┗ 📂 com  
 ┃   ┗ 📂 example  
 ┃     ┗ 📂 ddo_pay  
 ┃       ┣ 📂 client  
 ┃       ┣ 📂 common  
 ┃       ┃ ┣ 📂 config  
 ┃       ┃ ┃ ┣ 📂 redis  
 ┃       ┃ ┃ ┣ 📂 rest  
 ┃       ┃ ┃ ┣ 📂 S3  
 ┃       ┃ ┃ ┗ 📂 security  
 ┃       ┃ ┃   ┗ 📂 token  
 ┃       ┃ ┣ 📂 dto  
 ┃       ┃ ┣ 📂 exception  
 ┃       ┃ ┣ 📂 response  
 ┃       ┃ ┗ 📂 util  
 ┃       ┣ 📂 gift  
 ┃       ┃ ┣ 📂 controller  
 ┃       ┃ ┣ 📂 dto  
 ┃       ┃ ┃ ┣ 📂 create  
 ┃       ┃ ┃ ┣ 📂 select  
 ┃       ┃ ┃ ┗ 📂 update  
 ┃       ┃ ┣ 📂 entity  
 ┃       ┃ ┣ 📂 repository  
 ┃       ┃ ┗ 📂 service  
 ┃       ┃   ┗ 📂 impl  
 ┃       ┣ 📂 pay  
 ┃       ┃ ┣ 📂 controller  
 ┃       ┃ ┣ 📂 dto  
 ┃       ┃ ┃ ┣ 📂 bank_request  
 ┃       ┃ ┃ ┣ 📂 bank_response  
 ┃       ┃ ┃ ┣ 📂 finance  
 ┃       ┃ ┃ ┣ 📂 request  
 ┃       ┃ ┃ ┗ 📂 response  
 ┃       ┃ ┣ 📂 entity  
 ┃       ┃ ┣ 📂 finance_api  
 ┃       ┃ ┣ 📂 repository  
 ┃       ┃ ┗ 📂 service  
 ┃       ┃   ┗ 📂 impl  
 ┃       ┣ 📂 restaurant  
 ┃       ┃ ┣ 📂 controller  
 ┃       ┃ ┣ 📂 dto  
 ┃       ┃ ┃ ┣ 📂 receipt  
 ┃       ┃ ┃ ┣ 📂 request  
 ┃       ┃ ┃ ┗ 📂 response  
 ┃       ┃ ┣ 📂 entity  
 ┃       ┃ ┣ 📂 mapper  
 ┃       ┃ ┣ 📂 repository  
 ┃       ┃ ┗ 📂 service  
 ┃       ┃   ┣ 📂 crawling  
 ┃       ┃   ┣ 📂 impl  
 ┃       ┃   ┗ 📂 receipt  
 ┃       ┃     ┗ 📂 impl  
 ┃       ┣ 📂 sse  
 ┃       ┗ 📂 user  
 ┃         ┣ 📂 controller  
 ┃         ┣ 📂 dto  
 ┃         ┃ ┣ 📂 request  
 ┃         ┃ ┗ 📂 response  
 ┃         ┣ 📂 entity  
 ┃         ┣ 📂 mapper  
 ┃         ┣ 📂 repo  
 ┃         ┗ 📂 service  
 ┃           ┗ 📂 impl  
 ┗ 📂 resources
   ┗ 📂 application.yml

  </pre>
</details>

<details>
  <summary><strong>Front - mobile 폴더 구조 보기</strong></summary>
  <pre>
📁 FE/mobile/src
├─📁 features
│  └─📁 contactServices
│      ├─📁 api
│      └─📁 types
└─📁 shared
    └─📁 utils
  </pre>
</details>

<details>
  <summary><strong>Front - web 폴더 구조 보기</strong></summary>
  <pre>
📁 FE/web/src
├─📁 app
│  ├─📁 (BarLayout)
│  │  ├─📁 gift
│  │  │  └─📁 get
│  │  │      └─📁 [id]
│  │  └─📁 me
│  │      ├─📁 info
│  │      │  └─📁 setting
│  │      └─📁 stores
│  └─📁 (NoLayout)
│      ├─📁 callback
│      ├─📁 gift
│      │  └─📁 create
│      ├─📁 login
│      ├─📁 moneyCharge
│      ├─📁 pay
│      │  ├─📁 completed
│      │  └─📁 password
│      ├─📁 permission
│      ├─📁 store
│      │  └─📁 register
│      └─📁 user
│          └─📁 firstLogin
├─📁 components
│  └─📁 ui
├─📁 entity
│  ├─📁 gift
│  │  ├─📁 api
│  │  └─📁 model
│  └─📁 store
│      ├─📁 api
│      └─📁 model
├─📁 features
│  ├─📁 crawledStore
│  │  └─📁 ui
│  ├─📁 favoriteStores
│  │  └─📁 ui
│  ├─📁 giftForm
│  │  ├─📁 api
│  │  └─📁 ui
│  ├─📁 gitfBox
│  │  └─📁 ui
│  ├─📁 kakaoLogin
│  │  ├─📁 api
│  │  └─📁 ui
│  ├─📁 map
│  │  ├─📁 model
│  │  └─📁 ui
│  ├─📁 menuForm
│  │  ├─📁 api
│  │  └─📁 ui
│  ├─📁 myMoneyCheck
│  │  ├─📁 api
│  │  └─📁 ui
│  ├─📁 paymentCheck
│  │  ├─📁 api
│  │  └─📁 ui
│  ├─📁 payPwdForm
│  │  ├─📁 api
│  │  └─📁 ui
│  └─📁 permissonRequest
│      └─📁 api
├─📁 lib
├─📁 shared
│  ├─📁 api
│  ├─📁 constants
│  ├─📁 hooks
│  ├─📁 modal
│  ├─📁 msw
│  │  └─📁 mock
│  │      ├─📁 data
│  │      └─📁 handlers
│  ├─📁 reactQuery
│  └─📁 utils
├─📁 store
├─📁 types
└─📁 widgets
    ├─📁 bottomBar
    │  └─📁 ui
    ├─📁 fadeUpContainer
    │  └─📁 ui
    └─📁 searchBar
        └─📁 ui
  </pre>
</details>

## 📜 산출물
<details>
  <summary><strong>기능 명세서</strong></summary>
  <h3>🔹 유저 관리</h3>
  <img src="https://github.com/user-attachments/assets/user-domain.pgn" alt="기능명세서">
  <h3>🔹 기프티콘 관리</h3>
  <img src="https://github.com/user-attachments/assets/gift-domain.png" alt="기능명세서">
  <h3>🔹 자체 페이 관리</h3>
  <img src="https://github.com/user-attachments/assets/pay-domain.png" alt="기능명세서">
  <h3>🔹 맛집 페이 관리</h3>
  <img src="https://github.com/user-attachments/assets/restaurant-domain.png" alt="기능명세서">
  <h3>🔹 은행 관리</h3>
  <img src="https://github.com/user-attachments/assets/bank-domain.png" alt="기능명세서">

</details>

<details>
  <summary><strong>ERD</strong></summary>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/ERD.png" alt="erd">
</details>

<details>
  <summary><strong>피그마</strong></summary>
  <img src="https://github.com/user-attachments/assets/a8e4117e-4700-4747-aa7d-6cff5acc6ff4" alt="피그마">
  <img src="https://github.com/user-attachments/assets/0cc3436c-7408-48ee-a23e-bae14c26666b" alt="피그마">
  <img src="https://github.com/user-attachments/assets/4dac9d7d-1aa1-4f1a-9f5e-f041afc32699" alt="피그마">
  <img src="https://github.com/user-attachments/assets/42619465-62a8-45e4-908f-e1d7c0bddc63" alt="피그마">
</details>

<details>
  <summary><strong>API 명세서</strong></summary>
  <h3>🔹 유저 도메인</h3>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/domain/user-domain.png" alt="api명세서">
  <h3>🔹 기프티콘 도메인</h3>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/domain/gifticon-domain.png" alt="api명세서">
  <h3>🔹 페이 도메인</h3>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/domain/pay-domain.png" alt="api명세서">
  <h3>🔹 맛집 도메인</h3>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/domain/restaurant-domain.png" alt="api명세서">
  <h3>🔹 포스 도메인</h3>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/domain/pos-domain.png" alt="api명세서">
  <h3>🔹 은행 도메인</h3>
  <img src="https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21E106/-/raw/readme/exec/readme_assets/domain/bank-domain.png" alt="api명세서">
</details>
