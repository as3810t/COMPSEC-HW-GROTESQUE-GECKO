package hu.grotesque_gecko.caffstore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAFFListDTO {
    private List<CAFFDTO> caffs;
    private int totalCount;
    private int offset;
    private int pageSize;
}
