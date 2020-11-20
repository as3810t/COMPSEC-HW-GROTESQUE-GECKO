package hu.grotesque_gecko.caffstore.caff.repositories;

import hu.grotesque_gecko.caffstore.caff.models.CAFFFileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CAFFFileRepository extends JpaRepository<CAFFFileData, String> {
}
