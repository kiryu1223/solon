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
package org.noear.solon.web.cors;

import org.noear.solon.core.handle.*;
import org.noear.solon.core.route.PathRule;

/**
 * 跨域处理
 *
 * @author noear
 * @since 1.9
 */
public class CrossFilter extends AbstractCross<CrossFilter> implements Filter {
    private PathRule pathRule;

    /**
     * 设置路径匹配模式
     *
     * @since 3.0
     */
    public CrossFilter pathPatterns(String... patterns) {
        this.pathRule = new PathRule().include(patterns);
        return this;
    }

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if (pathRule == null || pathRule.test(ctx.pathNew())) {
            doHandle(ctx);
        }

        if (ctx.getHandled() == false) {
            chain.doFilter(ctx);
        }
    }
}
