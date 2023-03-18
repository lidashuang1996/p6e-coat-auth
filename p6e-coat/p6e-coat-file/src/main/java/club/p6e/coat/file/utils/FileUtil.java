package club.p6e.coat.file.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * 文件帮助类
 *
 * @author lidashuang
 * @version 1.0
 */
public final class FileUtil {

    /**
     * 文件连接符号
     */
    private static final String FILE_CONNECT_CHAR = ".";

    /**
     * 路径连接符号
     */
    private static final String PATH_CONNECT_CHAR = "/";

    private static final Random RANDOM = new Random();

    /**
     * 文件缓冲区大小
     */
    private static final int FILE_BUFFER_SIZE = 1024 * 1024 * 5;
    private static final DefaultDataBufferFactory DEFAULT_DATA_BUFFER_FACTORY = new DefaultDataBufferFactory();

    /**
     * 注入日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建文件夹
     *
     * @param folder 文件夹对象
     */
    public static void createFolder(File folder) {
        createFolder(folder, false);
    }

    /**
     * 创建文件夹
     *
     * @param folder      文件夹对象
     * @param deleteExist 是否删除存在的文件夹
     */
    public static void createFolder(File folder, boolean deleteExist) {
        boolean status = true;
        final String absolutePath = folder.getAbsolutePath();
        if (folder.exists()) {
            LOGGER.debug("[ CreateFolder ] => " + absolutePath + " exists !");
            if (deleteExist) {
                LOGGER.debug("[ CreateFolder ] => " + absolutePath + " exists >>> need delete !");
                LOGGER.debug("[ CreateFolder ] => " + absolutePath + " delete >>> " + deleteFolder(folder));
            } else {
                status = false;
            }
        }
        if (status) {
            LOGGER.debug("[ CreateFolder ] => " + absolutePath + " mkdirs >>> " + folder.mkdirs());
        }
    }

    /**
     * 创建文件夹
     *
     * @param folderPath 文件夹路径
     */
    public static void createFolder(String folderPath) {
        createFolder(folderPath, false);
    }

    /**
     * 创建文件夹
     *
     * @param folderPath  文件夹路径
     * @param deleteExist 是否删除存在的文件夹
     */
    public static void createFolder(String folderPath, boolean deleteExist) {
        createFolder(new File(folderPath), deleteExist);
    }

