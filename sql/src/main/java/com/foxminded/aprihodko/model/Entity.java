package com.foxminded.aprihodko.model;

public interface Entity<K> {
   K getId();
   
   void setId(K id);
}
