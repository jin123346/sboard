package com.sboard.controller;


import com.sboard.dto.CommentDTO;
import com.sboard.repository.CommentRepository;
import com.sboard.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comment/write")
    public ResponseEntity<CommentDTO> write(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {

        String regip = req.getRemoteAddr();
        commentDTO.setRegip(regip);
        log.info(commentDTO);

        CommentDTO dto = commentService.insertComment(commentDTO);
        log.info("dto : "+dto);
        return ResponseEntity.ok().body(dto);

    }

    @GetMapping("/comment/delete")
    public String delete(@RequestParam int no,@RequestParam int pg,@RequestParam String uid) {

        commentService.selectCommentByuid(no, uid);

        commentService.deleteComment(no);
        return "redirect:/article/view?no="+no+"&pg="+pg;
    }



}
