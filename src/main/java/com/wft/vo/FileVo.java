package com.wft.vo;

/**
 * 文件VO
 *
 * @author hupan
 * @date 2018/07/13
 */
public class FileVo {
    private String fileName;
    private String filePath;
    private String fileStatus;

    public FileVo(String fileName, String filePath, String fileStatus) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileStatus = fileStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    @Override
    public String toString() {
        return "FileVo [" + "fileName=" + fileName + ", filePath=" + filePath + ", fileStatus=" + fileStatus + "]";
    }
}
