package com.examle.core.common.utils;

import com.examle.core.common.io.UnsafeStringWriter;
import org.springframework.util.CollectionUtils;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String EMPTY = "";
    private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)");
    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String camelToSplitName(String camelName, String split) {
        if (isEmpty(camelName)) {
            return camelName;
        }
        StringBuilder buf = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (buf == null) {
                    buf = new StringBuilder();
                    if (i > 0) {
                        buf.append(camelName.substring(0, i));
                    }
                }
                if (i > 0) {
                    buf.append(split);
                }
                buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        return buf == null ? camelName : buf.toString();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String join(Collection<String> coll, String split) {
        if (CollectionUtils.isEmpty(coll)) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String s : coll) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(split);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String toString(Throwable e) {
        UnsafeStringWriter w = new UnsafeStringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName());
        if (e.getMessage() != null) {
            p.print(": " + e.getMessage());
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    public static boolean isInteger(String str) {
        return isNotEmpty(str) && INT_PATTERN.matcher(str).matches();
    }

    public static int parseInteger(String str) {
        return isInteger(str) ? Integer.parseInt(str) : 0;
    }

    public static String toQueryString(Map<String, String> ps) {
        StringBuilder buf = new StringBuilder();
        if (ps != null && ps.size() > 0) {
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(ps).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (isNoneEmpty(key, value)) {
                    if (buf.length() > 0) {
                        buf.append("&");
                    }
                    buf.append(key);
                    buf.append("=");
                    buf.append(value);
                }
            }
        }
        return buf.toString();
    }

    public static boolean isNoneEmpty(final String... ss) {
        if (ArrayUtils.isEmpty(ss)) {
            return false;
        }
        for (final String s : ss){
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将查询字符串解析为参数
     * @param qs
     * @return
     */
    public static Map<String, String> parseQueryString(String qs) {
        if (isEmpty(qs)) {
            return new HashMap<String, String>();
        }
        return parseKeyValuePair(qs, "\\&");
    }

    private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
        String[] tmp = str.split(itemSeparator);
        Map<String, String> map = new HashMap<String, String>(tmp.length);
        for (int i = 0; i < tmp.length; i++) {
            Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
            if (!matcher.matches()) {
                continue;
            }
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }
}
