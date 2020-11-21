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
public class CommentDTO {
    private String id;
    private String content;
    private Date createdDate;

    private String caffId;
    private String userId;
    private String userName;

    private String lastModifiedById;
    private String lastModifiedByName;
    private Date lastModifiedDate;
}
