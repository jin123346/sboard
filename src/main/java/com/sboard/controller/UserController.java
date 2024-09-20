package com.sboard.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sboard.config.AppConfig;
import com.sboard.dto.TermsDTO;
import com.sboard.dto.UserDTO;
import com.sboard.security.MyUserDetailsService;
import com.sboard.service.EmailService;
import com.sboard.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController {

    private final AppConfig appConfig;

    private final UserService userService;
    private final EmailService emailService;
    private final MyUserDetailsService myUserDetailsService;

    @GetMapping(value = {"/user/login" ,"/"})
    public String login(Model model){

        model.addAttribute("appVersion", appConfig.getAppInfo());
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
    public String register(UserDTO userDTO){
        log.info(userDTO.toString());
        if(userDTO == null){
            return "/user/register?success=202";
        }

        UserDTO savedUser= userService.insertUser(userDTO);

        return "redirect:/user/login?success=200";
    }


    @ResponseBody
    @GetMapping("/user/checkUser")
    public ResponseEntity<Integer> checkUser(@RequestParam("type") String type,
                                             @RequestParam("value") String value, HttpSession session) throws MessagingException {


        UserDTO user = userService.selectUserByType(type,value);
        int response= 0;

        //해당되는 유저가 있을때
        if(user != null){
            response= 1;
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        //해당되는 유저가 없는 email일때,
        if(type.equals("email") && user == null){
            LocalDateTime requestedAt = LocalDateTime.now();
            String code = emailService.sendMail(value, "/user/email.html");

            session.setAttribute("code", code);

           log.info("code : "+code);
            session.setAttribute("verificationCode",code);
            session.setAttribute("codeGenerationTime",requestedAt);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @ResponseBody
    @PostMapping("/user/checkUser")
    public ResponseEntity<Integer> checkUser(@RequestBody String requestBody
                                            , HttpSession session, Map map) {
        LocalDateTime requestedAt = (LocalDateTime) session.getAttribute("codeGenerationTime");
        String sessionCode = (String) session.getAttribute("verificationCode");
        String code = null;
        int response= 0;

        try{
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> jsonMap= mapper.readValue(requestBody,Map.class);
            code=jsonMap.get("code");
        } catch (Exception e) {
            log.error("Failed to parse JSON",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        log.info("code : "+ code);




        log.info("sessionCode : "+sessionCode);

        if (sessionCode == null || requestedAt == null) {
            return ResponseEntity.ok().body(response);
        }

        if (requestedAt.plusMinutes(5).isBefore(LocalDateTime.now())) {
            return ResponseEntity.ok().body(response);
        }

        if(sessionCode.equals(code)){
            session.removeAttribute("verificationCode");
            session.removeAttribute("codeGenerationTi|me");
            response=1;
            return ResponseEntity.ok().body(response);

        }else{
            return ResponseEntity.ok().body(response);
        }




    }



}
