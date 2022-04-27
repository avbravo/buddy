/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.avbravo.repository;

import com.avbravo.compiler.DynamicCompiler;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author avbravo
 */
public class AdressTest {
      
    public AdressTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        // TODO review the generated test code and remove the default call to fail.
        try {
            
             String fileName = "/home/mkyong/newFile.txt";

        Path path = Paths.get(fileName);

        // default, create, truncate and write to it.
        try (BufferedWriter writer =
                     Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            writer.write("Hello World !!");

        } catch (IOException e) {
            e.printStackTrace();
        }

              StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            out.println("package com.avbravo.repository;");
            out.println("");
            out.println("public class HelloWorld {");
            out.println("  public static void main(String args[]) {");
            out.println("    System.out.println(\"This is in another java file\");");
            out.println("  }");
            out.println("}");
            out.flush();
            out.close();
            String fullName = "com.avbravo.repository.HelloWorld";
            String src = writer.toString();
            
            System.out.println("codigo generado...");
            DynamicCompiler uCompiler = new DynamicCompiler(fullName, src);
            uCompiler.compile();
            uCompiler.run();
            System.out.println(" compilado....");
        } catch (Exception e) {
            System.out.println("testSomeMethod() "+e.getLocalizedMessage());
        }
   
    }
    
}
