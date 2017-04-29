package com.markwiggans.vtscheduler.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Anirudha Simha on 4/25/2017.
 */

public class ScheduleGenerator {
   public ArrayList<?> perm(ArrayList<?> courseAList, ArrayList<?> courseBList){
       // creating empty list
       ArrayList<Course> permuatations = new ArrayList<>();
       permuatations.clear();
       ArrayList<Course> mergedList = new ArrayList<>();

       for(Object elementA : courseAList){
           for(Object elementB : courseBList){
               mergedList.clear();

               if(courseAList.get(0) instanceof  ArrayList<?>){
                   mergedList.addAll((ArrayList<Course>)elementA);
                   mergedList.addAll((ArrayList<Course>)elementB);
               }
               else{
                   mergedList.add((Course)elementA);
                   mergedList.add((Course)elementB);
               }


           }
       }

        return new ArrayList<>();
   }

   public ArrayList<CRN> checkForConflicts(ArrayList<CRN> mergedList){
       for(int i = 0; i < mergedList.size(); i++){

       }
       return new ArrayList<CRN>();
   }
}
