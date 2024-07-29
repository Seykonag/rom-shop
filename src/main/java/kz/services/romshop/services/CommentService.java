package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.CommentDTO;
import kz.services.romshop.mappers.CommentMapper;
import kz.services.romshop.models.*;
import kz.services.romshop.repositories.CommentRepository;
import kz.services.romshop.repositories.OrderRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CommentRepository repository;
    private final CommentMapper commentMapper;

    @Transactional
    public void createComment(CommentDTO dto, String username) {
        Order order = orderRepository.getReferenceById(dto.getIdOrder());
        Product product = productRepository.getReferenceById(dto.getIdProduct());
        User user = userRepository.getReferenceByUsername(username);

        boolean approved = false;

        for (OrderDetails details: order.getDetails()) {
            approved = Objects.equals(details.getProduct().getId(), product.getId());
            if (approved) break;
        }

        if (!approved) throw new RuntimeException("Продукта нет в заказе");

        if (order.getUser() != user) throw new RuntimeException("Купите товар, чтобы оценить");

        if (order.getStatus() != OrderStatus.CLOSED) throw new RuntimeException("Ошибка заказа");

        List<Comment> comments;

        if (product.getComment() == null) comments = new ArrayList<>();
        else comments = product.getComment();

        Comment newComment = commentMapper.newComment(dto, user);

        comments.add(newComment);

        product.setComment(comments);
        repository.save(newComment);
        productRepository.save(product);
    }

    public void adminAnswer(Long id, String text) {
        if (text == null) throw  new RuntimeException("Нет текса у коментария");

        Comment comment = repository.getReferenceById(id);

        comment.setAnswerAdmin(text);
        repository.save(comment);
    }

    public List<CommentDTO> findByProduct(Long id) {
        Product product = productRepository.getReferenceById(id);

        List<Comment> comments = product.getComment();
        List<CommentDTO> list = new ArrayList<>();


        for (Comment comment: comments) {
            list.add(commentMapper.fromCommentDto(comment));
        }

        return list;
    }

    public void deleteComment(Long id) { repository.delete(repository.getReferenceById(id)); }
}
