package club.p6e.coat.file.file;

/**
 * @author lidashuang
 * @version 1.0
 */
public class UploadFileCleanTask {

    private final UploadFileCleanStrategy strategy;

    public UploadFileCleanTask(UploadFileCleanStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        try {
            this.strategy.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
