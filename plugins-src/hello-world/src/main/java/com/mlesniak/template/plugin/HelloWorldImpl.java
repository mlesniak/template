package com.mlesniak.template.plugin;

public class HelloWorldImpl implements HelloWorld {
    @Override
    public void sayHello(String name) {
        System.out.println("Hello from plugin!");
    }
}
