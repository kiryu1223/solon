/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.solon.data.sql;


import java.util.Map;

/**
 * 行
 *
 * @author noear
 * @since 3.0
 */
public interface Row {
    /**
     * 行大小
     */
    int size();

    /**
     * 行数据
     */
    Object[] data();

    /**
     * 获取名字
     *
     * @param columnIdx 列顺位（从1开始）
     */
    String getName(int columnIdx);

    /**
     * 获取名字列顺位
     *
     * @param name 名字
     */
    int getNameColumnIdx(String name);

    /**
     * 获取值
     *
     * @param columnIdx 列顺位（从1开始）
     */
    Object getObject(int columnIdx);

    /**
     * 获取值
     *
     * @param name 名字
     */
    Object getObject(String name);

    /**
     * 转为 Map
     */
    Map<String, Object> toMap();

    /**
     * 转为 Bean
     *
     * @param type      类型
     * @param converter 转换器
     */
    <T> T toBean(Class<T> type, Row.Converter converter);

    /**
     * 转为 Bean
     *
     * @param type 类型
     */
    <T> T toBean(Class<T> type);

    /**
     * 行转换器
     */
    @FunctionalInterface
    static interface Converter {
        /**
         * 转换
         *
         * @param row  行
         * @param type 类型
         */
        Object convert(Row row, Class<?> type);
    }
}