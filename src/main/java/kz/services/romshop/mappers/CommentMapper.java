package kz.services.romshop.mappers;

import kz.services.romshop.dto.CommentDTO;
import kz.services.romshop.models.Comment;
import kz.services.romshop.models.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDTO fromCommentDto(Comment comment) {
        return CommentDTO.builder()
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .username(comment.getUser().getUsername())
                .rating(comment.getRating())
                .data(comment.getDataComment())
                .text(comment.getText())
                .build();
    }

    public Comment newComment(CommentDTO dto, User user) {
        return Comment.builder()
                .user(user)
                .text(dto.getText())
                .rating(dto.getRating())
                .status(dto.getRating() >= 4)
                .answerAdmin(null)
                .build();
    }
}
