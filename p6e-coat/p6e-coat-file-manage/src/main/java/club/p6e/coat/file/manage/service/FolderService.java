package club.p6e.coat.file.manage.service;

import club.p6e.coat.file.manage.infrastructure.context.FolderContext;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface FolderService {
    FolderContext.CreateFileDto createFile(FolderContext.CreateFileRequest param);

    FolderContext.CreateFolderDto createFolder(FolderContext.CreateFolderRequest param);

    FolderContext.DeleteFolderDto deleteFile(FolderContext.DeleteFolderRequest param);

    FolderContext.DeleteFolderDto delete(FolderContext.DeleteFolderRequest deleteFolderRequest);
}
