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

/**
 * JVM系统属性配置数据来源。
 * <p/>
 * JVM系统属性，即启动JVM的-D选项。
 *
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 * @see System#getProperties()
 */
public class JvmSystemPropertyConfigSource extends AbstractConfigSource {
    @Override
    public boolean contains(String key) {
        return System.getProperties().contains(key);
    }

    public String get(String key) {
        return System.getProperty(key);
    }
}
