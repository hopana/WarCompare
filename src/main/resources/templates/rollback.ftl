#!/bin/bash
# 回滚脚本

set -o errexit

warName=${warName}
fileDate=$(date "+%m%d")
bakFile=${r'${warName}'}Bak${r'${fileDate}'}.tar.gz

if [ -f ${r'${bakFile}'} ]; then
    echo "开始回滚...."
    echo "1.删除新增文件"
<#if addedFileList?? && addedFileList?size gt 0>
    <#list addedFileList as addedFile>
    echo "删除：${r'${warName}'}/${addedFile}"
    rm -rf ${r'${warName}'}/${addedFile}
    </#list>
</#if>
    echo "2.回滚备份文件"
    tar -xzvf ${r'${bakFile}'}
    echo "回滚完毕...."
else
    echo "备份文件不存在：${r'${bakFile}'}"
fi

set +o errexit