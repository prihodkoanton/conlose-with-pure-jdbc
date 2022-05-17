package com.foxminded.aprihodko.main;

import java.util.List;

import com.foxminded.aprihodko.dao.postgres.GenerateGroups;

public class Main {

   public static void main(String[] args) {
      GenerateGroups gr = new GenerateGroups();
      System.out.println(gr.randomName());
      gr.createGroups();
   }

}
