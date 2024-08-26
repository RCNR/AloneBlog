- Article 클래스
  - @NoArgsConstructor(access=AccessLevel.PROTECTED)를 사용하여 기본 생성자 구성
    - PROTECTED -> Article 클래스를 무분별하게 만드는 경우를 만들지 않기 위해
  - @Builder 빌더 패턴으로 객체 생성
    - 어느 필드에 어느 값이 매칭되는지 파악 가능 -> 가독성 높여줌

- BlogRepository 인터페이스 : JpaRepository를 상속받음으로써 여기서 제공하는 여러 메서도 사용 가능
  - 만약 상속받지 않는다면 메서드 하나씩 다 구성해야함
  - 기본 CRUD 기능부터, 쿼리 메서드 등
  - JpaRepository 덕분에 Sprig Data JPA가 내부적으로 객체를 DB에 반영 -> 비즈니스 로직에 집중 가능

- AddArticleRequest 클래스 : dto 역할
  - 오로지 데이터 옮기기 위해 사용, 전달자 => 비즈니스 로직 X
  - @AllArgsConstructor 를 사용해 모든 필드 값을 파라미터로 받는 생성자 추가 되게
  - buildEntity() 는 블로그 글 추가할 때 저장할 엔티티로 변환하는 용도

- BlogService 클래스 : 요청에 따라 CRUD 등의 요청 처리
  - @Service 가 있는 해당 클래스는 빈으로 서블릿 컨테이너에 등록됨
  - findById() 메서드 : JPA의 findById()가 사용됨. 이는 Optional 형태라서 단순히 article을 리턴하면 안됨

  - update() 메서드에서 @Transactional을 사용한 이유 :
    - 트랜잭션 : DB에서 데이터 바꾸기 위한 작업 단위
    - DB 작업이 하나의 트랜잭션으로 처리되는데, 메서드 실행 중 오류 발생하면 **롤백**이 되어 데이터 유지 가능
    - 트랜잭션 안에서 처리가 성공적으로 완료되면 커밋이 되게 -> 일종의 경계 / 이게 없어서 db 작업이 실패하게 되면 데이터 부분적 업데이트 상황 발생 가능함
    - 트랜잭션 시작, 종료 관리 // 오류 발생 -> 롤백
  
- BlogApiController
  - @RestController : HTTP 응답 바디에 있는 자바 객체 데이터를 JSON 형태로 바꿔줄 수 있는
  - ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request)
    - @RequestBody : HTTP 응답 값을 AddArticleRequest에 매핑함 
  - 반환값으로 ResponseEntity<> 가 되면서 요청 값이 생성되었고 save된 article 정보를 응답 객체에 담아 전송됨
    - 201 -> Created
  
  - @GetMapping("/api/articles") 에 관하여
    - 블로그 리스트 전체 조회 -> 목록 조회 및 응답 역할의 dto 사용(ArticleResponse)
    - 응답용 객체 : ArticleResponse // 이를 body에 담아 클라이언트에 전송
    - **stream** : 여러 데이터가 모여 있는 컬렉션을 간편하게 처리해줌
  
  - findArticle() 메서드의 @PathVariable
    - URL에서 값을 가져오는 에너테이션. 여기 코드에서 {id} 값을 가져온다

  - updateArticle() 메서드에서의 @PutMapping()
    - PUT(update) 요청이 오면 그 요청의 Request body 정보가 reqeust로 넘어옴 -> 이후 서비스이 update() 실행
  
- BlogApiControllerTest 클래스
  - @AutoConfigureMockMvc : MockMvc 생성하고 이를 자동으로 구성
    - MockMvc : 애플리케이션 서버에 배포하지 않고도 테스트용 MVC 환경을 만들 수 있는 -> 컨트롤러 테스트 용
  - objectMapper : 자바 객체 데이터를 JSON으로 (직렬화 및 역직렬화)
  - WebApplicationContext : 일반 ApplicationContext 보다 확장 된 것
    - 웹 관련 : 서블릿 컨텍스트, 서블릿 매핑, 웹 관련 속성들

  - mockMvc.perform() : HTTP 요청을 컨트롤러에 전달 -> 결과(리턴): ResultActions 객체
    - 설정 내용을 바탕으로 요청 전송
    - RequestBody는 JSON 형태의 String임
    - ResultActions 객체로 응답의 상태 코드, 바디, 헤더 등을 알 수 있음

  - jsonPath() : JSON 구조에서 특정 요소 선택 또는 필터링 위해 사용하는 경로 표현식 -> mvc에서 JSON 응답 검증할 때
    - '$[]' : '$'는 json 경로를 의미 path 표현식은 $로 시작, [] : 인덱스


- ArticleListViewResponse (DTO 객체)
  - 앞까지는 API를 위한 컨트롤러
  - 이제 사용자에게 View를 보여줘야함
  - View에게 데이터 전달하기 위한 객체임

- BlogViewController
  - getArticles() 메서드
    - blogService에서 articles 찾음(findAll), 이를 Stream<Article> 형태로 변환(더 기능적임)
      - Article에 대해 ArticleListViewResponse 매핑하게 됨 (Stream<ArticleListviewResponse>)
        -> 이를 다시 List 형태로 변환


- Article 클래스에서 EntityListeners() 어노테이션 추가 부분
  - Article 엔티티의 생성 및 수정 시간 추가 -> 블로그 글 언제 생성 되었는지 뷰에서 확인할 수 있게끔
  - 이로 인해 createdTime, updatedTime 필드 생성 -> 각 @CreatedDate, @LastModifiedDate 사용 -> 이에 맞는 column까지
  - @EntityListeners(AuditingEntityListener.class)은 생성 및 수정 시간을 자동으로 감시 및 기록

- ArticleList.html
  - <a th:href="@{/articles/{id}(id=${item.id})}"
    class="btn btn-primary">자세히 보기</a>
    - href을 타임리프의 th:href로 변경 및 @{}의 URL 표현식을 사용해 '자세히 보기' 클릭시 주소를 articles/{item.id}로 변경
      - 이는 글의 상세화면으로 이동

- Article.html
  - th:text="|Posted on
    ${#temporals.format(article.createdTime, 'yyyy-MM-dd HH:mm')}|">게시한 날짜 정보 표시란
    - ${#temporals.format(article.createdTime, 'yyyy-MM-dd HH:mm')}|"
      => yyyy~~mm 으로 포매팅
    - | |  => 리터럴(literals).. 타임리프에서 리터럴은 작은따옴표로 감싸야함
      - 이때 리터럴(Posted on)과 변수를 같이 사용하면 | |에 담아 편리하게 사용함
      - "'abc ' + ${data}" => 이런 식으로 + 를 사용해 할 수도 있지만 위의 방법보단 불편함