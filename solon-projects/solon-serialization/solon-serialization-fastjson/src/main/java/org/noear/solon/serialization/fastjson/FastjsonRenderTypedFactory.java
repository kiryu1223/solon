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
package org.noear.solon.serialization.fastjson;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.noear.solon.core.handle.Render;
import org.noear.solon.serialization.StringSerializerRender;

/**
 * Json 类型化渲染器工厂
 *
 * @author noear
 * @since 1.5
 * @since 2.8
 */
public class FastjsonRenderTypedFactory extends FastjsonRenderFactoryBase {
    private final FastjsonStringSerializer serializer = new FastjsonStringSerializer();

    public FastjsonRenderTypedFactory() {
        serializer.cfgSerializerFeatures(false, true,
                SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteClassName,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String[] mappings() {
        return new String[]{"@type_json"};
    }

    @Override
    public Render create() {
        return new StringSerializerRender(true, serializer);
    }

    @Override
    public SerializeConfig config() {
        return serializer.getSerializeConfig();
    }
}