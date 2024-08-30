package alone_project.alone.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;


    /**
     * 엔티티 생성과 수정 시간을 위한 새로운 필드 생성
     */
    @CreatedDate // 엔티티 생성될 떄 시간 저장
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @LastModifiedDate // 엔티티 수정될 때의 시간 저장
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    
    @Builder // 빌더 패턴으로 객체 생성
    public Article(String title, String content) { // id는 자동
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
