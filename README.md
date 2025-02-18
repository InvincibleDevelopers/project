<h1 style="display:flex; align-items:center;">북파고 &nbsp;<sub>- 독서모임 SNS 프로젝트 -</sub></h1>

<br/>

### 프로젝트 소개

북파고는 회원들이 모여 독서모임을 만들고, 책에 대한 감상을 나누며 성향이 맞는 회원들과 교류할 수 있도록 연결 해주는 앱 프로젝트 입니다.

<br/>

<div align="center">
  <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki">🌐 Wiki</a>
</div>

<br/>


### 주요기술

```
typescript
react-native v0.75
react-query v5.56
react-native-navigation v6.1
react-native-dotenv

react-native-async-storage
react-native-encrypted-storage
react-native-seoul/kakao-login
axios
dayjs
tiptap-editor
react-daum-postcode
react-native-image-picker
```

<br/>

### 주요화면

<img src="https://github.com/InvincibleDevelopers/bookpago-app/blob/main/docs/%EC%A3%BC%EC%9A%94%ED%99%94%EB%A9%B4.png?raw=true"/>

<br/>

<a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%EC%9D%B8%EC%A6%9D">로그인 인증절차</a>

<br/>

<details open>
  <summary>1. 홈 스크린</summary>
  <desc>
    1. 홈화면에서는 인기도서 리스트(알라딘 API),
    <br />
    2. 사용자 맞춤 추천 도서(Chat GPT API), 
    <br />
    3. 참여자가 많은 독서모임
    <br />
    위 가지 정보를 한눈에 볼 수 있도록 구성하였습니다.
  </desc>
  <br/>
  <br/>
  <div>
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki">Workflow - Home</a>
  </div>
</details>

<br/>

<details open>
  <summary>2. 책 검색 스크린</summary>
  <desc>
    네이버 책검색 API를 사용한 책 검색 기능을 구현하였습니다.<br />
    책마다 가지고 있는 isbn값을 전달하여 책 상세 페이지로 이동합니다.
  </desc>
  <br/>
  <br/>
  <div>
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%EC%B1%85-%EA%B2%80%EC%83%89">Workflow - Search</a>
  </div>
  </div>
</details>

<br/>

<details open>
  <summary>3. 독서모임 스크린</summary>
  <desc>
    독서모임을 생성하고, 참여할 수 있는 화면입니다.<br />
    최신순과 거리순으로 정렬할 수 있습니다.<br />
    태그별 검색기능은 미구현 되었습니다.
  </desc>
  <br/>
  <br/>
  <div>
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%EB%8F%85%EC%84%9C%EB%AA%A8%EC%9E%84-%EC%9E%91%EC%84%B1">Workflow - Edit</a>
  </div>
</details>

<br/>

<details open>
  <summary>4. 프로필 스크린</summary>
  <desc>
    나와 상대방의 프로필 사진과 자기소개와 같은 프로필 정보를 확인할 수 있는 화면입니다.<br />
    즐겨찾기한 책리스트, 팔로우 팔로워 기능과 조회, 참여중인 독서모임에 대해 한눈에 볼 수 있게 구성되었습니다.
    또한 설정페이지로 이동할 수 있는 버튼이 위치해 있습니다.
  </desc>
  <br/>
  <br/>
  <div>
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%ED%94%84%EB%A1%9C%ED%95%84-%EC%88%98%EC%A0%95">Workflow - Edit</a>
    <br />
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%ED%8C%94%EB%A1%9C%EC%9A%B0">Workflow - Follow</a>
  </div>
</details>

<br/>

<details open>
  <summary>5. 책 상세 스크린</summary>
  <desc>
    isbn값을 유추할 수 있는 홈, 검색, 프로필 화면의 책 카드 UI를 통해 접근 할 수 있습니다.
    책 정보를 조회하고, 즐겨찾기, 리뷰작성과 조회를 할 수 있습니다.
  </desc>
  <br/>
  <br/>
  <div>
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%EC%A6%90%EA%B2%A8%EC%B0%BE%EA%B8%B0">Workflow - Favorites</a>
    <br />
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%EC%B1%85-%ED%8F%89%EC%A0%90-%EC%9E%91%EC%84%B1">Workflow - Rating</a>
  </div>
</details>

<br/>

<details open>
  <summary>6. 독서모임 상세 스크린</summary>
  <desc>
    tiptap에디터로 작성된 독서모임 정보와, 일자 주기를 알 수 있습니다.<br />
    독서모임을 모임장과 참여자로 역할이 나뉘어 있으며,<br />
    참여자가 신청을 하고 모임장이 수락 또는 거절하는 흐름으로 진행됩니다.
  </desc>
  <br/>
  <br/>
  <div>
    <a href="https://github.com/InvincibleDevelopers/bookpago-app/wiki/%EB%8F%85%EC%84%9C%EB%AA%A8%EC%9E%84-%EC%B0%B8%EA%B0%80%EC%8B%A0%EC%B2%AD-%EB%B0%8F-%EC%88%98%EB%9D%BD">Workflow - Join</a>
  </div>
</details>
