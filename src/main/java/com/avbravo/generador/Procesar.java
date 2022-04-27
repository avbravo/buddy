/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.generador;

import com.avbravo.model.AnnotationScanner;
import com.avbravo.repository.anootation.Entity;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

/**
 *
 * @author avbravo
 */
public class Procesar {

    AnnotationScanner annotationScanner = new AnnotationScanner();

    public void start() {
        try {
   
            String currentDir = new java.io.File(".").toURI().toString();
            System.out.println("currentDir " + currentDir);
            String packageName = this.getClass().getPackage().getName();
            System.out.println("getPackage().getName() " + packageName );
            System.out.println("getClass().getCanonicalName() " + this.getClass().getCanonicalName());
            System.out.println("getClass().getPackageName()" + this.getClass().getPackageName());
            System.out.println("getClass().getAnnotations()" + this.getClass().getAnnotations());

            
            
          //
        System.out.println("pom.xml");
            URL urlPom = this.getClass().getResource("/pom.xml");
            System.out.println("imprimo");
            System.out.println("URL a pom.xml "+urlPom.toURI());
//        System.out.println(new ResourceTest().getClass().getResource(fileName));
//        System.out.println(new ResourceTest().getClass().getClassLoader().getResource(fileName));
            
/**
 * Aqui verifica las clases que tengan la anotacion @Entity 
 * lee el paquete recorre la lista de clases 
 * verifica si tienen la anotacion @Entity
 */
            
            
List<Class<?>> entityClass=            annotationScanner.scanAnnotatedClasses(packageName, Entity.class);
if(entityClass == null || entityClass.isEmpty()){
    System.out.println("no hay clases con  @Entity");
}
else{
    System.out.println("si hay clases @Entity");
}

/***
 * Crea la clase en tiempo de ejecucion
 */

            // Create a Class
            DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                    .subclass(Object.class)
                    .name("com.avbravo.repository.CountryReoositoryImpl")
                    .method(named("toString")).intercept(FixedValue.value("Hello World!"))
                    .make();

//          File file = new File(classLoader.getResource("fileTest.txt").getFile());
//    inputStream = new FileInputStream(file);
// URL url = Resources.getResource("file name");
//
            System.out.println("ire a cargar archivos");
            File folder = getFileFromURL("/repository");
            if (folder != null) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles == null) {
                    System.out.println("directorio vacio...");
                } else {
                    for (File f : listOfFiles) {
                        System.out.println(" repo name() " + f.getName());
                        System.out.println(" repo getCanonicalPath() " + f.getCanonicalPath());
                    }
                }
            }

//URL sqlScriptUrl = MyServletContextListener.class
//                       .getClassLoader().getResource("sql/script.sql");
//String path="";        
//File folder = new File(path);
//File[] listOfFiles = folder.listFiles();
            String fileName = "com.avbravo.repository.CountryReoositoryImpl";

            Path path = Paths.get(fileName);
            File file = new File(fileName);
            unloaded.saveIn(file);
//unloaded.inject(file);
//unloaded.toJar(file);

            Class<?> objectClass = unloaded.load(Main.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();

            Object object = objectClass.newInstance();
            System.out.printf("%s: %s\n", object.getClass().getName(), object.toString());

            // Intercept methods
            Foo foo = new ByteBuddy()
                    .subclass(Foo.class)
                    .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("One!"))
                    .method(named("foo")).intercept(FixedValue.value("Two!"))
                    .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
                    .make()
                    .load(Main.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded()
                    .newInstance();

            System.out.println();
            System.out.printf("%s.bar(): %s\n", foo.getClass().getName(), foo.bar());
            System.out.printf("%s.foo(): %s\n", foo.getClass().getName(), foo.foo());
            System.out.printf("%s.foo(Object o): %s\n", foo.getClass().getName(), foo.foo("¡Hello World!"));

            // Delegating a method call
            Class<? extends Source> sourceClass = new ByteBuddy()
                    .subclass(Source.class)
                    .method(named("hello")).intercept(MethodDelegation.to(Target.class))
                    .make()
                    .load(Main.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();

            String message = sourceClass.newInstance().hello("World");
            System.out.println();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println("start() " + e.getLocalizedMessage());
        }
    }

    private File getFileFromURL(String repositoryPath) {
        try {
            //  URL url = this.getClass().getClassLoader().getResource("/repository");
            System.out.println("llego a cargar archivos");
            URL url = this.getClass().getClassLoader().getResource(repositoryPath);
            File file = null;
            if (url == null) {
                System.out.println("url is null");
                return null;
            }
            try {
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                file = new File(url.getPath());
            } finally {
                return file;
            }
        } catch (Exception e) {
            System.out.println("getFileFromURL() " + e.getLocalizedMessage());
        }
        return null;
    }
}
