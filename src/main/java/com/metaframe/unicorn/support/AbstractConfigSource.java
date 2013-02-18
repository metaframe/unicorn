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

import com.metaframe.unicorn.ConfigSource;

/**
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 */
public abstract class AbstractConfigSource implements ConfigSource {
    /**
     * @since 0.1.0
     */
    public abstract boolean contains(String key);

    /**
     * @since 0.1.0
     */
    public abstract String get(String key);

    /**
     * 本地检查是否配置项。对于
     */
    protected boolean localContains(String key) {
        return contains(key);
    }

    /**
     * @since 0.1.0
     */
    public String get(String key, String defaultValue) {
        if (localContains(key)) {
            return get(key);
        } else {
            return defaultValue;
        }
    }

    // the util methods!

    /**
     * @since 0.1.0
     */
    public boolean getBoolean(String key) {
        return Boolean.valueOf(get(key));
    }

    /**
     * @since 0.1.0
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (localContains(key)) {
            return Boolean.valueOf(get(key));
        } else {
            return defaultValue;
        }
    }

    /**
     * @since 0.1.0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * @since 0.1.0
     */
    public int getInt(String key, int defaultValue) {
        if (localContains(key)) {
            return Integer.parseInt(get(key));
        } else {
            return defaultValue;
        }
    }

    /**
     * @since 0.1.0
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * @since 0.1.0
     */
    public long getLong(String key, long defaultValue) {
        if (localContains(key)) {
            return Long.parseLong(get(key));
        } else {
            return defaultValue;
        }
    }
}
