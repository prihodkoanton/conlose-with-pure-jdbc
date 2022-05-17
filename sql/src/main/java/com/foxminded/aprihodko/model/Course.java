package com.foxminded.aprihodko.model;

import java.util.Objects;

public class Course extends LongEntity{
   public static final String COURSE_ID = "course_id";
   public static final String COURSE_NAME = "course_name";
   public static final String COURSE_DESCRIPTION = "course_description";
   
   private String name;
   
   public Course(Long id, String name) {
      super(id);
      this.name = name;
   }
   
   public Course(String name) {
      this(null, name);
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Objects.hash(name);
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
      Course other = (Course) obj;
      return Objects.equals(name, other.name);
   }
}
