package com.wft.vo;

import java.io.Serializable;
import java.util.List;

/**
 * war包对比结果
 *
 * @author hupan
 * @date 2018/07/04
 */
public class CompareResult implements Serializable {
    private boolean success;
    private boolean same;
    private List<String> modifiedFileList;
    private List<String> addedFileList;
    private List<String> deletedFileList;
    private String productionWarName;

    public static CompareResult failed() {
        return new CompareResult(false, false, null, null, null, "");
    }

    public CompareResult() {
    }

    private CompareResult(boolean success, boolean same, List<String> modifiedFileList, List<String> addedFileList, List<String> deletedFileList, String productionWarName) {
        this.success = success;
        this.same = same;
        this.modifiedFileList = modifiedFileList;
        this.addedFileList = addedFileList;
        this.deletedFileList = deletedFileList;
        this.productionWarName = productionWarName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public List<String> getModifiedFileList() {
        return modifiedFileList;
    }

    public void setModifiedFileList(List<String> modifiedFileList) {
        this.modifiedFileList = modifiedFileList;
    }

    public List<String> getAddedFileList() {
        return addedFileList;
    }

    public void setAddedFileList(List<String> addedFileList) {
        this.addedFileList = addedFileList;
    }

    public List<String> getDeletedFileList() {
        return deletedFileList;
    }

    public void setDeletedFileList(List<String> deletedFileList) {
        this.deletedFileList = deletedFileList;
    }

    public String getProductionWarName() {
        return productionWarName;
    }

    public void setProductionWarName(String productionWarName) {
        this.productionWarName = productionWarName;
    }

    @Override
    public String toString() {
        return "CompareResult{" + "success=" + success + ", same=" + same + ", modifiedFileList=" + modifiedFileList + ", addedFileList=" + addedFileList + ", deletedFileList=" + deletedFileList + ", productionWarName='" + productionWarName + '\'' + '}';
    }
}
