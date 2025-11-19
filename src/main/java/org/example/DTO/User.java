package org.example.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String id;                // 사용자 ID (Primary Key)
    private String passwordHash;      // 암호화된 비밀번호
    private boolean licenseVerified;  // 면허 인증 여부
    private String createdAt;         // 가입 시각
}
