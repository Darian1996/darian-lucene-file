package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 年级
 */
@Data
public class GradeDO {
    /**
     * 年级号
     */
    private Integer gradeNum;

    /**
     * 每门课多少节课
     */
    private Map<String, Integer> courseCountMap = new HashMap<>();

    /**
     * 一共多少节课
     */
    private int courseCount;

    /**
     * 自习课的个数
     */
    private int selfStudyCount;
}