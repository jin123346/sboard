package com.sboard.service;

import com.sboard.dto.CommentDTO;
import com.sboard.entity.Comment;
import com.sboard.entity.User;
import com.sboard.repository.CommentRepository;
import com.sboard.repository.UserRepository;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public CommentDTO selectCommentByuid(int no, String uid){

        
        return null;
    }


    public CommentDTO insertComment(CommentDTO commentdto) {
        Comment comment  = modelMapper.map(commentdto, Comment.class);
        User user = User.builder()
                .uid(comment.getUser().getUid())
                .nick(comment.getUser().getNick())
                .build();
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        CommentDTO commentDTO = modelMapper.map(savedComment, CommentDTO.class);
        log.info("insert commentDTO : "+commentDTO.toString());
        return  commentDTO;
    }
    public CommentDTO selectComment(CommentDTO commentdto) {return null;}
    public List<CommentDTO> selectComments(int no) {
        Optional<List<Comment>> optList = commentRepository.findCommentsByParent(no);
        if (optList.isPresent()) {
            List<Comment> comments = optList.get();
            log.info(comments);
            return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
        }
        return null;


    }
    public CommentDTO updateComment(CommentDTO commentdto) {
        return null;
    }
    public Boolean deleteComment(int no) {

       Boolean exists = commentRepository.existsById(no);

       if(exists) {
            commentRepository.deleteById(no);
            return true;
       }
        return false;
    }

}
