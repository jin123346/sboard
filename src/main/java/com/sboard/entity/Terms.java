package com.sboard.entity;

import com.sboard.dto.TermsDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="terms")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String terms;
    private String privacy;


    public TermsDTO toDTO(){
        return TermsDTO.builder()
                .terms(terms)
                .privacy(privacy)
                .build();
    }
}
