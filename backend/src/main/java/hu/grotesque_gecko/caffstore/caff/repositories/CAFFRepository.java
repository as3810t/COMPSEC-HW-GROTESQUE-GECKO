package hu.grotesque_gecko.caffstore.caff.repositories;

import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CAFFRepository extends JpaRepository<CAFF, String> {
    Page<CAFF> findAllByTitleContaining(String title, Pageable pageable);
    Page<CAFF> findAllByTagsContaining(String tag, Pageable pageable);
    Page<CAFF> findAllByTitleContainingAndTagsContaining(String title, String tag, Pageable pageable);
    Page<CAFF> findAllByOwner(User owner, Pageable pageable);
    Page<CAFF> findAllByOwnerAndTitleContaining(User owner, String title, Pageable pageable);
    Page<CAFF> findAllByOwnerAndTagsContaining(User owner, String tag, Pageable pageable);
    Page<CAFF> findAllByOwnerAndTitleContainingAndTagsContaining(User owner, String title, String tag, Pageable pageable);
}
