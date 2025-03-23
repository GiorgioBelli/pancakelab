package org.pancakelab.integration.actors;

public interface Entity {

    int id();

    default void log(String message, Object ...args) {
        System.out.format(this.getClass().getName()+"-"+this.id()+": "+message+"\n", args);
    }
}
