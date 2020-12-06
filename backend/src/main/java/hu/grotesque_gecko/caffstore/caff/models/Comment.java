package hu.grotesque_gecko.caffstore.caff.models;

import hu.grotesque_gecko.caffstore.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CAFF caff;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User lastModifiedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment )) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
