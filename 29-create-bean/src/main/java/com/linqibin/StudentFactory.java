package com.linqibin;

public class StudentFactory  {

    public static Student get() {
        return new Student();
    }

    public Student getStudent() {
        return new Student();
    }
}
