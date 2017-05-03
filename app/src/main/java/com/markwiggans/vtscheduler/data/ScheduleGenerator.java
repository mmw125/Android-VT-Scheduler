package com.markwiggans.vtscheduler.data;

import android.app.admin.DeviceAdminInfo;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Anirudha Simha on 4/25/2017.
 */

public class ScheduleGenerator {

    public static List<Schedule> generateSchedules(Context context, ArrayList<Course> desiredCourses) {
        // Creating Array list to hold list of crns
        ArrayList<ArrayList<DateTimeCRN>> crnsList = new ArrayList<ArrayList<DateTimeCRN>>();
        crnsList.clear();
        ArrayList<DateTimeCRN> courseCRNSList = new ArrayList<>();
        for (Course course : desiredCourses) {
            courseCRNSList.clear();
            for (CRN crn : course.getCRNs(context)) {
                // updating meeting times of CRN
                crn.updateMeetingTimes(context);
                // converting crn to series of DateTimeCRN objects
                for (MeetingTime m : crn.getMeetingTimes()) {
                    int startTime = m.getStartTime();
                    int endTime = m.getEndTime();
                    switch (m.getDay()) {
                        case MONDAY:
                            startTime += 10000 ;
                            endTime += 10000;
                            break;
                        case TUESDAY:
                            startTime += 20000 ;
                            endTime += 20000;
                            break;
                        case WEDNESDAY:
                            startTime += 30000 ;
                            endTime += 30000;
                            break;
                        case THURSDAY:
                            startTime += 40000 ;
                            endTime += 40000;
                            break;
                        case FRIDAY:
                            startTime += 50000 ;
                            endTime += 50000;
                            break;
                        case SATURDAY:
                            startTime += 60000 ;
                            endTime += 60000;
                            break;
                        case SUNDAY:
                            startTime += 70000 ;
                            endTime += 70000;
                            break;
                        default:
                            // this should not happen, like ever

                            break;
                    }
                    courseCRNSList.add(new DateTimeCRN(crn.getCRN(), startTime, endTime, crn));
                }
            }
            crnsList.add(courseCRNSList);
        }


        // Done creating lsit generting perms now
        ArrayList<ArrayList<DateTimeCRN>> results =  permperm(crnsList);

        List<Schedule> schedulesList = new ArrayList<>();
        schedulesList.clear();
        ArrayList<CRN> temp = new ArrayList<>();

        for (ArrayList<DateTimeCRN> s : results){
            temp.clear();
            for(DateTimeCRN c: s){
                temp.add(c.getCRNObject());
            }
            schedulesList.add(new Schedule(temp));
        }

        return schedulesList;
    }


    /**
     * @param mList list of objects to check collsions for
     * @return true if collision detected false otherwise
     */
    public static boolean meetingTimesCollision(ArrayList<DateTimeCRN> mList) {

        if (mList.size() <= 1) {
            return false;
        }

        // sorting the list first
        Collections.sort(mList, new Comparator<DateTimeCRN>() {
            @Override
            public int compare(DateTimeCRN p1, DateTimeCRN p2) {
                return p1.getStartTime() - p2.getStartTime(); // Ascending
            }
        });

        for (int i = 0; i < mList.size(); i++) {
            boolean startTimeColl = false;
            boolean endTimeColl = false;
            // first element
            if (i == 0) {
                startTimeColl = mList.get(i).getStartTime() >= mList.get(i + 1).getStartTime() &&
                        mList.get(i).getStartTime() <= mList.get(i + 1).getStartTime();
                endTimeColl = mList.get(i).getEndTime() >= mList.get(i + 1).getEndTime() &&
                        mList.get(i).getEndTime() <= mList.get(i + 1).getEndTime();
            }
            // last element
            else if (i + 1 == mList.size()) {
                startTimeColl = (mList.get(i).getStartTime() >= mList.get(i - 1).getStartTime() &&
                        mList.get(i).getStartTime() <= mList.get(i - 1).getStartTime());
                endTimeColl = (mList.get(i).getEndTime() >= mList.get(i - 1).getEndTime() &&
                        mList.get(i).getEndTime() <= mList.get(i - 1).getEndTime());
            }
            // every other element
            else {
                startTimeColl =
                        // checking if interferance with one in front
                        (mList.get(i).getStartTime() >= mList.get(i + 1).getStartTime() &&
                                mList.get(i).getStartTime() <= mList.get(i + 1).getStartTime()) ||
                                // checking if interferance with one in back
                                (mList.get(i).getStartTime() >= mList.get(i - 1).getStartTime() &&
                                        mList.get(i).getStartTime() <= mList.get(i - 1).getStartTime());
                endTimeColl =
                        // checking if interferance with one in front
                        (mList.get(i).getEndTime() >= mList.get(i + 1).getEndTime() &&
                                mList.get(i).getEndTime() <= mList.get(i + 1).getEndTime()) ||
                                // checking if interferance wit hone in back
                                (mList.get(i).getEndTime() >= mList.get(i - 1).getEndTime() &&
                                        mList.get(i).getEndTime() <= mList.get(i - 1).getEndTime());
            }

            if (startTimeColl || endTimeColl) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<ArrayList<DateTimeCRN>> perm(ArrayList<?> courseAList, ArrayList<?> courseBList) {
        // creating empty list
        ArrayList<ArrayList<DateTimeCRN>> permuatationsList = new ArrayList<>();
        permuatationsList.clear();


        ArrayList<DateTimeCRN> permutation = new ArrayList<>();
        // creating every possible perm
        for (Object elementA : courseAList) {
            for (Object elementB : courseBList) {
                permutation.clear();

                if (courseAList.get(0) instanceof ArrayList<?>) {
                    permutation.addAll((ArrayList<DateTimeCRN>) elementA);
                } else {
                    permutation.add((DateTimeCRN) elementA);
                }

                if (courseBList.get(0) instanceof ArrayList<?>) {
                    permutation.addAll((ArrayList<DateTimeCRN>) elementB);
                } else {
                    permutation.add((DateTimeCRN) elementB);
                }

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
    public static ArrayList<ArrayList<DateTimeCRN>> permperm(ArrayList<ArrayList<DateTimeCRN>> crnsList) {
        ArrayList<ArrayList<DateTimeCRN>> permutationsList = new ArrayList<>();
        permutationsList.clear();

        int numClasses = crnsList.size();
        if (numClasses <= 1) {

        } else {
            for (int i = 0; i < crnsList.size(); ) {
                if (i == 0) {
                    permutationsList.addAll(perm(crnsList.get(0), crnsList.get(1)));
                } else if (i > 1 && i < crnsList.size() - 1) {
                    ArrayList<ArrayList<DateTimeCRN>> temp = perm(permutationsList, crnsList.get(i));
                    permutationsList.clear();
                    permutationsList.addAll(temp);
                } else {
                    // do nothing on the last one since it was already permutated or the second one sonce ti was already permutated as well
                }

            }
        }
        return permutationsList;
    }


}
