package com.wft.util;

import com.wft.vo.CompareResult;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WarDiff {
    protected final static Logger log = Logger.getLogger(WarDiff.class);

    JarFile[] wars = new JarFile[2];
    Set<String> ignoreFileSet = new HashSet<String>(16);

    public WarDiff(String file1, String file2) throws IOException {
        wars[0] = new JarFile(file1);
        wars[1] = new JarFile(file2);
    }

    public WarDiff(String file1, String file2, List<String> ignoreList) throws IOException {
        wars[0] = new JarFile(file1);
        wars[1] = new JarFile(file2);

        if (ignoreList != null && ignoreList.size() > 0) {
            ignoreFileSet.addAll(ignoreList);
        }
    }

    /**
     * Remove the CRC which is after the last colon
     *
     * @param codes
     * @return
     */
    public List<String> stripCRCs(List<String> codes) {
        List<String> result = new ArrayList<String>();
        for (String s: codes) {
            result.add(s.substring(0, s.lastIndexOf(':')));
        }
        return result;
    }

    /**
     * Extract an embedded jar file so that it can be scanned.
     *
     * @param jar
     * @param jarEntry
     * @param prefix
     * @return
     * @throws IOException
     */
    public List<String> dumpEmbeddedJar(JarFile jar, JarEntry jarEntry, String prefix) throws IOException, ExecutionException, InterruptedException {
        prefix += jarEntry.getName() + ":";

        File tmpFile = File.createTempFile("JAR", "jar");
        byte[] buffer = new byte[4096];
        InputStream is = jar.getInputStream(jarEntry);
        OutputStream os = new FileOutputStream(tmpFile);
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            os.write(buffer, 0, length);
        }

        IOUtils.closeQuietly(os, is);

        return dumpJar(new JarFile(tmpFile), prefix);

    }

    /**
     * Scan a jar file. If it is an embedded jar then pass a prefix.
     *
     * @param jar
     * @param prefix
     * @return
     */
    public List<String> dumpJar(JarFile jar, String prefix) throws ExecutionException, InterruptedException {
        List<String> crcList = FileCRCUtil.getCRC(Collections.list(jar.entries()), prefix, ignoreFileSet);
        closeStrem(jar);
        return crcList;
    }

    /**
     * Compare two ear files.
     *
     * @return true if files are identical
     */
    public CompareResult compare() {
        List<String> fileStringList1 = null;
        List<String> fileStringList2 = null;
        try {
            fileStringList1 = dumpJar(wars[0], "");
            fileStringList2 = dumpJar(wars[1], "");
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.error("war包对比异常：{}", e);
            return CompareResult.failed();
        } catch (InterruptedException e) {
            log.error("war包对比异常：{}", e);
            return CompareResult.failed();
        } finally {
            closeStrem(wars);
        }

        // Throw away codes that have identical filenames and CRCs.
        List<String> identical = new ArrayList<String>(fileStringList1);
        identical.retainAll(fileStringList2);
        fileStringList1.removeAll(identical);
        fileStringList2.removeAll(identical);

        // Remove the CRCs from the files that are left
        fileStringList1 = stripCRCs(fileStringList1);
        fileStringList2 = stripCRCs(fileStringList2);

        // If they are in both wars then the file has been changed.
        // Otherwise it has been added/removed
        List<String> changed = new ArrayList<String>(fileStringList1);
        changed.retainAll(fileStringList2);
        fileStringList1.removeAll(changed);
        fileStringList2.removeAll(changed);

        CompareResult result = new CompareResult();
        if (fileStringList1.size() + fileStringList2.size() + changed.size() == 0) {
            result.setSuccess(true);
            result.setSame(true);
        } else {
            result.setSuccess(true);
            result.setSame(false);
            result.setAddedFileList(fileStringList2);
            result.setModifiedFileList(changed);
            result.setDeletedFileList(fileStringList1);
        }

        return result;
    }

    private void closeStrem(JarFile...jarFiles) {
        for (JarFile jarFile : jarFiles) {
            try {
                jarFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("关闭Zip流异常：{}", e);
            }
        }
    }

}