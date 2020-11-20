package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.api.dto.CAFFDTO;
import hu.grotesque_gecko.caffstore.api.dto.CAFFListResponse;
import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.caff.services.CAFFService;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.utils.Paginated;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/caff")
public class CAFFController {
    @Autowired
    CAFFService caffService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CAFFListResponse getAll(
        @AuthenticationPrincipal User currentUser,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "100") int pageSize,
        @RequestParam(required = false, defaultValue = "") String title,
        @RequestParam(required = false, defaultValue = "") String tag,
        @RequestParam(required = false, defaultValue = "") String userId
    ) {
        Paginated<CAFF> caffs = caffService.getAll(
            currentUser,
            offset,
            pageSize,
            title,
            tag,
            userId
        );

        return CAFFListResponse.builder()
            .caffs(caffs.getEntities().stream().map(this::caffToDTO).collect(Collectors.toList()))
            .totalCount((int) caffs.getTotalCount())
            .offset(offset)
            .pageSize(pageSize)
            .build();
    }

    @SneakyThrows
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CAFFDTO createOne(
        @AuthenticationPrincipal User currentUser,
        @RequestParam String title,
        @RequestParam(required = false, defaultValue = "") String tags,
        @RequestParam MultipartFile file
    ) {
        CAFF newCaff = caffService.createOne(
            currentUser,
            title,
            tags.equals("") ? Collections.emptyList() : Arrays.asList(tags.split("\\|")),
            ByteBuffer.wrap(file.getBytes())
        );
        return caffToDTO(newCaff);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CAFFDTO getOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        return caffToDTO(caffService.getOneById(currentUser, id));
    }

    @GetMapping(value = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody byte[] download(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        byte[] file = caff.getFileData().getFile();

        return file;
    }

    @GetMapping(value = "/{id}/preview", produces = "image/bmp")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody byte[] preview(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        byte[] file = caff.getFileData().getPreview();

        return file;
    }

    @SneakyThrows
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CAFFDTO editOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @RequestParam String title,
        @RequestParam(required = false, defaultValue = "") String tags,
        @RequestParam(required = false) MultipartFile file
    ) {
        CAFF caff = caffService.editOne(
            currentUser,
            id,
            title,
            tags.equals("") ? Collections.emptyList() : Arrays.asList(tags.split("\\|")),
            ByteBuffer.wrap(file.getBytes())
        );
        return caffToDTO(caff);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody void deleteOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        caffService.deleteOne(currentUser, id);
    }

    private CAFFDTO caffToDTO(CAFF caff) {
        return CAFFDTO.builder()
            .id(caff.getId())
            .title(caff.getTitle())
            .tags(caff.getTags())
            .ownerId(caff.getOwner().getId())
            .ownerName(caff.getOwner().getUsername())
            .lastModifiedById(caff.getLastModifiedBy().getId())
            .lastModifiedByName(caff.getLastModifiedBy().getUsername())
            .lastModifiedDate(caff.getLastModifiedDate())
            .build();
    }
}
