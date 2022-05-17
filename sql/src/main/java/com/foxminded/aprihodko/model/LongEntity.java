package com.foxminded.aprihodko.model;

import java.util.Objects;

public class LongEntity implements Entity<Long>{
   protected Long id;
   
   public LongEntity(Long id) {
      this.id = id;
   }

   @Override
   public Long getId() {
      return this.id;
   }

   @Override
   public void setId(Long id) {
      this.id = id;
      
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      LongEntity other = (LongEntity) obj;
      return Objects.equals(id, other.id);
   }
}
