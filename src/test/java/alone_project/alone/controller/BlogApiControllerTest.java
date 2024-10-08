package alone_project.alone.controller;

import alone_project.alone.domain.Article;
import alone_project.alone.dto.AddArticleRequest;
import alone_project.alone.dto.UpdateArticleRequest;
import alone_project.alone.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화 및 역직렬화

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // test 시작 전 다 삭제하기
    public void mockMvcSet() { // MockMvc 설정
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }


    @DisplayName("블로그 글 추가 : addArticle")
    @Test
    public void addArticle() throws Exception {

        // given
        final String url = "/api/articles";
        final String title = "title입니다.";
        final String content = "content입니다.";
        final AddArticleRequest myRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(myRequest); // 직렬화(JSON으로)

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title); // 첫번째 제목이 title과 같은지 확인
        assertThat(articles.get(0).getContent()).isEqualTo(content);
        assertThat(articles.get(0).getTitle()).contains("title");
    }

    @DisplayName("블로그 글 목록 조회 : findAllArticles")
    @Test
    public void findAllArticles() throws Exception {

        // given - 블로그 글 저장
        final String url = "/api/articles";

        final String title_1 = "title_1입니다";
        final String content_1 = "content_1입니다";
        final String title_2 = "title_2입니다";
        final String content_2 = "content_2입니다";

        blogRepository.save(Article.builder()
                .title(title_1)
                .content(content_1)
                .build());
        blogRepository.save(Article.builder()
                .title(title_2)
                .content(content_2)
                .build());

        // when - 리스트 조회 API 호출
        final ResultActions result = mockMvc.perform(get(url).
                accept(MediaType.APPLICATION_JSON));

        // then = 응답코드 200, 반환받은 값 중 1번째 요소의 content 및 title이 저장된 값과 같은지
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].content").value(content_2))
                .andExpect(jsonPath("$[1].title").value(title_2));
    }

    @DisplayName("블로글 조회 성공 : findArticle")
    @Test
    public void findArticle() throws Exception {

        // given
        final String url = "/api/articles/{id}";
        final String title = "title입니다";
        final String content = "content입니다";

        Article article = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        ResultActions resultActions = mockMvc.perform(get(url, article.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("블로그 글 삭제 : deleteArticle")
    @Test
    public void deleteArticle() throws Exception {

        // given
        final String url = "/api/articles/{id}";
        final String title = "title입니다";
        final String content = "content입니다";

        Article article = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        mockMvc.perform(delete(url, article.getId())).andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("블로그 글 수정 : updateArticle")
    @Test
    public void updateArticle() throws Exception{

        // given
        final String url = "/api/articles/{id}";
        final String title = "title입니다";
        final String content = "content입니다";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // 새로운 title, content 생성
        final String newTitle = "newTitle입니다.";
        final String newContent = "newContent입니다.";

        final UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}