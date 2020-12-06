package hu.grotesque_gecko.caffstore.caff.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="caff_file_Data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAFFFileData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String id;

    @Lob
    private byte[] file;

    @Lob
    private byte[] preview;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private CAFF caff;
}
