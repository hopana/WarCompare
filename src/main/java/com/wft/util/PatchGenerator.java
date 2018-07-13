package com.wft.util;

import com.wft.vo.CompareResult;
import com.wft.vo.ResultVo;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 补丁包生成工具
 *
 * @author hupan
 * @date 2018/07/04
 */
public class PatchGenerator {
    private final static Logger log = Logger.getLogger(PatchGenerator.class);
    private final static String FILE_SEPERATOR = System.getProperty("file.separator");
    private final static String PATCH_CONTENT_DIR = "ROOT";
    private final static String FINAL_PATH_EXT = ".zip";
    private final static String BACKUP_TEMPLATE_NAME = "backup.ftl";
    private final static String ROLLBACK_TEMPLATE_NAME = "rollback.ftl";
    private final static String APPLY_TEMPLATE_NAME = "apply.ftl";


    private CompareResult result;
    private String patchTemplateDir;

    public PatchGenerator(CompareResult result, String patchTemplateDir) {
        this.result = result;
        this.patchTemplateDir = patchTemplateDir;
    }

    public ResultVo generate() {
        if (result.isSame()) {
            return ResultVo.fail("war包相同，不需要生成补丁包");
        }

        if (StringUtils.isBlank(patchTemplateDir)) {
            return ResultVo.fail("补丁模板文件路径为空");
        }

        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"), DateUtils.getCurrentDate() + "_PATCH" + "_" + System.currentTimeMillis() + FILE_SEPERATOR + getFinalPatchName(result.getProductionWarName()));
            tempDir.mkdirs();
            File readmeTemplate = new File(patchTemplateDir, "README.txt");
            // 创建ROOT目录
            File root = new File(tempDir, PATCH_CONTENT_DIR);
            // 生成README文件
            File readme = new File(tempDir + FILE_SEPERATOR + "README.txt");
            FileUtil.copyFile(readmeTemplate, readme);

            List<String> deletedFileList = result.getDeletedFileList();
            List<String> addedFileList = result.getAddedFileList();
            List<String> modifiedFileList = result.getModifiedFileList();

            List<String> backFileList = new ArrayList<String>(deletedFileList.size() + modifiedFileList.size());
            backFileList.addAll(deletedFileList);
            backFileList.addAll(modifiedFileList);
            Collections.sort(backFileList);

            List<String> patchFileList = new ArrayList<String>(modifiedFileList.size() + addedFileList.size());
            patchFileList.addAll(modifiedFileList);
            patchFileList.addAll(addedFileList);
            Collections.sort(patchFileList);

            createPatch(root, patchFileList);
            createBackupShell(root, backFileList);
            createRollbackShell(root, addedFileList);
            createApplyPatchShell(root, deletedFileList);

            String finalPatchFile = root.getParentFile().getParent() + FILE_SEPERATOR + getFinalPatchName(result.getProductionWarName()) + FINAL_PATH_EXT;
            CompressUtil.zipDirWithPath(root.getParent(), finalPatchFile, true);
            return ResultVo.success("补丁生成成功", finalPatchFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("补丁文件生成异常：{}", e);
            return ResultVo.fail("补丁生成异常");
        }
    }

    public void createPatch(File parentRootFile, List<String> patchFileList) {
        File patchTarGzDir = new File(parentRootFile, PATCH_CONTENT_DIR);
        patchTarGzDir.mkdirs();

        try {
            for (String patchFile : patchFileList) {
                File file = new File(patchTarGzDir, patchFile);
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("创建补丁异常：{}", e);
        }

        try {
            File patchTarFile = new File(parentRootFile.getAbsolutePath() + FILE_SEPERATOR + getFinalPatchName(result.getProductionWarName()) + ".tar");
            CompressUtil.tar(patchTarFile.getAbsolutePath(), true, patchTarGzDir);
            CompressUtil.gz(patchTarFile, "", true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成补丁包异常：{}", e);
        }
    }

    public void createBackupShell(File parentRootFile, List<String> backFileList) {
        File backupShellFile = new File(parentRootFile, "tarROOTBak.sh");
        Map<String, Object> dataMap = new HashMap<String, Object>(2);
        dataMap.put("warName", result.getProductionWarName());
        dataMap.put("backFileList", backFileList);

        try {
            FreeMarkerUtil.process(patchTemplateDir, BACKUP_TEMPLATE_NAME, backupShellFile.getAbsolutePath(), dataMap);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成备份脚本异常：{}", e);
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("生成备份脚本异常：{}", e);
        }
    }

    public void createRollbackShell(File parentRootFile, List<String> addedFileList) {
        File rollbackShellFile = new File(parentRootFile, "rollbackROOT.sh");
        Map<String, Object> dataMap = new HashMap<String, Object>(2);
        dataMap.put("warName", result.getProductionWarName());
        dataMap.put("addedFileList", addedFileList);

        try {
            FreeMarkerUtil.process(patchTemplateDir, ROLLBACK_TEMPLATE_NAME, rollbackShellFile.getAbsolutePath(), dataMap);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成回滚脚本异常：{}", e);
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("生成回滚脚本异常：{}", e);
        }
    }

    public void createApplyPatchShell(File parentRootFile, List<String> deletedFileList) {
        File rapplyPatchShellFile = new File(parentRootFile, "applyPatch.sh");
        Map<String, Object> dataMap = new HashMap<String, Object>(2);
        dataMap.put("warName", result.getProductionWarName());
        dataMap.put("deletedFileList", deletedFileList);

        try {
            FreeMarkerUtil.process(patchTemplateDir, APPLY_TEMPLATE_NAME, rapplyPatchShellFile.getAbsolutePath(), dataMap);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成补丁脚本异常：{}", e);
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("生成补丁脚本异常：{}", e);
        }
    }

    private static String getFinalPatchName(String finalWarName) {
        return finalWarName + "_" + "Patch" + DateUtils.getCurrentMonthDay();
    }

    public CompareResult getResult() {
        return result;
    }

    public void setResult(CompareResult result) {
        this.result = result;
    }

    public String getPatchTemplateDir() {
        return patchTemplateDir;
    }

    public void setPatchTemplateDir(String patchTemplateDir) {
        this.patchTemplateDir = patchTemplateDir;
    }

    public static void main(String[] args) throws Exception {

    }

}
