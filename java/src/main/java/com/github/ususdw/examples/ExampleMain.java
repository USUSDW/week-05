package com.github.ususdw.examples;

public class ExampleMain {
    interface ArithmeticOperation {
        int apply(int value);
    }

    interface Provider<T> {
        T get();
    }

    public static int addTwo(int value) {
        return value + 2;
    }

    public static void printWithFive(ArithmeticOperation operation) {
        System.out.println(operation.apply(5));
    }

    public static void main(String[] args) {
        printWithFive(ExampleMain::addTwo);
        printWithFive((x) -> x * x);
    }
}
