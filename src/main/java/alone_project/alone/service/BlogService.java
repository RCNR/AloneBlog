package alone_project.alone.service;

import alone_project.alone.domain.Article;
import alone_project.alone.dto.AddArticleRequest;
import alone_project.alone.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final
public class BlogService {

    private final BlogRepository blogRepository;

    // dto 사용해서 레포지토리에 Article 저장
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.buildEntity());
    }
}
