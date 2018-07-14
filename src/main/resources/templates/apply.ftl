#!/usr/bin/env bash
# 补丁脚本

set -o errexit

warName=${warName}
fileDate=$(date "+%m%d")
patchName=${r'${warName}'}_Patch${r'${fileDate}'}.tar.gz

if [ -f ${r'${patchName}'} ]; then
    echo "开始应用补丁...."
    echo "1.移除已删除文件"
<#if deletedFileList?? && deletedFileList?size gt 0>
    <#list deletedFileList as deletedFile>
    echo "删除：${r'${warName}'}/${deletedFile}"
    rm -rf ${r'${warName}'}/${deletedFile}
    </#list>
</#if>
    echo "2.解压补丁包"
    tar -xzvf ${r'${patchName}'}
    echo "补丁应用成功"
else
    echo "补丁包不存在：${r'${patchName}'}"
fi

set +o errexit