    /**
     * 删除文件夹
     *
     * @param folder 文件夹对象
     */
    public static boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            boolean result = true;
            LOGGER.debug("[ DeleteFolder ] => " + folder.getAbsolutePath() + " >>> [START]");
            final File[] files = folder.listFiles();
            if (files != null) {
                LOGGER.debug("[ DeleteFolder ] => " + folder.getAbsolutePath() + " catalogue (" + files.length + ")");
                for (final File f : files) {
                    if (f.isFile()) {
                        if (!deleteFile(f)) {
                            result = false;
                        }
                    } else if (f.isDirectory()) {
                        if (!deleteFolder(f)) {
                            result = false;
                        }
                    }
                }
                LOGGER.debug("[ DeleteFolder ] => " + folder.getAbsolutePath() + " delete >>> " + folder.delete());
            }
            LOGGER.debug("[ DeleteFolder ] => " + folder.getAbsolutePath() + " >>> [END]");
            return result;
        } else {
            LOGGER.debug("[ DeleteFolder ] ERROR => " + folder.getAbsolutePath() + " is not folder.");
            return false;
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹路径
     */
    public static boolean deleteFolder(String folderPath) {
        return deleteFolder(new File(folderPath));
    }

    /**
     * 删除文件
     *
     * @param file 文件对象
     * @return 删除操作结果
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            final boolean result = file.delete();
            LOGGER.debug("[ DeleteFile ] => " + file.getAbsolutePath() + " delete >>> " + result);
            return result;
        } else {
            LOGGER.debug("[ DeleteFile ] ERROR => " + file.getAbsolutePath() + " is not file.");
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 删除操作结果
     */
    public static boolean deleteFile(String filePath) {
        return deleteFile(new File(filePath));
    }

    /**
     * 验证文件是否存在
     *
     * @param file 文件对象
     * @return 文件是否存在结果
     */
    public static boolean checkFileExist(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * @param filePath 文件路径
     * @return 文件是否存在结果
     */
    public static boolean checkFileExist(String filePath) {
        return checkFileExist(new File(filePath));
    }

    /**
     * 文件路径拼接
     *
     * @param left  拼接左边
     * @param right 拼接右边
     * @return 拼接后的文件路径
     */
    public static String composePath(String left, String right) {
        if (left == null || right == null) {
            return "";
        } else {
            final StringBuilder result = new StringBuilder();
            if (left.endsWith(PATH_CONNECT_CHAR)) {
                result.append(left, 0, left.length() - 1);
            } else {
                result.append(left);
            }
            result.append(PATH_CONNECT_CHAR);
            if (right.startsWith(PATH_CONNECT_CHAR)) {
                result.append(right, 1, right.length());
            } else {
                result.append(right);
            }
            return result.toString();
        }
    }

    /**
     * 路径转换为绝对路径
     *
     * @param path 待转换路径
     * @return 转换为绝对路径
     */
    public static String convertAbsolutePath(String path) {
        if (path == null) {
            return "";
        } else {
            if (path.startsWith(PATH_CONNECT_CHAR)) {
                return path;
            } else {
                return PATH_CONNECT_CHAR + path;
            }
        }
    }

    /**
     * 文件拼接
     *
     * @param left  文件名称
     * @param right 文件后缀
     * @return 拼接后的文件
     */
    public static String composeFile(String left, String right) {
        if (left == null
                || right == null
                || left.isEmpty()
                || right.isEmpty()) {
            return null;
        } else {
            return left + FILE_CONNECT_CHAR + right;
        }
    }

    /**
     * 获取文件后缀
     *
     * @param content 文件名称
     * @return 文件后缀
     */
    public static String getSuffix(String content) {
        if (content != null && !content.isEmpty()) {
            final StringBuilder suffix = new StringBuilder();
            for (int i = content.length() - 1; i >= 0; i--) {
                final String ch = String.valueOf(content.charAt(i));
                if (FILE_CONNECT_CHAR.equals(ch)) {
                    return suffix.toString();
                } else {
                    suffix.append(ch);
                }
            }
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param file 文件对象
     * @return Flux<DataBuffer> 读取的文件内容
     */
    public static Flux<DataBuffer> readFile(File file) {
        if (file.isFile()) {
            try {
                return DataBufferUtils.read(new FileUrlResource(
                        file.getAbsolutePath()), DEFAULT_DATA_BUFFER_FACTORY, FILE_BUFFER_SIZE);
            } catch (IOException e) {
                return Flux.error(e);
            }
        } else {
            return Flux.error(new RuntimeException());
        }
    }

    public static File[] readFolder(String folderPath) {
        return readFolder(new File(folderPath));
    }

    public static File[] readFolder(File folder) {
        if (folder.isDirectory()) {
            return folder.listFiles((f, n) -> f.isFile());
        } else {
            return null;
        }
    }

    /**
     * 获取文件 MD5 签名
     *
     * @param file 文件对象
     * @return 签名内容
     */
    public static Mono<String> obtainMD5Signature(File file) {
        if (file.isFile()) {
            final MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                return Mono.error(e);
            }
            return readFile(file)
                    .flatMap(buffer -> {
                        md.update(buffer.toByteBuffer());
                        return Mono.just(buffer.readPosition());
                    })
                    .collectList()
                    .flatMap(l -> {
                        final byte[] md5Bytes = md.digest();
                        final StringBuilder hexValue = new StringBuilder();
                        for (final byte md5Byte : md5Bytes) {
                            int val = ((int) md5Byte) & 0xff;
                            if (val < 16) {
                                hexValue.append("0");
                            }
                            hexValue.append(Integer.toHexString(val));
                        }
                        return Mono.just(hexValue.toString());
                    });
        } else {
            return Mono.error(new RuntimeException());
        }
    }

    /**
     * 获取文件 MD5 签名
     *
     * @param filePath 文件路径
     * @return 签名内容
     */
    public static Mono<String> obtainMD5Signature(String filePath) {
        return obtainMD5Signature(new File(filePath));
    }

    /**
     * 生成唯一的文件名称
     *
     * @return 文件名称
     */
    public static String generateName() {
        return GeneratorUtil.uuid() + GeneratorUtil.random(6, true, false);
    }


}
