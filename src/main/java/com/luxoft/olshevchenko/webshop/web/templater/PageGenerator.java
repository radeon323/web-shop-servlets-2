package com.luxoft.olshevchenko.webshop.web.templater;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;


public class PageGenerator {
    private static final String HTML_DIR = PropertiesReader.getProperties().getProperty("html_directory");
    private static final Configuration cfg = new Configuration();

    public static String getPage(String filename, Map<String, Object> data) {
        try {
            Writer stream = new StringWriter();
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
            return stream.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPage(String filename) {
        return getPage(filename, Collections.emptyMap());
    }


}
