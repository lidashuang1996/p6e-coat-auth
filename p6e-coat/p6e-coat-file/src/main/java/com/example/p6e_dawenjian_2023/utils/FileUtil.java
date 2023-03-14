package com.example.p6e_dawenjian_2023.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件帮助类
 *
 * @author lidashuang
 * @version 1.0
 */
public final class FileUtil {

    /**
     * 路径连接符号
     */
    private static final String PATH_CONNECT_CHAR = "/";

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
                LOGGER.debug("[ CreateFolder ] => " + absolutePath
                        + " exists ! need delete >>> " + deleteFolder(folder));
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
}
