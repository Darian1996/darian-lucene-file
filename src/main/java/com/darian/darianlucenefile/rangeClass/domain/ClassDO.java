package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Data
public class ClassDO {
    /**
     * 年级
     */
    private int gradeNum;
    /**
     * 班级号
     */
    private int classNum;

    /**
     * course.Code -> teacherId
     */
    private Map<String, List<Integer>> courseTeacherIdMap = new HashMap<>();
}