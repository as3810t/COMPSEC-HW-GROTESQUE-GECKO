package hu.grotesque_gecko.caffstore.models;

import hu.grotesque_gecko.caffstore.utils.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="caffs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAFF {
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

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @NotEmpty
    @Column(nullable = false)
    private String caffFileName;

    @NotEmpty
    @Column(nullable = false)
    private String caffPreviewFileName;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(
        mappedBy = "caff",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Column()
    private Date lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User lastModifiedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CAFF )) return false;
        return id != null && id.equals(((CAFF) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
