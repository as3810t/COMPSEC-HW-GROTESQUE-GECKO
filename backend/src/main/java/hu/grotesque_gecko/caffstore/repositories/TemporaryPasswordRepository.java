package hu.grotesque_gecko.caffstore.repositories;

import hu.grotesque_gecko.caffstore.models.TemporaryPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryPasswordRepository extends JpaRepository<TemporaryPassword, String> {
}
