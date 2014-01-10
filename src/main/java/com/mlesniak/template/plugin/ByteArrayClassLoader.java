package com.mlesniak.template.plugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Source: http://stackoverflow.com/questions/16602668/creating-a-classloader-to-load-a-jar-file-from-a-byte-array
 */
public class ByteArrayClassLoader extends ClassLoader {
    private final byte[] jarBytes;

    public ByteArrayClassLoader(byte[] jarBytes) {
        this.jarBytes = jarBytes;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            Class<?> sysClass = getClass().getClassLoader().loadClass(name);
            return sysClass;
        } catch (ClassNotFoundException e) {
            // Ignore it. It's ok.
        }

        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            try {
                InputStream in = getResourceAsStream(name.replace('.', '/') + ".class");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                byte[] bytes = out.toByteArray();
                clazz = defineClass(name, bytes, 0, bytes.length);
                if (resolve) {
                    resolveClass(clazz);
                }
            } catch (Exception e) {
                clazz = super.loadClass(name, resolve);
            }
        }
        return clazz;
    }

    @Override
    public URL getResource(String name) {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        try {
            JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jarBytes));
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if (entry.getName().equals(name)) {
                    return jis;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}