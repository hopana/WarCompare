package com.wft.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.jar.JarEntry;

/**
 * Future导出任务类
 *
 * @author hupan
 * @since 2018-06-20 17:15
 */
public class FileCRCUtil {
    protected final static Logger log = Logger.getLogger(FileCRCUtil.class);

    public static List<String> getCRC(List<JarEntry> jarEntries, final String prefix, Set<String> ignoreList) throws ExecutionException, InterruptedException {
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        List<String> crcList = new ArrayList<String>(jarEntries.size());

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(20, new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

        for (final JarEntry entry : jarEntries) {
            // 只计算文件CRC，不计算目录
            if (!entry.isDirectory() && !ignoreList.contains("*" + entry.getName().substring(entry.getName().lastIndexOf(".")))) {
                Future<String> future = executorService.submit(() -> prefix + entry.getName() + ":" + entry.getCrc());
                futureList.add(future);
            }
        }

        for (Future<String> future : futureList) {
            crcList.add(future.get());
        }

        return crcList;
    }

}
