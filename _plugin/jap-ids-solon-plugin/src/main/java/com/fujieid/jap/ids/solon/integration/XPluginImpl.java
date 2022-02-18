package com.fujieid.jap.ids.solon.integration;

import com.fujieid.jap.ids.JapIds;
import com.fujieid.jap.ids.context.IdsContext;
import com.fujieid.jap.ids.solon.IdsProps;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.Plugin;
import com.fujieid.jap.ids.solon.http.ErrorFilter;
import com.fujieid.jap.ids.solon.http.controller.*;

/**
 * @author noear, 颖
 * @since 1.6
 */
public class XPluginImpl implements Plugin {

    @Override
    public void start(SolonApp app) {

        // 不实现和注入任何 Service, 因为 Jap Ids 会调用
        // ServiceLoader.load 方法, 这样方便用户实现后自动注入
        // 还能很好地避免冲突
        IdsContext context = new IdsContext()
                .setIdsConfig(IdsProps.idsConfig);

        JapIds.registerContext(context);

        Aop.wrapAndPut(IdsContext.class, context);

        //添加控制器
        app.add(IdsProps.bastPath, ApprovalController.class);
        app.add(IdsProps.bastPath, AuthorizationController.class);
        app.add(IdsProps.bastPath, CheckSessionController.class);
        app.add(IdsProps.bastPath, ErrorController.class);
        app.add(IdsProps.bastPath, LoginController.class);
        app.add(IdsProps.bastPath, LogoutController.class);
        app.add(IdsProps.bastPath, TokenController.class);
        app.add(IdsProps.bastPath, UserController.class);

        app.add(IdsProps.wellPath, DiscoveryController.class);

        //添加过滤器
        app.filter(new ErrorFilter());
    }

}
