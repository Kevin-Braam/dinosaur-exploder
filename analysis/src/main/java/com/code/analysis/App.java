package com.code.analysis;

import spoon.processing.AbstractProcessor;

/**
 * Hello world!
 */
public class App {

    public static class MyProcessor extends AbstractProcessor {
        @Override
        public void process(spoon.reflect.declaration.CtElement element) {
            // This method will be called for each element in the code being analyzed.
            // You can add your analysis logic here.
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
