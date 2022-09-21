# Blog Search Service

### Description
- Kakao/Naver Blog open API를 이용하여 블로그 검색 서비스를 구현.
- Domain/기능에 따라 module을 분리하였으며 각 서비스끼리는 rest api call을 통해서 작동하도록 설계.

### 기능 요구사항
- 블로그 검색 (search-service)
- 인기 검색어 목록 조회 (statistics-service)
- 멀티 모듈 구성 및 모듈간 의존성 제약
  - 향후 scale out등 확장성을 고려하여 독립적으로 구동할 수 있게 설계/개발을 진행하였습니다.
- 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
- 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현
  - 트래픽/데이터가 많을 경우, bottleneck이 가장 심할거라고 생각하는 부분인 ranking 구현을 redis를 이용하였습니다.
  - 테스트의 용이성을 위해서 embedded redis를 사용했습니다.
- 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공

### 사용된 주요 기술 stack
- Spring MVC
- Spring Data Redis / Embedded Redis (추가)
- Spring Data JPA
- H2

### API 명세
각 서비스 resources/http 내 http request file을 생성하여 IDE를 통한 테스트가 용이하도록 구성.
Search-Service API 기본 정보 (localhost:8080)

|METHOD| URI                                                                               | 설명 |
|------|-----------------------------------------------------------------------------------|----|
|GET| /v1/search?keyword={검색어}&page={page}&page-size={page-size}&sort={ACCURACY,LATEST} | 블로그 검색 |

0. 공통 API format
기본적으로 두 서비스의 response의 body 구조는 아래와 같습니다.
```
{
    "code" : Number,
    "message": String,  // Optional, 정상 응답이 아닌 경우에 존재
    "responses": Object // Optional, 정상 응답인 경우에 존재
}
```

1. 블로그 검색 조회
- Request Example
```
http://localhost:8080/v1/search?keyword=카카오&page=1&page-size=10&sort=ACCURACY
```
- Response Example
```
{
  "code": 200,
  "responses": [
    {
      "title": "<b>카카오</b> - 나무위키",
      "content": null,
      "url": "https://namu.wiki/w/%EC%B9%B4%EC%B9%B4%EC%98%A4"
    },
    {
      "title": "<b>카카오</b>엔터테인먼트 - 나무위키",
      "content": null,
      "url": "https://namu.wiki/w/%EC%B9%B4%EC%B9%B4%EC%98%A4%EC%97%94%ED%84%B0%ED%85%8C%EC%9D%B8%EB%A8%BC%ED%8A%B8"
    },
    {
      "title": "<b>카카오</b> 로그인",
      "content": null,
      "url": "https://developers.kakao.com/docs/latest/ko/kakaologin/prerequisite"
    },
    {
      "title": "<b>카카오</b> 로그인",
      "content": null,
      "url": "https://developers.kakao.com/docs/latest/ko/kakaologin/trouble-shooting"
    },
    {
      "title": "<b>카카오</b>게임즈, 우마무스메 간담회서 개선안 밝혀… 이용자와 해결점 찾아야 할 것 : 게임샷",
      "content": null,
      "url": "http://www.gameshot.net/common/con_view.php?code=GA6326ed4d5bb41"
    },
    {
      "title": "<b>카카오</b>, 모바일 다음(Daum) 뉴스 개편 : 게임샷",
      "content": null,
      "url": "http://www.gameshot.net/common/con_view.php?code=GA63071bada3cfa"
    },
    {
      "title": "[매거진] [<b>카카오</b>같이가치] 세상에서 가장 쉽게 기부하는 방법 - <b>카카오</b>같이가치",
      "content": null,
      "url": "https://together.kakao.com/magazines/1469"
    },
    {
      "title": "<b>카카오</b>싱크를 통한 회원가입을 할 때 <b>카카오</b>디벨로퍼에서 설정한 동의항목들이 뜨지 않습니다",
      "content": null,
      "url": "https://devtalk.kakao.com/t/topic/124597"
    },
    {
      "title": "도움말 | <b>카카오</b>페이지",
      "content": null,
      "url": "https://page.kakao.com/help?helpId=1462"
    },
    {
      "title": "㈜<b>카카오</b>게임즈, ‘<b>카카오</b> 배틀그라운드’ 랭커 챔피언십(KPRC) 9월 3일(토) 개최",
      "content": null,
      "url": "https://bbs.ruliweb.com/news/read/171138"
    }
  ]
}
```

Statistics-Service API 기본 정보 (localhost:8081)

|METHOD|URI|설명|
|------|---|---|
|GET|/v1/statistics/blog-search/top10|블로그 검색 상위 top 10 조회|
|PATCH|/v1/statistics/blog-search/{search-keyword}|검색 기록 count 증가|

1. 블로그 검색 상위 top 10 조회

- Request Example
```
GET http://localhost:8081/v1/statistics/blog-search/top10
```
- Response Example
```
{
  "keywords": [
    {
      "keyword": "블로그 검색",
      "count": 3
    },
    {
      "keyword": "카카오",
      "count": 2
    },
    {
      "keyword": "자바",
      "count": 2
    },
    {
      "keyword": "스프링",
      "count": 2
    },
    {
      "keyword": "네이버",
      "count": 2
    },
    {
      "keyword": "redis",
      "count": 2
    },
    {
      "keyword": "h2",
      "count": 2
    },
    {
      "keyword": "a",
      "count": 1
    }
  ]
}
```

2. 검색 기록 count 증가

- Request Example
```
PUT http://localhost:8081/v1/statistics/blog-search/카카오
```
- Response Example
```
Response code: 200; Time: 1581ms; Content length: 0 bytes
```

### How to run
각 Service 아래 jar파일을 같이 저장해두었습니다. 두 서비스의 jar file을 아래와 같이 기동한 후 테스트하실 수 있습니다.  
API 테스트의 경우, Intellij를 사용하시고 있으시다면 각 프로젝트 내 http 파일을 이용해서 API를 편하게 호출하실 수 있습니다.

search-service의 경우, statistics-service가 올라가 있어야 정상적으로 모든 기능이 작동하게 됩니다.
```
java -jar statistics-service-0.0.1-SNAPSHOT.jar
java -jar search-service-0.0.1-SNAPSHOT.jar
```


