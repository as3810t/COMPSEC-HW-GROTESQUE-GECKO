package hu.grotesque_gecko.caffstore.caff.services;

import com.google.protobuf.ByteString;
import hu.grotesque_gecko.caff_parser.service.CaffParserGrpc;
import hu.grotesque_gecko.caff_parser.service.CaffParserOuterClass;
import hu.grotesque_gecko.caffstore.authorization.services.AuthorizeService;
import hu.grotesque_gecko.caffstore.caff.exceptions.CAFFDoesNotExistsException;
import hu.grotesque_gecko.caffstore.caff.exceptions.CAFFFormatErrorException;
import hu.grotesque_gecko.caffstore.caff.exceptions.CAFFTemporalErrorException;
import hu.grotesque_gecko.caffstore.caff.exceptions.CAFFTitleCannotBeEmptyException;
import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.caff.models.CAFFFileData;
import hu.grotesque_gecko.caffstore.caff.repositories.CAFFFileRepository;
import hu.grotesque_gecko.caffstore.caff.repositories.CAFFRepository;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.services.UserService;
import hu.grotesque_gecko.caffstore.utils.Paginated;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static hu.grotesque_gecko.caffstore.utils.Preconditions.checkPagination;
import static hu.grotesque_gecko.caffstore.utils.Preconditions.checkParameter;

@Service
public class CAFFService {
    @Autowired
    CAFFRepository caffRepository;

    @Autowired
    CAFFFileRepository fileRepository;

    @Autowired
    AuthorizeService authorizeService;

    @Autowired
    UserService userService;

    @Value("${spring.native.address:localhost:50051}")
    String nativeParserAddress;

    public Paginated<CAFF> getAll(
        User currentUser,
        int offset,
        int pageSize,
        String title,
        String tag,
        String userId
    ) {
        authorizeService.canGetAllCAFF(currentUser);
        checkPagination(offset, pageSize);

        PageRequest pageRequest = PageRequest.of(offset, pageSize);
        Page<CAFF> caffs;
        if(!userId.isEmpty()) {
            User user = userService.internalFindOneById(userId);
            if(!title.isEmpty() && !tag.isEmpty()) {
                caffs = caffRepository.findAllByOwnerAndTitleContainingAndTagsContaining(user, title, tag, pageRequest);
            }
            else if(!title.isEmpty()) {
                caffs = caffRepository.findAllByOwnerAndTitleContaining(user, title, pageRequest);
            }
            else if(!tag.isEmpty()) {
                caffs = caffRepository.findAllByOwnerAndTagsContaining(user, tag, pageRequest);
            }
            else {
                caffs = caffRepository.findAllByOwner(user, pageRequest);
            }
        }
        else {
            if(!title.isEmpty() && !tag.isEmpty()) {
                caffs = caffRepository.findAllByTitleContainingAndTagsContaining(title, tag, pageRequest);
            }
            else if(!title.isEmpty()) {
                caffs = caffRepository.findAllByTitleContaining(title, pageRequest);
            }
            else if(!tag.isEmpty()) {
                caffs = caffRepository.findAllByTagsContaining(tag, pageRequest);
            }
            else {
                caffs = caffRepository.findAll(pageRequest);
            }
        }

        return new Paginated<>(caffs.getContent(), caffs.getTotalElements());
    }

    @Transactional
    public CAFF createOne(
        User currentUser,
        String title,
        List<String> tags,
        ByteBuffer file
    ) {
        authorizeService.canCreateCAFF(currentUser);

        checkParameter(!title.isEmpty(), CAFFTitleCannotBeEmptyException.class);

        CAFF newCaff = CAFF.builder()
            .title(title)
            .tags(String.join(";", tags))
            .owner(currentUser)
            .lastModifiedBy(currentUser)
            .lastModifiedDate(new Date())
            .build();

        caffRepository.save(newCaff);

        ByteBuffer preview = generatePreview(file);
        byte[] previewArray = new byte[preview.remaining()];
        preview.get(previewArray);

        CAFFFileData fileData = CAFFFileData.builder()
            .caff(newCaff)
            .file(file.array())
            .preview(previewArray)
            .build();
        newCaff.setFileData(fileData);

        fileRepository.save(fileData);

        return newCaff;
    }

    public CAFF getOneById(
        User currentUser,
        String id
    ) {
        Optional<CAFF> caff = caffRepository.findById(id);
        if(!caff.isPresent()) {
            throw new CAFFDoesNotExistsException();
        }

        authorizeService.canViewCAFF(currentUser, caff.get());

        return caff.get();
    }

    @Transactional
    public CAFF editOne(
        User currentUser,
        String id,
        String title,
        List<String> tags,
        ByteBuffer file
    ) {
        CAFF caff = getOneById(currentUser, id);
        authorizeService.canEditCAFF(currentUser, caff);

        checkParameter(!title.isEmpty(), CAFFTitleCannotBeEmptyException.class);

        caff.setTitle(title);
        caff.setTags(String.join(";", tags));

        if(file != null) {
            fileRepository.delete(caff.getFileData());

            ByteBuffer preview = generatePreview(file);
            byte[] previewArray = new byte[preview.remaining()];
            preview.get(previewArray);

            CAFFFileData fileData = CAFFFileData.builder()
                .caff(caff)
                .file(file.array())
                .preview(previewArray)
                .build();
            caff.setFileData(fileData);

            fileRepository.save(fileData);
        }

        caff.setLastModifiedBy(currentUser);
        caff.setLastModifiedDate(new Date());

        caffRepository.save(caff);

        return caff;
    }

    @Transactional
    public void deleteOne(
        User currentUser,
        String id
    ) {
        CAFF caff = getOneById(currentUser, id);
        authorizeService.canDeleteCAFF(currentUser, caff);
        caffRepository.delete(caff);
    }

    private ByteBuffer generatePreview(final ByteBuffer buffer) {
        try {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(nativeParserAddress)
                .usePlaintext()
                .maxInboundMessageSize(10000000)
                .build();

            final CaffParserGrpc.CaffParserBlockingStub blockingStub = CaffParserGrpc.newBlockingStub(channel);
            final CaffParserOuterClass.PreviewRequest request = CaffParserOuterClass.PreviewRequest.newBuilder()
                .setCaff(ByteString.copyFrom(buffer))
                .build();

            final CaffParserOuterClass.PreviewResponse response = blockingStub.previewCaff(request);
            channel.shutdownNow();

            if(response.hasBmp()) {
                return response.getBmp().asReadOnlyByteBuffer();
            }
            else {
                throw new CAFFFormatErrorException(response.getErrorMessage());
            }
        }
        catch(StatusRuntimeException e) {
            throw new CAFFTemporalErrorException(e);
        }
    }
}
