/*
 * Copyright 2013 Unicorn Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metaframe.unicorn.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * JVM系统属性配置数据来源。
 * <p/>
 * JVM系统属性，即启动JVM的-D选项。
 *
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 * @see System#getProperties()
 */
public class ClasspathPropertyConfigSource extends AbstractConfigSource {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathPropertyConfigSource.class);

    private final Properties properties;

    public ClasspathPropertyConfigSource(String fileName) {
        properties = loadProperties(fileName, true, true);
    }


    public static Properties loadProperties(String fileName, boolean allowMultiFile, boolean optional) {
        Properties properties = new Properties();
        List<URL> list = new ArrayList<URL>();

        try {
            Enumeration<URL> urls = ClasspathPropertyConfigSource.class.getClassLoader().getResources(fileName);
            list = new ArrayList<java.net.URL>();
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
        } catch (Throwable t) {
            logger.warn("Fail to load " + fileName + " file: " + t.getMessage(), t);
        }

        if (list.size() == 0) {
            if (!optional) {
                String errMsg = "No " + fileName + " found on the class path";
                logger.warn(errMsg);
                throw new IllegalStateException(errMsg);
            }
            return properties;
        }

        if (!allowMultiFile) {
            if (list.size() > 1) {
                String errMsg = String.format("only 1 %s file is expected, but %d dubbo.properties files found on class path: %s",
                        fileName, list.size(), list.toString());
                logger.warn(errMsg);
                throw new IllegalStateException(errMsg); // see http://code.alibabatech.com/jira/browse/DUBBO-133
            }
        }

        logger.info("load " + fileName + " properties file from " + list);

        for (java.net.URL url : list) {
            try {
                Properties p = new Properties();
                InputStream input = url.openStream();
                if (input != null) {
                    try {
                        p.load(input);
                        properties.putAll(p);
                    } finally {
                        try {
                            input.close();
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                }
            } catch (Throwable e) {
                logger.warn("Fail to load " + fileName + " file from " + url + "(ignore this file): " + e.getMessage(), e);
            }
        }

        return properties;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    @Override
    public boolean contains(String key) {
        return properties.containsKey(key);
    }
}
