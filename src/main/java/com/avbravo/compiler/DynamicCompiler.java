/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.List;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 *
 * @author avbravo
 */
public class DynamicCompiler {
    private JavaFileManager fileManager;
    private String fullName;
    private String sourceCode;

    public DynamicCompiler(String fullName, String srcCode) {
        this.fullName = fullName;
        this.sourceCode = srcCode;
        this.fileManager = initFileManager();
    }

    public JavaFileManager initFileManager() {
        if (fileManager != null)
            return fileManager;
        else {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            fileManager = new
                    ClassFileManager(compiler
                    .getStandardFileManager(null, null, null));
            return fileManager;
        }
    }

    public void compile() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();    
        List<JavaFileObject> files = new ArrayList<>();
        files.add(new CharSequenceJavaFileObject(fullName, sourceCode));

        compiler.getTask(
                null,
                fileManager,
                null,
                null,
                null,
                files
        ).call();
    }

    public void run() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            fileManager
                    .getClassLoader(null)
                    .loadClass(fullName)
                    .getDeclaredMethod("main", new Class[]{String[].class})
                    .invoke(null, new Object[]{null});
        } catch (InvocationTargetException e) {
            System.out.print("InvocationTargetException");
            //logger.error("InvocationTargetException:", e);
        } catch (NoSuchMethodException e) {
            System.out.print("NoSuchMethodException ");
            //logger.error("NoSuchMethodException:", e);
        }
    }

    public class CharSequenceJavaFileObject extends SimpleJavaFileObject {

        /**
         * CharSequence representing the source code to be compiled
         */
        private CharSequence content;

        public CharSequenceJavaFileObject(String className, CharSequence content) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }

    public class ClassFileManager extends ForwardingJavaFileManager {
        private JavaClassObject javaClassObject;

        public ClassFileManager(StandardJavaFileManager standardManager) {
            super(standardManager);
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return new SecureClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    byte[] b = javaClassObject.getBytes();
                    return super.defineClass(name, javaClassObject.getBytes(), 0, b.length);
                }
            };
        }

        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            this.javaClassObject = new JavaClassObject(className, kind);
            return this.javaClassObject;
        }
    }

    public class JavaClassObject extends SimpleJavaFileObject {
        protected final ByteArrayOutputStream bos =
                new ByteArrayOutputStream();

        public JavaClassObject(String name, Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/')
                    + kind.extension), kind);
        }

        public byte[] getBytes() {
            return bos.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return bos;
        }
    }
}
