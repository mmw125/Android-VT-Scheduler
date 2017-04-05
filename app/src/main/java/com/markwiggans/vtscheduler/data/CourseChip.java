package com.markwiggans.vtscheduler.data;

import com.plumillonforge.android.chipview.Chip;

/**
 * Created by Mark Wiggans on 4/5/2017.
 */

public class CourseChip implements Chip {
    private String courseString;
    public CourseChip(String courseString) {
        this.courseString = courseString;
    }

    @Override
    public String getText() {
        return courseString;
    }
}
