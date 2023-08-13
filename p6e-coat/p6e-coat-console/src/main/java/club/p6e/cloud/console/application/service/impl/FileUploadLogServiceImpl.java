package club.p6e.cloud.console.application.service.impl;

import club.p6e.cloud.console.application.service.FileUploadLogService;
import club.p6e.cloud.console.domain.aggregate.FileUploadLogDetailsAggregate;
import club.p6e.cloud.console.domain.aggregate.FileUploadLogListAggregate;
import club.p6e.cloud.console.infrastructure.context.FileUploadLogContext;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class FileUploadLogServiceImpl implements FileUploadLogService {
    @Override
    public FileUploadLogContext.ListDto list(FileUploadLogContext.Request request) {
        final FileUploadLogListAggregate aggregate = FileUploadLogListAggregate.search(
                request.getPage(),
                request.getSize(),
                request.getQuery()
        );
        final FileUploadLogContext.ListDto result = new FileUploadLogContext.ListDto();
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setList(CopyUtil.runList(aggregate.getList(), FileUploadLogContext.Item.class));
        return result;
    }

    @Override
    public FileUploadLogContext.Details.Dto details(FileUploadLogContext.Details.Request request) {
        final FileUploadLogDetailsAggregate aggregate = FileUploadLogDetailsAggregate.get(request.getId());
        final FileUploadLogContext.Details.Dto result =
                CopyUtil.run(aggregate.getModel(), FileUploadLogContext.Details.Dto.class);
        result.setList(CopyUtil.runList(aggregate.getList(), FileUploadLogContext.Details.ChunkModel.class));
        return result;
    }

    @Override
    public FileUploadLogContext.Storage.Dto storage(FileUploadLogContext.Storage.Request request) {

        return null;
    }

}
