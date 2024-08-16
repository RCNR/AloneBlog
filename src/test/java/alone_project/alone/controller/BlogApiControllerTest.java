package alone_project.alone.controller;

import alone_project.alone.domain.Article;
import alone_project.alone.dto.AddArticleRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
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

        // then = 응답코드 200, 반환받은 값 중 0번째 요소의 content 및 title이 저장된 값과 같은지
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].content").value(content_2))
                .andExpect(jsonPath("$[1].title").value(title_2));
    }
}