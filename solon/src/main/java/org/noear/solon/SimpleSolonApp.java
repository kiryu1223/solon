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
package org.noear.solon;

import org.noear.solon.core.NvMap;
import org.noear.solon.core.util.ConsumerEx;

/**
 * 简单应用
 *
 * @author noear
 * @since 3.0
 */
public class SimpleSolonApp extends SolonApp {

    public SimpleSolonApp(Class<?> source, String... args) throws Exception {
        this(source, NvMap.from(args));
    }

    public SimpleSolonApp(Class<?> source, NvMap args) throws Exception {
        super(source, args);
    }

    private SolonApp bakApp;

    /**
     * 简单开始
     */
    public SimpleSolonApp startSimply(ConsumerEx<SolonApp> initialize) throws Throwable {
        //切换全局 app
        bakApp = Solon.app();
        Solon.appSet(this);

        super.start(initialize);
        return this;
    }

    /**
     * 简单停止（阻塞模式）
     */
    public void stopSimply() {
        try {
            super.prestop();
            super.stopping = true;
            super.stop();
        } finally {
            //恢复全局 app
            Solon.appSet(bakApp);
        }
    }
}