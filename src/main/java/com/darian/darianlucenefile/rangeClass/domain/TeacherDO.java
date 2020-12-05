
package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TeacherDO {
    /**
     * 唯一标识
     */
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 不能上的课程
     */
    private String mustNotMakeClassString;

    /**
     * 不用上的课程的集合
     */
    private Set<XYCoordinate> mustNotMakeClassXYSet = new HashSet<>();
}