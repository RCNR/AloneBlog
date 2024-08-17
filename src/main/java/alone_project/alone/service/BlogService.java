package alone_project.alone.service;

import alone_project.alone.domain.Article;
import alone_project.alone.dto.AddArticleRequest;
import alone_project.alone.dto.UpdateArticleRequest;
import alone_project.alone.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // final
public class BlogService {

    private final BlogRepository blogRepository;

    // dto 사용해서 레포지토리에 Article 저장
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.buildEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        Optional<Article> article = blogRepository.findById(id);
        return article.orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
