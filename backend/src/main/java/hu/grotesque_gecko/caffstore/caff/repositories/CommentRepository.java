package hu.grotesque_gecko.caffstore.caff.repositories;

import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.caff.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findAllByCaff(CAFF caff, Pageable pageable);
}
