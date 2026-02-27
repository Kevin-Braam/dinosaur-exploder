package com.code.analysis;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        CtClass<?> l = Launcher.parseClass("class A { void m() { System.out.println(\"yeah\");} }");
        System.out.println(l);
    }
}
