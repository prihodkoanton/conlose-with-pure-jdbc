package com.foxminded.aprihodko.model;

import java.util.Objects;

public class Students extends LongEntity {
    public static final String STUDENT_ID = "student_id";
    public static final String GROUP_ID = "group_id";
    public static final String FIRRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";

    private String firstName;
    private String lastName;

    public Students(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Students(String firstName, String lastName) {
        this(null, firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(firstName, lastName);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Students other = (Students) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "Students [firstName=" + firstName + ", lastName=" + lastName + "]";
    }
    
    
}
