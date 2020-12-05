package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Getter;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/9/3  1:12
 */
@Getter
public enum CourseRuleEnums {

    HALF_DAY_MUST_HAVE_ONE("HALF_DAY_MUST_HAVE_ONE",
            "半天必须有一节课"),

    HALF_DAY_HAVE_ONE_OR_ZERO("HALF_DAY_HAVE_ONE_OR_ZERO",
            "半天内有1节课，或者0节课"),

    ONE_DAY_HAVE_ONE_OR_ZERO("ONE_DAY_HAVE_ONE_OR_ZERO",
            "一天内有1节课，或者0节课"),

    HALF_DAY_CONFLICT_OTHER_COURSE("HALF_DAY_CONFLICT_OTHER_COURSE",
            "半天内和【】课程不能同时上"),

    ONE_DAY_CONFLICT_OTHER_COURSE("ONE_DAY_CONFLICT_OTHER_COURSE",
            "一天内和【】课程不能同时上"),

    GRADE_ONLY_ONE_CLASSROOM("GRADE_ONLY_ONE_CLASSROOM",
            "一个年级，这个课程只有 1 个教室"),

    AFTERNOON_MUST_NOT_HAVE_CLASS("AFTERNOON_MUST_NOT_HAVE_CLASS",
            "下午不能课程"),

    MORNING_MUST_HAVE_ONE_OR_MORE("MORNING_MUST_HAVE_ONE_OR_MORE",
            "每天上午必须排列一节课"),

    MORNING_HEAD_2_MUST_NOT_HAVE("MORNING_HEAD_2_MUST_NOT_HAVE",
            "每天早上不能排的课");
    /**
     * code
     */
    private String code;

    /**
     *
     */
    private String desc;

    CourseRuleEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
