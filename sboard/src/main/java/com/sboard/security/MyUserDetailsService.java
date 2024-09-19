package com.sboard.security;

import com.sboard.entity.User;
import com.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    //인증처리 함 // 비밀번호는 적용하지 않는 이유는 비밀번호가 틀려도 상관없이 일단 user의 유뮤를 판단
    // => 이것을 통과하면 이 entity를 Authenticationprovider로 entity를 보내서 비밀번호 일치유무가 검사됨
    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findById(uid);
        if(optUser.isPresent()){
            //시큐리티 사용자 인증객체 생성 후 리턴
            MyUserDetails myUserDetails = MyUserDetails.builder()
                                                .user(optUser.get())
                                                .build();

            return myUserDetails;
        }

        //사용자가 입력한 아이디가 없는경
        return null;
    }
}
