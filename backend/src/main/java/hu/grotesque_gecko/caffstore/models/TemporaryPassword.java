package hu.grotesque_gecko.caffstore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Entity
@Table(name="temporary_passwords")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryPassword {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String id;

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date validUntil;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemporaryPassword )) return false;
        return id != null && id.equals(((TemporaryPassword) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
