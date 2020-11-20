package hu.grotesque_gecko.caffstore.repositories;

import hu.grotesque_gecko.caffstore.models.CAFF;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CAFFRepository extends JpaRepository<CAFF, String> {
}
