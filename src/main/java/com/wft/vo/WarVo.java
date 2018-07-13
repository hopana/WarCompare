package com.wft.vo;

import java.io.Serializable;

/**
 * War包
 *
 * @author hupan
 * @date 2018/07/03
 */
public class WarVo implements Serializable {
    private String oldWarPath;
    private String newWarPath;
    private String fileFilter;

    public String getOldWarPath() {
        return oldWarPath;
    }

    public void setOldWarPath(String oldWarPath) {
        this.oldWarPath = oldWarPath;
    }

    public String getNewWarPath() {
        return newWarPath;
    }

    public void setNewWarPath(String newWarPath) {
        this.newWarPath = newWarPath;
    }

    public String getFileFilter() {
        return fileFilter;
    }

    public void setFileFilter(String fileFilter) {
        this.fileFilter = fileFilter;
    }

    @Override
    public String toString() {
        return "WarVo [" + "oldWarPath=" + oldWarPath + ", newWarPath=" + newWarPath + ", fileFilter=" + fileFilter + "]";
    }
}
