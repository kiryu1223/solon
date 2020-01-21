package org.noear.solon.core;


import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class XClassLoader extends URLClassLoader {

    private static XClassLoader _global = new XClassLoader();

    public static XClassLoader global() {
        return _global;
    }

    private Map<URL, JarURLConnection> cachedMap = new HashMap<>();

    protected XClassLoader() {
        super(new URL[]{}, ClassLoader.getSystemClassLoader());
    }

    /**
     * 加载jar包
     */
    public void loadJar(URL file) {
        try {
            // 打开并缓存文件url连接
            URLConnection uc = file.openConnection();
            if (uc instanceof JarURLConnection) {
                uc.setUseCaches(true);
                ((JarURLConnection) uc).getManifest();
                cachedMap.put(file, (JarURLConnection) uc);
            }
        } catch (Exception e) {
            System.err.println("Failed to cache plugin JAR file: " + file.toExternalForm());
        }
        addURL(file);
    }

    /**
     * 卸载jar包
     * */
    public void unloadJar(URL file) {
        JarURLConnection jarURL = cachedMap.get(file);

        if (jarURL == null) {
            return;
        }

        try {
            jarURL.getJarFile().close();
            jarURL = null;
            cachedMap.remove(file);
            System.gc();
        } catch (Exception e) {
            System.err.println("Failed to unload JAR file\n" + e);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    /**
     * 绑定线程
     * */
    public static void bindingThread() {
        Thread.currentThread().setContextClassLoader(global());
    }
}
