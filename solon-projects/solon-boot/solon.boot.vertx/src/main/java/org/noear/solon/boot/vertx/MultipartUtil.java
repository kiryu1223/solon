package org.noear.solon.boot.vertx;

import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.http.HttpPartFile;
import org.noear.solon.boot.io.LimitedInputException;
import org.noear.solon.boot.io.LimitedInputStream;
import org.noear.solon.boot.vertx.uploadfile.HttpMultipart;
import org.noear.solon.boot.vertx.uploadfile.HttpMultipartCollection;
import org.noear.solon.core.exception.StatusException;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author noear
 * @since 2.9
 */
public class MultipartUtil {
    public static void buildParamsAndFiles(Context ctx, Map<String, List<UploadedFile>> filesMap) {
        try {
            HttpMultipartCollection parts = new HttpMultipartCollection(ctx.contentType(), ctx.bodyAsStream());

            while (parts.hasNext()) {
                HttpMultipart part = parts.next();
                String name = ServerProps.urlDecode(part.getName());

                if (isFile(part)) {
                    doBuildFiles(name, filesMap, part);
                } else {
                    ctx.paramSet(name, part.getString());
                }
            }
        } catch (Exception e) {
            throw status4xx(ctx, e);
        }
    }

    private static void doBuildFiles(String name, Map<String, List<UploadedFile>> filesMap, HttpMultipart part) throws IOException {
        List<UploadedFile> list = filesMap.get(name);
        if (list == null) {
            list = new ArrayList<>();
            filesMap.put(name, list);
        }

        String contentType = part.getHeaders().get("Content-Type");
        String filename = part.getFilename();
        String extension = null;
        int idx = filename.lastIndexOf(".");
        if (idx > 0) {
            extension = filename.substring(idx + 1);
        }

        HttpPartFile partFile = new HttpPartFile(filename, new LimitedInputStream(part.getBody(), ServerProps.request_maxFileSize));
        UploadedFile f1 = new UploadedFile(partFile::delete, contentType, partFile.getSize(), partFile.getContent(), filename, extension);

        list.add(f1);
    }

    private static boolean isField(HttpMultipart filePart) {
        return filePart.getFilename() == null;
    }

    private static boolean isFile(HttpMultipart filePart) {
        return !isField(filePart);
    }


    //////////////////////

    public static StatusException status4xx(Context ctx, Exception e) {
        if (e instanceof StatusException) {
            return (StatusException) e;
        } else {
            if (isBodyLargerEx(e)) {
                return new StatusException("Request Entity Too Large: " + ctx.method() + " " + ctx.pathNew(), e, 413);
            } else {
                return new StatusException("Bad Request:" + ctx.method() + " " + ctx.pathNew(), e, 400);
            }
        }
    }

    /**
     * 是否为 body larger ex?
     */
    public static boolean isBodyLargerEx(Throwable e) {
        return hasLargerStr(e) || hasLargerStr(e.getCause());
    }

    private static boolean hasLargerStr(Throwable e) {
        if (e instanceof LimitedInputException) {
            return true;
        }

        return false;
    }
}
