package com.wft.util;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩解压工具类
 *
 * @author hupan
 * @date 2018/07/05
 */
public class CompressUtil {
    private final static Logger log = Logger.getLogger(CompressUtil.class);
    private static final int BUFFER_SIZE = 1024;
    private static final String GZ_EXT = ".gz";
    private static final String ZIP_EXT = ".zip";

    public static void tar(String saveName, boolean deleteSrcFile, File... files) throws IOException {
        FileOutputStream fos = new FileOutputStream(saveName);
        TarArchiveOutputStream taos = new TarArchiveOutputStream(fos);
        // TAR has an 8 gig file limit by default, this gets around that
        taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
        // TAR originally didn't support long file names, so enable the support for it
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        taos.setAddPaxHeadersForNonAsciiNames(true);

        for (File file : files) {
            addToArchiveCompression(taos, file, "");
        }

        IOUtils.closeQuietly(taos, fos);

        if (deleteSrcFile) {
            for (File file : files) {
                FileUtil.deleteFile(file);
            }
        }
    }

    public static void untar(String in, File out) throws IOException {
        FileInputStream fis = new FileInputStream(in);
        TarArchiveInputStream tais = new TarArchiveInputStream(fis);
        TarArchiveEntry entry;
        while ((entry = tais.getNextTarEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            File curfile = new File(out, entry.getName());
            File parent = curfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(curfile);
            IOUtils.copy(tais, fos);
            IOUtils.closeQuietly(fos);
        }

        IOUtils.closeQuietly(tais, fis);
    }

    /**
     * 添加归档文件
     *
     * @param taos tar归档输出流
     * @param file 待归档的文件
     * @param basePath 归档的根路径
     * @throws IOException
     */
    private static void addToArchiveCompression(TarArchiveOutputStream taos, File file, String basePath) throws IOException {
        String entry = basePath + File.separator + file.getName();
        if (file.isFile()) {
            taos.putArchiveEntry(new TarArchiveEntry(file, entry));
            FileInputStream is = new FileInputStream(file);
            IOUtils.copy(is, taos);

            IOUtils.closeQuietly(is);
            taos.closeArchiveEntry();
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    addToArchiveCompression(taos, child, entry);
                }
            }
        }
    }

    /**
     * 将文件用gz压缩
     *
     * @param source 需要压缩的文件
     * @return File 返回压缩后的文件
     * @throws IOException
     */
    public static File gz(File source, String targetFile, boolean deleteSrcFile) throws IOException {
        File target;
        if (StringUtils.isNotBlank(targetFile)) {
            target = new File(targetFile);
        } else {
            target = new File(source.getParentFile(), source.getName() + GZ_EXT);
        }

        FileInputStream in = new FileInputStream(source);
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(target));
        byte[] array = new byte[BUFFER_SIZE];
        int number = -1;
        while ((number = in.read(array, 0, array.length)) != -1) {
            out.write(array, 0, number);
        }

        IOUtils.closeQuietly(out, in);

        if (deleteSrcFile) {
            if(!source.delete()) {
                FileUtil.deleteFile(source);
            }
        }

        return target;
    }

    /**
     * 解压
     *
     * @param warPath   war包路径
     * @param unzipPath 解压路径
     */
    public static void unzip(String warPath, String unzipPath) {
        File warFile = new File(warPath);

        BufferedInputStream bis = null;
        ArchiveInputStream ais = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(warFile));
            ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, bis);

            JarArchiveEntry entry = null;
            while ((entry = (JarArchiveEntry) ais.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(unzipPath, entry.getName()).mkdir();
                } else {
                    OutputStream os = FileUtils.openOutputStream(new File(unzipPath, entry.getName()));
                    IOUtils.copy(ais, os);
                    IOUtils.closeQuietly(os);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("未找到war文件");
        } catch (ArchiveException e) {
            log.error("不支持的压缩格式");
        } catch (IOException e) {
            log.error("文件写入发生错误");
        } finally {
            IOUtils.closeQuietly(ais, bis);
        }
    }

    /**
     * 压缩
     *
     * @param srcDir   压缩路径
     * @param destFile war包路径
     */
    public static void zipDir(String srcDir, String destFile, boolean deleteSrcDir) {
        File outFile = new File(destFile);

        BufferedOutputStream bos = null;
        ZipArchiveOutputStream aos = null;
        try {
            outFile.createNewFile();
            bos = new BufferedOutputStream(new FileOutputStream(outFile));
            aos = (ZipArchiveOutputStream) new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, bos);

            Iterator<File> files = FileUtils.iterateFiles(new File(srcDir), null, true);
            while (files.hasNext()) {
                File file = files.next();
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                aos.putArchiveEntry(zipArchiveEntry);

                FileInputStream fis = new FileInputStream(file);
                IOUtils.copy(fis, aos);
                aos.closeArchiveEntry();
                IOUtils.closeQuietly(fis);
            }

            //IOUtils.closeQuietly(aos, bos);
        } catch (IOException e) {
            log.error("创建文件失败");
        } catch (ArchiveException e) {
            log.error("不支持的压缩格式");
        } finally {
            IOUtils.closeQuietly(aos, bos);

            if (deleteSrcDir) {
                FileUtil.deleteFile(new File(srcDir));
            }
        }
    }

    public static void zipDirWithPath(String srcFileName, String targetFileName, boolean deleteSrcDir) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            log.error("文件不存在：" + srcFileName);
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipArchiveOutputStream zaos = null;

        try {
            fos = new FileOutputStream(targetFileName);
            bos = new BufferedOutputStream(fos);
            zaos = (ZipArchiveOutputStream) new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, bos);
            compress(srcFile, zaos, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(zaos, bos, fos);

            if (deleteSrcDir) {
                FileUtil.deleteFile(new File(srcFileName));
            }
        }
    }

    private static void compress(File srcFile, ZipArchiveOutputStream zaos, String basedir) {
        if (srcFile.isDirectory()) {
            if (!srcFile.exists()) {
                log.info("路径不存在：" + srcFile.getAbsolutePath());
                return;
            }

            for (File file : srcFile.listFiles()) {
                compress(file, zaos, basedir + srcFile.getName() + File.separator);
            }
        } else {
            if (!srcFile.exists()) {
                log.error("文件不存在：" + srcFile.getAbsolutePath());
                return;
            }

            try {
                zaos.putArchiveEntry(new ZipArchiveEntry(srcFile, basedir + srcFile.getName()));
                FileInputStream fis = new FileInputStream(srcFile);
                IOUtils.copy(fis, zaos);
                zaos.closeArchiveEntry();

                IOUtils.closeQuietly(fis);
            } catch (Exception e) {
                log.error("文件压缩异常", e);
            }
        }
    }

    /**
     * @param zipFileName
     * @param file
     * @param deleteSrcFileFlag
     */
    public static void zip(String zipFileName, File file, boolean deleteSrcFileFlag) {
        if (file == null) {
            return;
        }

        InputStream is = null;
        ZipArchiveOutputStream zos = null;
        try {
            zos = new ZipArchiveOutputStream(new File(zipFileName));
            zos.setUseZip64(Zip64Mode.AsNeeded);
            // 将文件用ZipArchiveEntry封装，使用ZipArchiveOutputStream写到压缩文件
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
            zos.putArchiveEntry(zipArchiveEntry);
            zos.setEncoding("UTF-8");

            is = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 5];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                // 把缓冲区的字节写入到ZipArchiveEntry
                zos.write(buffer, 0, len);
            }
            // 在此处关闭inputStream流。如果在finally块中关闭流，则只会关闭最后一个流对象
            IOUtils.closeQuietly(is);

            zos.closeArchiveEntry();
            zos.flush();
            zos.finish();
        } catch (Exception e) {
            log.error("文件压缩异常：{}", e);
        } finally {
            IOUtils.closeQuietly(is, zos);
            if (deleteSrcFileFlag) {
                FileUtil.deleteFile(file);
            }
        }
    }

    public static void main(String[] args) {
        zipDirWithPath("D:\\ddd", "D:\\ttt1.zip", false);
    }

}
