package hu.grotesque_gecko.caffstore.repositories;

import hu.grotesque_gecko.caffstore.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
