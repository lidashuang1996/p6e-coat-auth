package com.example.p6e_dawenjian_2023.folder;

import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class DefaultUploadFolderStorageLocationPathServiceImpl implements UploadFolderStorageLocationPathService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public String path() {
        return FileUtil.composePath(DATE_TIME_FORMATTER.format(
                LocalDateTime.now()), UUID.randomUUID().toString().replaceAll("-", ""));
    }

}
