package com.sboard.service;

import com.sboard.dto.VerificationCodeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final Map<String, VerificationCode> codeStore = new ConcurrentHashMap<>();
    private final JavaMailSender mailSender;



    @Value("${spring.mail.username")
    private String serviceEmail;
    private final Integer EXPIRATION_TIME_IN_MINUTES = 5;

    public void sendVerificationCode(VerificationCodeDTO requestDTO) {
        String code= UUID.randomUUID().toString();
        VerificationCode verificationCode = new VerificationCode(code,requestDTO.getEmail(), LocalDateTime.now().plusMinutes(5) );

        codeStore.put(code,verificationCode);


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(requestDTO.getEmail());
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + code);
        mailSender.send(message);

    }

    public boolean verifyCode(String code) {
        VerificationCode verificationCode = codeStore.get(code);
        if (verificationCode != null && verificationCode.getExpiration().isAfter(LocalDateTime.now())) {
            codeStore.remove(code);  // Remove after successful verification
            return true;
        }
        return false;
    }





    private static class VerificationCode {
        private final String code;
        private final String email;
        private final LocalDateTime expiration;

        public VerificationCode(String code, String email, LocalDateTime expiration) {
            this.code = code;
            this.email = email;
            this.expiration = expiration;
        }

        public String getCode() {
            return code;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getExpiration() {
            return expiration;
        }


    }



}
