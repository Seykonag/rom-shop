package kz.services.romshop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    private static final String SEQ_NAME = "comment_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private int rating;
    private String text;
    @CreationTimestamp
    private LocalDateTime dataComment;
    @ElementCollection
    @CollectionTable(name = "comment_advantages")
    private List<String> advantages;
    @ElementCollection
    @CollectionTable(name = "comment_disadvantages")
    private List<String> disadvantages;
    private String answerAdmin;
}
