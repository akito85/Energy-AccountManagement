package com.dbs.common.library.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlDecodeEncodeUtil {

    private UrlDecodeEncodeUtil() {}

    public static String encodePath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    public static String decodePath(String path) {
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }
}
