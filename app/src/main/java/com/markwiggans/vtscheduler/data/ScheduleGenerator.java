package com.markwiggans.vtscheduler.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by Anirudha Simha on 4/25/2017.
 */

public class ScheduleGenerator {

    /**
     *
     * @param mList list of objects to check collsions for
     * @return true if collision detected false otherwise
     */
    public  boolean meetingTimesCollision(ArrayList<DateTimeCRN> mList){

        if(mList.size() <= 1){
            return false;
        }

        // sorting the list first
        Collections.sort(mList, new Comparator<DateTimeCRN>() {
            @Override
            public int compare(DateTimeCRN p1, DateTimeCRN p2) {
                return p1.getStartTime() - p2.getStartTime(); // Ascending
            }
        });

        for(int i = 0; i < mList.size(); i++){
            boolean startTimeColl = false;
            boolean endTimeColl = false;
            // first element
            if (i == 0) {
                startTimeColl = mList.get(i).getStartTime() >= mList.get(i+1).getStartTime() &&
                        mList.get(i).getStartTime() <= mList.get(i+1).getStartTime();
                endTimeColl = mList.get(i).getEndTime() >= mList.get(i+1).getEndTime() &&
                        mList.get(i).getEndTime() <= mList.get(i+1).getEndTime();
            }
            // last element
            else if(i+1 == mList.size()){
                startTimeColl = (mList.get(i).getStartTime() >= mList.get(i-1).getStartTime() &&
                        mList.get(i).getStartTime() <= mList.get(i-1).getStartTime());
                endTimeColl = (mList.get(i).getEndTime() >= mList.get(i-1).getEndTime() &&
                        mList.get(i).getEndTime() <= mList.get(i-1).getEndTime());
            }
            // every other element
            else{
                startTimeColl =
                        // checking if interferance with one in front
                        (mList.get(i).getStartTime() >= mList.get(i+1).getStartTime() &&
                        mList.get(i).getStartTime() <= mList.get(i+1).getStartTime())||
                        // checking if interferance with one in back
                        (mList.get(i).getStartTime() >= mList.get(i-1).getStartTime() &&
                                mList.get(i).getStartTime() <= mList.get(i-1).getStartTime());
                endTimeColl =
                        // checking if interferance with one in front
                        (mList.get(i).getEndTime() >= mList.get(i+1).getEndTime() &&
                        mList.get(i).getEndTime() <= mList.get(i+1).getEndTime()) ||
                        // checking if interferance wit hone in back
                        (mList.get(i).getEndTime() >= mList.get(i-1).getEndTime() &&
                                mList.get(i).getEndTime() <= mList.get(i-1).getEndTime());
            }

            if(startTimeColl || endTimeColl){
                return true;
            }
        }

        return false;
    }

   public ArrayList<ArrayList<DateTimeCRN>> perm(ArrayList<?> courseAList, ArrayList<?> courseBList){
       // creating empty list
       ArrayList<ArrayList<DateTimeCRN>> permuatationsList = new ArrayList<>();
       permuatationsList.clear();


       ArrayList<DateTimeCRN> permutation = new ArrayList<>();
        // creating every possible perm
       for(Object elementA : courseAList){
           for(Object elementB : courseBList){
               permutation.clear();

               if(courseAList.get(0) instanceof  ArrayList<?>){
                   permutation.addAll((ArrayList<DateTimeCRN>)elementA);
                   permutation.addAll((ArrayList<DateTimeCRN>)elementB);
               }
               else{
                   permutation.add((DateTimeCRN) elementA);
                   permutation.add((DateTimeCRN) elementB);
               }
               // add the permutation to the list if it does not have any collisions
                if(!meetingTimesCollision(permutation)){
                    permuatationsList.add(permutation);
                }

           }
       }

        return permuatationsList;
   }

    /**
     *
     * @param crnsList array list of arraylist, arrayslist are arraylist of DATeTime CRNS for each course
     * @return final list pof permutations
     */
   public ArrayList<ArrayList<DateTimeCRN>> permperm(ArrayList<ArrayList<DateTimeCRN>> crnsList){
       ArrayList<ArrayList<DateTimeCRN>> permutationsList = new ArrayList<>();
       permutationsList.clear();

       int numClasses = crnsList.size();
       if (numClasses <= 1){

       }
       else{
           for(int i = 0; i < crnsList.size();){

           }
       }





       return permutationsList;
   }


}
