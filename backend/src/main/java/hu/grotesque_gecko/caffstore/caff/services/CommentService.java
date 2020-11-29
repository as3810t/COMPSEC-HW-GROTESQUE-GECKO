package hu.grotesque_gecko.caffstore.caff.services;

import hu.grotesque_gecko.caffstore.authorization.services.AuthorizeService;
import hu.grotesque_gecko.caffstore.caff.exceptions.CommentCannotBeEmptyException;
import hu.grotesque_gecko.caffstore.caff.exceptions.CommentDoesNotExistException;
import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.caff.models.Comment;
import hu.grotesque_gecko.caffstore.caff.repositories.CommentRepository;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.utils.Paginated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static hu.grotesque_gecko.caffstore.utils.Preconditions.checkPagination;
import static hu.grotesque_gecko.caffstore.utils.Preconditions.checkParameter;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AuthorizeService authorizeService;

    public Paginated<Comment> getAll(
        User currentUser,
        CAFF caff,
        int offset,
        int pageSize
    ) {
        authorizeService.canGetAllComment(currentUser, caff);
        checkPagination(offset, pageSize);

        Page<Comment> comments = commentRepository.findAllByCaff(
            caff,
            PageRequest.of(offset, pageSize, Sort.by("createdDate").descending())
        );

        return new Paginated<>(comments.getContent(), comments.getTotalElements());
    }

    @Transactional
    public Comment createOne(
        User currentUser,
        CAFF caff,
        String content
    ) {
        authorizeService.canMakeComment(currentUser, caff);
        checkParameter(!content.isEmpty(), CommentCannotBeEmptyException.class);

        Comment newComment = Comment.builder()
            .content(content)
            .caff(caff)
            .user(currentUser)
            .createdDate(new Date())
            .lastModifiedBy(currentUser)
            .lastModifiedDate(new Date())
            .build();

        commentRepository.save(newComment);

        return newComment;
    }

    public Comment getOne(
        User currentUser,
        CAFF caff,
        String id
    ) {
        authorizeService.canGetAllComment(currentUser, caff);

        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent() || !comment.get().getCaff().getId().equals(caff.getId())) {
            throw new CommentDoesNotExistException();
        }

        return comment.get();
    }

    @Transactional
    public Comment editOne(
        User currentUser,
        CAFF caff,
        String id,
        String content
    ) {
        Comment comment = getOne(currentUser, caff, id);
        authorizeService.canEditComment(currentUser, caff, comment);
        if(comment.getContent() == null) {
            throw new CommentDoesNotExistException();
        }

        checkParameter(!content.isEmpty(), CommentCannotBeEmptyException.class);

        comment.setContent(content);
        comment.setLastModifiedBy(currentUser);
        comment.setLastModifiedDate(new Date());

        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public void deleteOne(
        User currentUser,
        CAFF caff,
        String id
    ) {
        Comment comment = getOne(currentUser, caff, id);
        authorizeService.canDeleteComment(currentUser, caff, comment);

        comment.setContent(null);
        comment.setLastModifiedBy(currentUser);
        comment.setLastModifiedDate(new Date());

        commentRepository.save(comment);
    }
}
