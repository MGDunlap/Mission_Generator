package com.mission.missioncreator;

public class Mission {

     private String name, type, description, reward;

     public Mission(){
         name = "Mission";
         type = "Basic Mission";
         description = "This is a basic mission";
         reward = "Nothing";
     }

     public Mission(String name, String type, String description, String reward){
         this.name = name;
         this.type = type;
         this.description = description;
         this.reward = reward;
     }

}
