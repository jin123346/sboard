package com.sboard.controller;


import com.sboard.dto.TermsDTO;
import com.sboard.dto.UserDTO;
import com.sboard.dto.VerificationCodeDTO;
import com.sboard.service.EmailService;
import com.sboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping(value = {"/user/login" ,"/"})
    public String login(){

        return "/user/login";
    }



    @GetMapping("/user/terms")
    public String terms(Model model){

        TermsDTO terms= userService.selectTemrs();
        model.addAttribute("terms",terms);

        return "/user/terms";
    }

    @GetMapping("/user/register")
    public String register(){
        return "/user/register";
    }

    @PostMapping("/user/register")
    public String register(UserDTO userDTO, Model model) {
        UserDTO savedUser = userService.insertUser(userDTO);

        if (savedUser != null) {
            model.addAttribute("user", savedUser);
            return "redircet:/user/login?success=100";
        }
        return "/user/login?success=200";
    }

    @ResponseBody
    @GetMapping("/user/checkUser")
    public ResponseEntity<Integer> checkUser(@RequestParam("type") String type,
                                    @RequestParam("value") String value){


        UserDTO user = userService.selectUserByType(type,value);
        int response= 0;


        if(user != null){
            response= 1;

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if(type.equals("email") && user == null){
            String email = value;
            VerificationCodeDTO requestEmail= VerificationCodeDTO.builder().email(email).build();
            emailService.sendVerificationCode(requestEmail);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @ResponseBody
    @PostMapping("/user/checkUser")
    public ResponseEntity<Integer> checkUser(@RequestParam("code") String code
                                            ){

        boolean isValid= emailService.verifyCode(code);
        if(isValid){
            return ResponseEntity.ok(1);
        }else{
            return ResponseEntity.ok(0);
        }
    }



}
