package com.wft.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * FreeMarker文件生成工具类
 *
 * @author hupan
 * @since 2017-03-20 17:00
 */
public class FreeMarkerUtil {
    private final static String DEFAULT_TEMPLATE_PATH = "/patch/";

    /**
     * 获取Template对象
     *
     * @param templateName 模版名称（不带路径）
     * @return Template对象
     * @throws IOException 抛出IOException
     */
    public static Template getTemplate(String templatePath, String templateName) throws IOException {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_23);
        conf.setDefaultEncoding("UTF-8");
        conf.setDirectoryForTemplateLoading(new File(templatePath));

        return conf.getTemplate(templateName);
    }

    public static void process(String templateName, String fileName, Map<String, Object> dataModel) throws IOException, TemplateException {
        URL patchUrl = FreeMarkerUtil.class.getResource(DEFAULT_TEMPLATE_PATH);
        Template template = getTemplate(patchUrl.getPath(), templateName);

        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        template.process(dataModel, writer);
        writer.flush();
        writer.close();
    }

    public static void process(String templatePath, String templateName, String fileName, Map<String, Object> dataModel) throws IOException, TemplateException {
        Template template = getTemplate(templatePath, templateName);

        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        template.process(dataModel, writer);
        writer.flush();
        writer.close();
    }

}
