package club.p6e.coat.file.manage.infrastructure.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class FileContext implements Serializable {

    @Data
    @Accessors(chain = true)
    public static class UpdateRequest implements Serializable {
        private Integer id;
        private String name;
        private List<String> tags;
    }

    @Data
    @Accessors(chain = true)
    public static class CreateFolderRequest implements Serializable {
        private Integer id;
        private Integer pid;
        private String name;
    }

    @Data
    @Accessors(chain = true)
    public static class CreateFileVo implements Serializable {
        private Integer id;
        private String operator;
    }


    @Data
    @Accessors(chain = true)
    public static class CreateFileDto implements Serializable {
        private Integer id;
        private String operator;
    }

    @Data
    @Accessors(chain = true)
    public static class CreateFolderVo implements Serializable {
        private Integer id;
    }


    @Data
    @Accessors(chain = true)
    public static class CreateFolderDto implements Serializable {
        private Integer id;
    }



    @Data
    @Accessors(chain = true)
    public static class DeleteFolderRequest implements Serializable {
        private Integer id;
        private Integer pid;
        private String name;
    }

    @Data
    @Accessors(chain = true)
    public static class DeleteFolderVo implements Serializable {
    }


    @Data
    @Accessors(chain = true)
    public static class DeleteFolderDto implements Serializable {
    }




}
