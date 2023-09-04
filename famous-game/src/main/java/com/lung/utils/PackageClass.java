package com.lung.utils;


import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class PackageClass {
    private static String[] PATHS;
    private static List<String> ignoreClass = new ArrayList<>();

    static {
        initPaths();
    }


    public static Set<Class<?>> find(Collection<String> packageNames) {
        Set<Class<?>> result = new HashSet<>();

        for (String p : packageNames) {
            result.addAll(find(p));
        }

        return result;
    }

    public static Set<Class<?>> find(String packageName) {
        try {
            Set<Class<?>> result = new HashSet<>();
            List<File> files = findFiles(packageName);
            for (File file : files) {
                String fileName = file.getName();
                if (file.isDirectory()) {
                    String pack;
                    if (packageName == null || packageName.length() == 0) {
                        pack = fileName;
                    } else {
                        pack = packageName + "." + fileName;
                    }

                    Set<Class<?>> r = find(pack);
                    result.addAll(r);
                }
                if (file.isFile()) {
                    String classSimpleName = fileName.substring(0, fileName.lastIndexOf('.'));
                    String className = packageName + "." + classSimpleName;

                    if (ignoreClass.contains(className)) {
                        continue;
                    }
                    Class<?> clazz = Class.forName(className);
                    result.add(clazz);
                }
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    public static void ignoreClass(List<Class<?>> ignoreClass) {
        PackageClass.ignoreClass.clear();
        for (Class<?> c : ignoreClass) {
            PackageClass.ignoreClass.add(c.getName());
        }
    }

    private static List<File> findFiles(String packageName) {
        List<File> result = new ArrayList<>();
        for (String path : PATHS) {
            File dir = new File(path, packageToPath(packageName));

            File[] files = dir.listFiles(pathname -> {
                if (pathname.isDirectory()) {
                    return true;
                }
                return pathname.getName().matches(".*\\.class$");
            });

            if (files == null) {
                files = new File[0];
            }

            for (File f : files) {
                result.add(f);
            }
        }

        return result;
    }

    private static void initPaths() {
        try {
            String pathStr = System.getProperty("java.class.path");
            String os = System.getProperty("os.name");
            if (os != null && os.startsWith("Windows")) {
                PATHS = pathStr.split(";");
            } else {
                PATHS = pathStr.split(":");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Class<?>> getPackageClasses(String pname) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
//        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        ClassLoader cl = PackageClass.class.getClassLoader();
        String packageDirName = pname.replace('.', '/');

        try {
            Enumeration<URL> dirs = cl.getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    findByFile(cl, pname, URLDecoder.decode(url.getFile(), "utf-8"), classes);
                } else if ("jar".equals(protocol)) {
                    findInJar(cl, pname, packageDirName, url, classes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static void findByFile(ClassLoader cl, String packageName, String filePath, Set<Class<?>> classes) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory())
            return;

        File[] dirFiles = dir.listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findByFile(cl, packageName + "." + file.getName(), file.getAbsolutePath(), classes);
            } else {
                try {
                    String className = packageName + "."
                            + file.getName().substring(0, file.getName().length() - 6);
//                    Class<?> clazz = Class.forName(className);
                    Class<?> clazz = PackageClass.class.getClassLoader().loadClass(className);
                    classes.add(clazz);
                } catch (ExceptionInInitializerError e) {
                } catch (NoClassDefFoundError e) {
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void findInJar(ClassLoader cl, String pname, String packageDirName, URL url, Set<Class<?>> classes) {
        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.isDirectory())
                    continue;

                String name = entry.getName();

                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }

                if (name.startsWith(packageDirName) && name.contains("/") && name.endsWith(".class")) {
                    name = name.substring(0, name.length() - 6).replace('/', '.');
                    try {
                        Class<?> clazz = cl.loadClass(name);
                        classes.add(clazz);
                    } catch (Throwable e) {
                        System.out.println("无法直接加载的类：" + name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}