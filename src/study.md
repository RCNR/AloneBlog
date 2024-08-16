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

