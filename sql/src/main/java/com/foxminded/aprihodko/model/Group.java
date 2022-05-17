package com.foxminded.aprihodko.model;

import java.util.Objects;

public class Group extends LongEntity{
   public static final String GROUP_ID = "group_id";
   public static final String GROUP_NAME = "group_name";
   
   private String name;
   
   public Group(Long id, String name) {
      super(id);
      this.name = name;
   }
   
   public Group(String name) {
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
      Group other = (Group) obj;
      return Objects.equals(name, other.name);
   }
}
