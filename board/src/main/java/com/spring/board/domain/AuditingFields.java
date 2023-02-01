package com.spring.board.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class) // Spring Data JPA Auditing 활성화
@MappedSuperclass   // @Entity 클래스들이 이 클래스를 상속할 경우 필드들도 컬럼으로 인식 -> @Embedded 타입으로도 사용할 수 있다.
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // LocalDateTime 타입을 JSON으로 변환할 때 ISO 8601 포맷으로 변환
    @CreatedDate
    @Column(nullable = false, updatable = false)    // 생성일시는 수정 불가
    private LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy;            // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;           // 수정
}
