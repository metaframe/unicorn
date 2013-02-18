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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 把字符串和{@link Map}转成{@link com.metaframe.unicorn.ConfigSource}。
 *
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 */
public class CommonConfigSource extends AbstractConfigSource {

    private final Map<String, String> configs;

    private CommonConfigSource(Map<String, String> configs, boolean deepCopy) {
        if (deepCopy) {
            this.configs = new HashMap<String, String>(configs.size());
            for (Map.Entry<String, String> c : configs.entrySet()) {
                this.configs.put(c.getKey(), c.getValue());
            }
        } else {
            this.configs = configs;
        }
    }

    private static final Pattern PAIR_SEPARATOR = Pattern.compile("\\s*[&]\\s*");
    private static final Pattern KV_SEPARATOR = Pattern.compile("\\s*[=]\\s*");

    /**
     * 把字符串转成{@link CommonConfigSource}。
     * <p/>
     * 字符串的格式是<code>key1=value1&key2=value2</code>.
     *
     * @param configString 配置字符串.
     * @since 0.1.0
     */
    public static CommonConfigSource fromString(String configString) {
        if (configString == null || (configString = configString.trim()).length() == 0) {
            return new CommonConfigSource(new HashMap<String, String>(0), false);
        }

        HashMap<String, String> cs = new HashMap<String, String>();
        String[] pairs = PAIR_SEPARATOR.split(configString);
        for (String pair : pairs) {
            if (pair.length() == 0) continue;

            String[] kv = KV_SEPARATOR.split(pair);
            switch (kv.length) {
                case 1:
                    cs.put(kv[0], "");
                    break;
                case 2:
                    cs.put(kv[0], kv[1]);
                    break;
                default:
                    throw new IllegalArgumentException("input config(" + configString +
                            ") is illegal: key(" + kv[0] + ") has more than 1 value!");
            }
        }

        return new CommonConfigSource(cs, false);
    }

    /**
     * 把{@link Map}转成{@link CommonConfigSource}。
     *
     * @since 0.1.0
     */
    public static CommonConfigSource fromMap(Map<String, String> configs) {
        return new CommonConfigSource(configs, true);
    }

    static Map<String, String> kv2Map(String... kv) {
        Map<String, String> cs = new HashMap<String, String>();

        for (int i = 0; i < kv.length; i += 2) {
            String key = kv[i];
            if (key == null) {
                throw new IllegalArgumentException("Key must not null!");
            }
            if (i + 1 < kv.length) {
                cs.put(key, kv[i + 1]);
            } else {
                cs.put(key, null);
            }
        }

        return cs;
    }

    /**
     * 把方法参数两两组合作为Key-Value转成{@link CommonConfigSource}。
     * 如果方法参数个数是奇数，作为最后一个参数作为Key，对应的Value是<code>null</code>。
     *
     * @since 0.1.0
     */
    public static CommonConfigSource fromKv(String... kvPairs) {
        return new CommonConfigSource(kv2Map(kvPairs), false);
    }

    /**
     * 在{@link CommonConfigSource}加入新参数。
     *
     * @since 0.1.0
     */
    public CommonConfigSource addConfig(String... kvPairs) {
        Map<String, String> cs = new HashMap<String, String>(this.configs);
        cs.putAll(kv2Map(kvPairs));
        return new CommonConfigSource(cs, false);
    }

    /**
     * 在{@link CommonConfigSource}加入新参数。
     *
     * @since 0.1.0
     */
    public CommonConfigSource addConfig(Map<String, String> configs) {
        Map<String, String> cs = new HashMap<String, String>(this.configs);
        cs.putAll(configs);
        return new CommonConfigSource(cs, false);
    }

    /**
     * {@link CommonConfigSource}转成{@link Map}。
     *
     * @since 0.1.0
     */
    public Map<String, String> toMap() {
        return new HashMap<String, String>(configs);
    }

    private transient volatile String toString;

    /**
     * {@link CommonConfigSource}转成字符串。
     */
    @Override
    public String toString() {
        if (toString != null) return toString;

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> c : configs.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }

            sb.append(c.getKey().trim());

            String value = c.getValue();
            if (value != null && (value = value.trim()).length() > 0) {
                sb.append("=").append(value);
            }
        }
        return toString = sb.toString();
    }

    /**
     * @since 0.1.0
     */
    public boolean contains(String key) {
        return configs.containsKey(key);
    }

    /**
     * @since 0.1.0
     */
    public String get(String key) {
        return configs.get(key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configs == null) ? 0 : configs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommonConfigSource other = (CommonConfigSource) obj;
        if (configs == null) {
            if (other.configs != null)
                return false;
        } else if (!configs.equals(other.configs))
            return false;
        return true;
    }
}
