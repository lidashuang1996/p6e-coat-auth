package club.p6e.cloud.console.domain.aggregate;

import club.p6e.cloud.console.error.GlobalExceptionContext;
import club.p6e.cloud.console.infrastructure.model.FileUploadChunkModel;
import club.p6e.cloud.console.infrastructure.model.FileUploadModel;
import club.p6e.cloud.console.infrastructure.repository.FileUploadChunkRepository;
import club.p6e.cloud.console.infrastructure.repository.FileUploadRepository;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FileUploadLogDetailsAggregate {

    private final FileUploadModel model;
    private final List<FileUploadChunkModel> list;

    public static FileUploadLogDetailsAggregate get(Integer id) {
        final FileUploadRepository fRepository = SpringUtil.getBean(FileUploadRepository.class);
        final FileUploadChunkRepository fcRepository = SpringUtil.getBean(FileUploadChunkRepository.class);
        final Optional<FileUploadModel> fOptional = fRepository.findById(id);
        if (fOptional.isEmpty()) {
            throw GlobalExceptionContext.executeParameterException(
                    FileUploadLogDetailsAggregate.class,
                    "",
                    ""
            );
        }
        final FileUploadModel fileUploadModel = fOptional.get();
        final List<FileUploadChunkModel> fileUploadChunkModelList = fcRepository.findAll(
                (Specification<FileUploadChunkModel>) (rt, qy, cb) ->
                        cb.equal(rt.get(FileUploadChunkModel.FID), fileUploadModel.getId()),
                Sort.by(Sort.Order.asc(FileUploadChunkModel.ID)));
        return new FileUploadLogDetailsAggregate(fileUploadModel, new ArrayList<>(fileUploadChunkModelList));
    }

    private FileUploadLogDetailsAggregate(FileUploadModel model, List<FileUploadChunkModel> list) {
        this.list = list;
        this.model = model;
    }

    public FileUploadModel getModel() {
        return model;
    }

    public List<FileUploadChunkModel> getList() {
        return list;
    }
}
