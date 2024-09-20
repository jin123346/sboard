package com.sboard.service;

import com.sboard.dto.TermsDTO;
import com.sboard.dto.UserDTO;
import com.sboard.entity.Terms;
import com.sboard.entity.User;
import com.sboard.repository.Termsrepository;
import com.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.channels.NonWritableChannelException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {



    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Termsrepository termsRepository;




    public String loginUser(String uid, String password) {
       String endcodedpassword = passwordEncoder.encode(password);

       Optional<User> opt= userRepository.findByUidAndPass(uid,endcodedpassword);

       if(opt.isPresent()) {
           User user = opt.get();
           return user.getUid();
       }

       return null;

    }

    public UserDTO insertUser(UserDTO userDTO) {
      String encodepass =  passwordEncoder.encode(userDTO.getPass());
      userDTO.setPass(encodepass);

      User user =  userDTO.toEntity();

      User saveUser = userRepository.save(user);

      return saveUser.toDTO();
    }
    public UserDTO selectUserByType(String type,String value){
        if(type.equals("uid")){
            Optional<User> user = userRepository.findById(value);
            if(user.isPresent()){
                UserDTO userdto = user.get().toDTO();
                return userdto;
            }
        }else if(type.equals("nick")){
            Optional<User> user = userRepository.findByNick("value");
            if(user.isPresent()){
                UserDTO userdto = user.get().toDTO();
                return userdto;
            }
        }else if(type.equals("email")){
            Optional<User> user = userRepository.findByEmail(value);
            if(user.isPresent()){
                UserDTO userdto = user.get().toDTO();
                return userdto;
            }

        }

        return null;

    }

    public void selectUser(){}
    public void selectUsers(){}
    public void updateUser(){}
    public void deleteUser(){}


//terms
    public TermsDTO selectTemrs(){
        List<Terms> list= termsRepository.findAll();
        TermsDTO termsDTO= list.get(0).toDTO();
        return termsDTO;
    }

}
