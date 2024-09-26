package com.sboard.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sboard.config.AppConfig;
import com.sboard.config.AppInfo;
import com.sboard.dto.TermsDTO;
import com.sboard.dto.UserDTO;
import com.sboard.security.MyUserDetailsService;
import com.sboard.service.EmailService;
import com.sboard.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController {

    private final AppInfo appInfo;

    private final UserService userService;
    private final EmailService emailService;
    private final MyUserDetailsService myUserDetailsService;

    @GetMapping("/user/login")
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
    public String register(UserDTO userDTO, HttpServletRequest req){
        log.info(userDTO.toString());
        if(userDTO == null){
            return "/user/register?success=202";
        }
        String regip= req.getRemoteAddr();
        userDTO.setRegip(regip);

        UserDTO savedUser= userService.insertUser(userDTO);

        return "redirect:/user/login?success=200";
    }


    @ResponseBody
    @GetMapping("/user/{type}/{value}")
    public ResponseEntity<?> checkUser(@PathVariable("type")  String type,
                                       @PathVariable("value") String value, HttpSession session, Model model)  {

        log.info("type : "+type+", value : "+value);
        int count = userService.selectCountUserByType(type,value);



        //해당되는 유저가 없는 email일때,
        if(type.equals("email") && count == 0){
            LocalDateTime requestedAt = LocalDateTime.now();
            String code = emailService.sendMail(value, "/user/email.html",session);
            log.info("value code : "+code);


        }

        // Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);


        return ResponseEntity.ok().body(resultMap);

    }

    //이메일 일치 검사
    @ResponseBody
    @PostMapping("/email")
    public ResponseEntity<?> checkUser(@RequestBody Map<String, String> jsonData
                                            , HttpSession session) {

        log.info("checkEmail code : " + jsonData);

        String receiveCode = jsonData.get("code");
        log.info("checkEmail receiveCode : " + receiveCode);


        String sessionCode = (String) session.getAttribute("code");


        log.info("sessionCode : "+sessionCode);


        // Json 생성
        Map<String, Object> resultMap;
        if(sessionCode.equals(receiveCode)){
            // Json 생성
            resultMap = new HashMap<>();
            resultMap.put("result", true);


        }else{
            // Json 생성
            resultMap = new HashMap<>();
            resultMap.put("result", false);
        }

        session.removeAttribute("code");

        return ResponseEntity.ok().body(resultMap);


    }




}
