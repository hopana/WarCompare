package com.wft.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * 文件操作工具类
 *
 * @author hupan
 * @date 2018/07/04
 */
public class FileUtil {
    private final static Logger log = Logger.getLogger(FileUtil.class);

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 复制文件夹
     *
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static byte[] read(String filePath, String fileName) throws Exception {
        byte[] ret = null;
        File file = new File(new File(filePath), fileName);
        if (!file.exists()) {
            throw new Exception();
        }
        long fileLen = file.length();
        ret = new byte[(int) fileLen];
        long numRead = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            numRead = fis.read(ret, 0, (int) fileLen);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (numRead != fileLen) {
            throw new Exception("读取文件错误");
        }
        return ret;
    }

    public static String readFileContent(File file) throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        java.lang.StringBuffer content = new StringBuffer();
        String line = "";
        while ((line = d.readLine()) != null) {
            content.append(line + "\n");
        }
        d.close();
        return new String(content);
    }

    public static String readFileContent(File file, String charset) throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        java.lang.StringBuffer content = new StringBuffer();
        String line = "";
        while ((line = d.readLine()) != null) {
            content.append(line + "\n");
        }
        d.close();
        return new String(content);
    }

    public static void writeFile(File file, byte[] b) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        dos.write(b);
        dos.flush();
        dos.close();
    }

    public static void writeFileContent(File file, String content) throws IOException {
        java.io.BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        bw.write(content);
        bw.flush();
        bw.close();
    }

    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    boolean success = deleteFile(child);
                    if (!success) {
                        return false;
                    }
                }
            }

            return file.delete();
        }

        return file.delete();
    }

}
