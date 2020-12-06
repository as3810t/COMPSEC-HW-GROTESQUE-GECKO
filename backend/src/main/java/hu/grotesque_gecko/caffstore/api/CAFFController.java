package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.api.dto.CAFFDTO;
import hu.grotesque_gecko.caffstore.api.dto.CAFFListDTO;
import hu.grotesque_gecko.caffstore.api.dto.CommentDTO;
import hu.grotesque_gecko.caffstore.api.dto.CommentListDTO;
import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.caff.models.Comment;
import hu.grotesque_gecko.caffstore.caff.services.CAFFService;
import hu.grotesque_gecko.caffstore.caff.services.CommentService;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.utils.Paginated;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    CommentService commentService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CAFFListDTO getAll(
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

        return CAFFListDTO.builder()
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
        @RequestPart(required = false) String tags,
        @RequestPart MultipartFile file
    ) {
        CAFF newCaff = caffService.createOne(
            currentUser,
            title,
            tags == null ? Collections.emptyList() : Arrays.asList(tags.split("\\|")),
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
    @ApiResponses({
        @ApiResponse(code = 200, message = "CAFF file", response = byte[].class)
    })
    public ResponseEntity<Resource> download(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        byte[] file = caff.getFileData().getFile();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename=\"" + caff.getTitle() + ".caff\"");

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new ByteArrayResource(file));
    }

    @GetMapping(value = "/{id}/preview", produces = "image/bmp")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") }, produces = "image/bmp")
    @ApiResponses({
        @ApiResponse(code = 200, message = "BMP file", response = byte[].class)
    })
    public ResponseEntity<Resource> preview(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        byte[] file = caff.getFileData().getPreview();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "inline");

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length)
            .contentType(MediaType.parseMediaType("image/bmp"))
            .body(new ByteArrayResource(file));
    }

    @SneakyThrows
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CAFFDTO editOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @RequestParam String title,
        @RequestPart(required = false) String tags,
        @RequestPart(required = false) MultipartFile file
    ) {
        CAFF caff = caffService.editOne(
            currentUser,
            id,
            title,
            tags == null ? Collections.emptyList() : Arrays.asList(tags.split("\\|")),
            file == null ? null : ByteBuffer.wrap(file.getBytes())
        );
        return caffToDTO(caff);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public void deleteOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        caffService.deleteOne(currentUser, id);
    }

    @GetMapping(value = "/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody
    CommentListDTO getAllComment(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "100") int pageSize
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        Paginated<Comment> comments = commentService.getAll(currentUser, caff, offset, pageSize);

        return CommentListDTO.builder()
            .comments(comments.getEntities().stream().map(this::commentToDTO).collect(Collectors.toList()))
            .totalCount((int) comments.getTotalCount())
            .offset(offset)
            .pageSize(pageSize)
            .build();
    }

    @PostMapping(value = "/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CommentDTO createOneComment(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @RequestPart String content
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        Comment newComment = commentService.createOne(
            currentUser,
            caff,
            content
        );
        return commentToDTO(newComment);
    }

    @GetMapping(value = "/{id}/comment/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CommentDTO getOneComment(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @PathVariable String commentId
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        return commentToDTO(commentService.getOne(currentUser, caff, commentId));
    }

    @PutMapping(value = "/{id}/comment/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody CommentDTO editOneCOmment(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @PathVariable String commentId,
        @RequestPart String content
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        Comment comment = commentService.editOne(
            currentUser,
            caff,
            commentId,
            content
        );
        return commentToDTO(comment);
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public void deleteOneComment(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @PathVariable String commentId
    ) {
        CAFF caff = caffService.getOneById(currentUser, id);
        commentService.deleteOne(currentUser, caff, commentId);
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

    private CommentDTO commentToDTO(Comment comment) {
        return CommentDTO.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .caffId(comment.getCaff().getId())
            .userId(comment.getUser() != null ? comment.getUser().getId() : null)
            .userName(comment.getUser() != null ? comment.getUser().getUsername() : null)
            .createdDate(comment.getCreatedDate())
            .lastModifiedById(comment.getLastModifiedBy().getId())
            .lastModifiedByName(comment.getLastModifiedBy().getUsername())
            .lastModifiedDate(comment.getLastModifiedDate())
            .build();
    }
}
