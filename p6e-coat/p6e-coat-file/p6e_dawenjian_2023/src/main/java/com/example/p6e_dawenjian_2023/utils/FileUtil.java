package com.example.p6e_dawenjian_2023.utils;

import java.io.File;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FileUtil {

    public static void createFolder(String folderPath) {

    }

    public static String composePath(String a, String b) {
        return a + "/" + b;
    }

    public static String convertAbsolutePath(String a) {
        return a;
    }

    public static boolean checkFileExist(String file) {
        return checkFileExist(new File(file));
    }

    public static boolean checkFileExist(File file) {
        return file != null && file.exists() && file.isFile();
    }
}
