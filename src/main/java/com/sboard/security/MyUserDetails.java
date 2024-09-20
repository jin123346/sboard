package com.sboard.security;

import com.sboard.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class MyUserDetails implements UserDetails {

    // User entity 선언
    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //계정이 갖는 권한 목록을 생성하는 메서드
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole())); //


        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPass();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    //계정 만료 여부 휴먼계정으로 처리하는 방법
    @Override
    public boolean isAccountNonExpired() {
        return true;
        // 계정만료 여부 : true - 만료 안됨 , false - 만료됨
    }

    //계정 잠금(관리자) - 나쁜놈 퇴치용
    @Override
    public boolean isAccountNonLocked() {
        //계정 잠김 여부( true:잠김 아님, false:잠김)
        return true;
    }

    //계정 비밀번호 만료여부
    @Override
    public boolean isCredentialsNonExpired() {
        //비밀번호 유효기간(true : 유효기간 없음 , false : 만료)

        return true;
    }


    //계정 활성화여부
    @Override
    public boolean isEnabled() {
        //계정 탈퇴 또는 계정 비활성화시 사용 (true 활성,false : 비활성)
        return true;
    }

}
