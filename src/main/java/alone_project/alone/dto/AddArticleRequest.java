package alone_project.alone.dto;

import alone_project.alone.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드값 파라미터로 받는 생성자
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    public Article buildEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }

}
