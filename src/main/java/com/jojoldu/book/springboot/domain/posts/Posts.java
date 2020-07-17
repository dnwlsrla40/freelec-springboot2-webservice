package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor  // 기본 생성자 자동 추가
@Entity // Entity 클래스는 절대 Setter 메서드를 만들 지 않고, 필드 값 변경 필요 시 목적과 의도를 나타낼 수 있는 메서드를 추가 -> 인스턴스 값 변경이 가시적
    // Setter가 없으므로, 생성자 통해 최종값을 채운 후 DB에 삽입 -> 변경 필요 시 이벤트에 맞는 public 메서드 호출
public class Posts extends BaseTimeEntity {

    @Id // PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성 규칙 GenerationType.IDENTITY->auto increment
    private Long id;

    @Column(length = 500, nullable = false) // 기본 값 외에 변경 필요한 옵션 존재 시 사용
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder    // 해당 클래스의 빌더 패턴 클래스 생성, 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함, 생성자 역할, 차이점 = 어느 필드에 어떤 값을 채워야 할 지 명시
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
