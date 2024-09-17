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
package org.noear.solon.serialization.protostuff;

import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.2
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(AppContext context) {
        ProtostuffRender render = new ProtostuffRender();
        context.wrapAndPut(ProtostuffRender.class, render); //用于扩展
        Solon.app().renderManager().register("@protobuf",render);

        //支持 protostuff 内容类型执行
        ProtostuffActionExecutor executor = new ProtostuffActionExecutor();
        context.wrapAndPut(ProtostuffActionExecutor.class, executor); //用于扩展
        Solon.app().chainManager().addExecuteHandler(executor);
    }
}
