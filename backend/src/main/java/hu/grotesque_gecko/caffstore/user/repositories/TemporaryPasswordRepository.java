package hu.grotesque_gecko.caffstore.user.repositories;

import hu.grotesque_gecko.caffstore.user.models.TemporaryPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryPasswordRepository extends JpaRepository<TemporaryPassword, String> {
}
