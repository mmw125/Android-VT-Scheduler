package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anirudha Simha on 4/25/2017.
 */

public class ScheduleGenerator {

    public static List<Schedule> generateSchedules(Context context, ArrayList<Course> desiredCourses) {
        Log.d("schedule_gen", "desiredCourses size = " + desiredCourses.size());
        // Creating Array list to hold list of crns
        ArrayList<ArrayList<CRN>> crnsList = new ArrayList<>();
        for (Course course : desiredCourses) {
            ArrayList<CRN> courseCRNSList = new ArrayList<>();
            for (CRN crn : course.getCRNs(context)) {
                // updating meeting times of CRN
                crn.updateMeetingTimes(context);
                courseCRNSList.add(crn);
            }
            crnsList.add(courseCRNSList);
        }

        // Done creating list generating perms now
        ArrayList<ArrayList<CRN>> results = permperm(crnsList);

        List<Schedule> schedulesList = new ArrayList<>();

        for (ArrayList<CRN> s : results) {
            schedulesList.add(new Schedule(s));
        }

        Log.d("schedule_gen", "result size = " + schedulesList.size());
        return schedulesList;
    }


    /**
     * @param mList list of objects to check collsions for
     * @return true if collision detected false otherwise
     */
    public static boolean meetingTimesCollision(ArrayList<CRN> mList) {

        if (mList.size() <= 1) {
            return false;
        }

        ArrayList<MeetingTime> meetingTimes = new ArrayList<>();
        for(CRN crn : mList) {
            meetingTimes.addAll(crn.getMeetingTimes());
        }
        // sorting the list first
        Collections.sort(meetingTimes);

        for (int i = 0; i + 1 < meetingTimes.size(); i++) {
            if (meetingTimes.get(i).getEndTime() >= meetingTimes.get(i + 1).getStartTime()) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<ArrayList<CRN>> perm(ArrayList<?> courseAList, ArrayList<CRN> courseBList) {
        // creating empty list
        ArrayList<ArrayList<CRN>> permuatationsList = new ArrayList<>();

        ArrayList<CRN> permutation;
        // creating every possible perm
        for (Object elementA : courseAList) {
            for (CRN elementB : courseBList) {
                permutation = new ArrayList<>();

                if (courseAList.get(0) instanceof ArrayList<?>) {
                    permutation.addAll((ArrayList<CRN>) elementA);
                } else {
                    permutation.add((CRN) elementA);
                }
                permutation.add(elementB);

                // add the permutation to the list if it does not have any collisions
                if (!meetingTimesCollision(permutation)) {
                    permuatationsList.add(permutation);
                }
            }
        }
        return permuatationsList;
    }

    /**
     * @param crnsList array list of arraylist, arrayslist are arraylist of DATeTime CRNS for each course
     * @return final list pof permutations
     */
    public static ArrayList<ArrayList<CRN>> permperm(ArrayList<ArrayList<CRN>> crnsList) {
        ArrayList<ArrayList<CRN>> permutationsList = new ArrayList<>();

        if (crnsList.size() == 1) {
            for(CRN crn : crnsList.get(0)) {
                ArrayList<CRN> inner = new ArrayList<>(1);
                inner.add(crn);
                permutationsList.add(inner);
            }
        } else if(crnsList.size() != 0){
            for (int i = 0; i < crnsList.size(); i++) {
                if (i == 0) {
                    permutationsList.addAll(perm(crnsList.get(0), crnsList.get(1)));
                } else if (i > 1 && i < crnsList.size() - 1) {
                    permutationsList = perm(permutationsList, crnsList.get(i));
                } else {
                    // do nothing on the last one since it was already permutated or the second one sonce ti was already permutated as well
                }

            }
        }
        return permutationsList;
    }
}
