package com.github.javafaker;

public class Verbs {

    private final ExtendedFaker faker;

    protected Verbs(ExtendedFaker faker) {
        this.faker = faker;
    }

    public String base() {
        return this.faker.fakeValuesService().fetchString("verbs.base");
    }

    public String past() {
        return this.faker.fakeValuesService().fetchString("verbs.past");
    }

    public String pastParticiple() {
        return this.faker.fakeValuesService().fetchString("verbs.past_participle");
    }

    public String simplePresent() {
        return this.faker.fakeValuesService().fetchString("verbs.simple_present");
    }

    public String ingForm() {
        return this.faker.fakeValuesService().fetchString("verbs.ing_form");
    }

}
