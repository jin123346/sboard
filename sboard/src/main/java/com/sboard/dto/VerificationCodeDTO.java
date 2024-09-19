package com.sboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VerificationCodeDTO {



    @Email
    @NotBlank
    private String email;



//    private String code;
//    private LocalDateTime createAt;
//    private Integer expirationTimeInMinutes;
//
//    public boolean isExpired(LocalDateTime verifiedAt) {
//        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(expirationTimeInMinutes);
//        return verifiedAt.isBefore(expiredAt);
//    }
//
//    public String generateCodeMessage(){
//        String formattedExpiredAt = createAt
//                .plusMinutes(expirationTimeInMinutes)
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//
//        return String.format(
//                """
//                    [verification code]
//                    %s
//                    Expired At:%s
//                """,code,formattedExpiredAt
//        );
//    }
}
