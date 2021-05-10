package com.ddd.moodof.domain.model.category;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    private Long previousId;

    private String title;

    private Long userId;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


    public Category updatePreviousId(Long previousId, Long targetId){
        verifySurveyId(targetId);
        verifyPreviousIdsAreSame(previousId);
        this.previousId = previousId;
        return this;
    }

    private void verifySurveyId(Long targetId) {
        if (!this.targetId.equals(targetId)) {
            throw new IllegalArgumentException(
                    "수정할 targetId 일치하지 않습니다, 수정할 targetId: " + this.targetId + "입력 받은 targetId : " + targetId);
        }
    }

    private void verifyPreviousIdsAreSame(Long previousId) {
        if (this.previousId.equals(previousId)) {
            throw new IllegalArgumentException("동일한 previousId 변경할 수 없습니다.");
        }
    }

}
