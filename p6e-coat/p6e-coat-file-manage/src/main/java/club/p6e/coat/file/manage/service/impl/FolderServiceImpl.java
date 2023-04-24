package club.p6e.coat.file.manage.service.impl;

import club.p6e.coat.file.manage.domain.entity.FileEntity;
import club.p6e.coat.file.manage.domain.entity.FolderEntity;
import club.p6e.coat.file.manage.infrastructure.context.FolderContext;
import club.p6e.coat.file.manage.infrastructure.model.FileModel;
import club.p6e.coat.file.manage.infrastructure.model.FolderModel;
import club.p6e.coat.file.manage.service.FolderService;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class FolderServiceImpl implements FolderService {

    @Override
    public FolderContext.CreateFileDto createFile(FolderContext.CreateFileRequest param) {
        final FileEntity entity = FileEntity.create(param.getId(),
                CopyUtil.run(param, FileModel.class).setSource(param.getPath()));
        return CopyUtil.run(entity.getModel(), FolderContext.CreateFileDto.class);
    }

    @Override
    public FolderContext.CreateFolderDto createFolder(FolderContext.CreateFolderRequest param) {
        final FolderEntity entity = FolderEntity.create(param.getId(), CopyUtil.run(param, FolderModel.class));
        return CopyUtil.run(entity.getModel(), FolderContext.CreateFolderDto.class);
    }

    @Override
    public FolderContext.DeleteFolderDto deleteFile(FolderContext.DeleteFolderRequest param) {
        return null;
    }

    @Override
    public FolderContext.DeleteFolderDto delete(FolderContext.DeleteFolderRequest deleteFolderRequest) {
        return null;
    }

}
