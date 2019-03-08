package com.stl.skipthelibrary.Singletons;

public class Test {
    private static final Test ourInstance = new Test();

    public static Test getInstance() {
        return ourInstance;
    }

    private Test() {
    }
}
