# Blog Search Service

### Description
- Kakao/Naver Blog open API를 이용하여 블로그 검색 서비스를 구현.
- Domain/기능에 따라 module을 분리하였으며 각 서비스끼리는 rest api call을 통해서 작동하도록 설계.

### 기능 요구사항
- 블로그 검색 (search-service)
- 인기 검색어 목록 조회 (statistics-service)

### 사용된 주요 기술 stack
- Spring MVC
- Spring Data Redis
- Embedded Redis
- Spring Data JPA
- H2

### API 명세
각 서비스 resources/http 내 http request file을 생성하여 IDE를 통한 테스트가 용이하도록 구성.
Statistics-Service API 기본 정보

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


