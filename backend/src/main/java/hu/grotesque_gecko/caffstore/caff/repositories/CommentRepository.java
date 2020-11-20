package hu.grotesque_gecko.caffstore.caff.repositories;

import hu.grotesque_gecko.caffstore.caff.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
