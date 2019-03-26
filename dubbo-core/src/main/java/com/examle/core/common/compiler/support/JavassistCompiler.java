package com.examle.core.common.compiler.support;

import com.examle.core.common.utils.ClassHelper;
import javassist.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavassistCompiler extends AbstractCompiler {

    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w\\.\\*]+);\n");

    private static final Pattern EXTENDS_PATTERN = Pattern.compile("\\s+extends\\s+([\\w\\.]+)[^\\{]*\\{\n");

    private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile("\\s+implements\\s+([\\w\\.]+)\\s*\\{\n");

    private static final Pattern METHODS_PATTERN = Pattern.compile("\n(private|public|protected)\\s+");

    private static final Pattern FIELD_PATTERN = Pattern.compile("[^\n]+=[^\n]+;");

    @Override
    protected Class<?> doCompile(String name, String source) throws Throwable {
        int i = name.lastIndexOf('.');
        String className = i < 0 ? name : name.substring(i + 1);
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new LoaderClassPath(ClassHelper.getCallerClassLoader(getClass())));
        Matcher matcher = IMPORT_PATTERN.matcher(source);
        List<String> importPackages = new ArrayList<String>();
        Map<String, String> fullNames = new HashMap<String, String>();
        while (matcher.find()) {
            String pkg = matcher.group(1);
            if (pkg.endsWith(".*")) {
                //待写
            }else{
                int pi = pkg.lastIndexOf('.');
                if (pi > 0) {
                    String pkgName = pkg.substring(0, pi);
                    pool.importPackage(pkgName);
                    importPackages.add(pkgName);
                    fullNames.put(pkg.substring(pi + 1), pkg);
                }
            }
        }
        String[] packages = importPackages.toArray(new String[0]);
        matcher = EXTENDS_PATTERN.matcher(source);
        CtClass cls;
        if (matcher.find()) {
            cls = null;//待完成
        }else{
            cls = pool.makeClass(name);
        }
        matcher = IMPLEMENTS_PATTERN.matcher(source);
        if (matcher.find()) {
            String[] ifaces = matcher.group(1).trim().split("\\,");
            for (String iface : ifaces) {
                iface = iface.trim();
                String ifaceClass;
                if (iface.contains(".")) {
                    ifaceClass = iface;
                } else if (fullNames.containsKey(iface)) {
                    ifaceClass = fullNames.get(iface);
                } else {
//                    ifaceClass = ClassUtils.forName(packages, iface).getName();
                    ifaceClass = null;//待完成
                }
                cls.addInterface(pool.get(ifaceClass));
            }
        }
        String body = source.substring(source.indexOf("{") + 1, source.length() - 1);
        String[] methods = METHODS_PATTERN.split(body);
        for (String method : methods) {
            method = method.trim();
            if (method.length() > 0) {
                if (method.startsWith(className)) {
                    cls.addConstructor(CtNewConstructor.make("public " + method, cls));
                } else if (FIELD_PATTERN.matcher(method).matches()) {
                    cls.addField(CtField.make("private " + method, cls));
                } else {
                    cls.addMethod(CtNewMethod.make("public " + method, cls));
                }
            }
        }
        return cls.toClass(ClassHelper.getCallerClassLoader(getClass()), JavassistCompiler.class.getProtectionDomain());
    }
}
