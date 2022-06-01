package com.github.javafaker;

public class ExtendedFaker extends Faker {

    private final Verbs verbs;

    public ExtendedFaker() {this.verbs = new Verbs(this);}

    public static ExtendedFaker instance() {
        return new ExtendedFaker();
    }

    public Verbs verbs() {
        return this.verbs;
    }

}
