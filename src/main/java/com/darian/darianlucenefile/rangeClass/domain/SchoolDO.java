package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class SchoolDO {
    /**
     * 名字
     */
    private String name;

    /**
     * 一周上几天课
     */
    private int baseDays;

    /**
     * 一天上几节课
     */
    private int oneDaySourceCount;

    /**
     * 这个学校不用上的课程
     */
    private String mustNotMakeClassString;

    /**
     * 不用上的课程的集合
     */
    private Set<XYCoordinate> mustNotMakeClassXYSet = new HashSet<>();

    /**
     * 一周一共多少课时
     */
    private int weekSourceCount;
}