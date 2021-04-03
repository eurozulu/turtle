package org.spoofer.interpreter;

public interface Stack {

    String TOKEN_START = "${";
    String TOKEN_END = "}";

    <T> T get(String name);
    boolean contains(String name);
}
