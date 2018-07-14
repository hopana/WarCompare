
一、升级步骤：

1，在服务器上依次执行以下操作
1）拷贝ROOT目录下的所有文件到/home/weixin/pro/tomcat7.0.10/webapps/ 目录下
2）备份
在/home/weixin/pro/tomcat7.0.10/webapps/目录下执行：sh bakcup.sh
3）停止tomcat服务
4）应用补丁
在/home/weixin/pro/tomcat7.0.10/webapps/目录下执行：sh applyPatch.sh
5）重启服务

2，回滚
在/home/weixin/pro/tomcat7.0.10/webapps/目录下执行：sh rollback.sh


