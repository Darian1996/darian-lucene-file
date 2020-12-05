package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 课程
 */
@Data
public class CourseDO {
    /**
     * 课程code
     */
    private String code;

    /**
     * 课程名字
     */
    private String desc;

    /**
     * 连续的次数
     */
    private int continuousCount;

    /**
     * 这门课程同时可以上的班级的个数
     */
    private int classCount;

    /**
     * 这个课程不用上的课程
     */
    private String mustNotMakeClassString;

    /**
     * 不用上的课程的集合
     * <>generator</>
     */
    private Set<XYCoordinate> mustNotMakeClassXYSet = new HashSet<>();

    /**
     * 必须有一节课的一个String集合
     */
    private List<String> mustHaveOneClassStringList = new ArrayList<>();

    /**
     * 必须有一节课的 Set<XY> 集合
     * <>generator</>
     */
    private List<Set<XYCoordinate>> mustHaveOneClassSetList = new ArrayList<>();

    /**
     * 特殊的配置
     */
    private List<CourseRuleDO> courseRuleDOList;

}