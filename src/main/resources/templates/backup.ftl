#!/bin/bash
# 备份脚本

set -o errexit

warName=${warName}
fileDate=$(date "+%m%d")
echo "开始备份...."
tar -czvf ${r'${warName}'}Bak${r'${fileDate}'}.tar.gz \
<#if backFileList?? && backFileList?size gt 0>
    <#list backFileList as backFile>
        <#if backFile_has_next>
            ${r'${warName}'}/${backFile} \<#lt>
        <#else>
            ${r'${warName}'}/${backFile}<#lt>
        </#if>
    </#list>
</#if>
echo "备份完毕...."

set +o errexit