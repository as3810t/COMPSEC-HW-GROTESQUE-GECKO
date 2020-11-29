package hu.grotesque_gecko.caffstore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAFFDTO {
    private String id;
    private String title;
    private List<String> tags;

    private String ownerId;
    private String ownerName;
    private String lastModifiedById;
    private String lastModifiedByName;

    private Date lastModifiedDate;
}
