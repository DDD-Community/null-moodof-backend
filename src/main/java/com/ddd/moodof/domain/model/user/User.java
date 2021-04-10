package com.ddd.moodof.domain.model.user;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String nickname;
    private String profileUrl;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
