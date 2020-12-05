
package com.darian.darianlucenefile.rangeClass.service;

import com.darian.darianlucenefile.rangeClass.config.RangeClassConfig;
import com.darian.darianlucenefile.rangeClass.domain.ClassDO;
import com.darian.darianlucenefile.rangeClass.domain.CourseDO;
import com.darian.darianlucenefile.rangeClass.domain.GradeDO;
import com.darian.darianlucenefile.rangeClass.domain.SchoolDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RangeClassService {

    @Resource
    private RangeClassConfig rangeClassConfig;

    public void rangeClass() {

        // 获取学校的信息
        SchoolDO schoolDO = rangeClassConfig.getSchoolDO();

        // 初始化所有的班级
        Map<String, String[][]> classCourseMap = new HashMap<>();
        Map<String, ClassDO> classDOMap = rangeClassConfig.getClassDOMap();
        for (Map.Entry<String, ClassDO> stringClassDOEntry : classDOMap.entrySet()) {
            classCourseMap.put(stringClassDOEntry.getKey(), getAllArrays(schoolDO));
        }

        // 初始化年级的相关信息
        Map<Integer, GradeDO> gradeDOMap = rangeClassConfig.getGradeDOMap();


        // 获取所有的课程的信息
        Map<String, CourseDO> courseDOMap = rangeClassConfig.getCourseDOMap();


        // 1. 先添加老师

        // 2. 添加 班级（年级号，班级号）

        // 3. （为每个班级）添加 课程
        // 3.1 课程一次排几节（1）？：1，2
        // 3.2 课程必须（1）个班一起上？：1，2

        // 4. 为每个班级的每个课程添加老师

        // 5. 老师不能上课的时间表

        // 6. 课程不能上课的时间表

        // 7. 生成课程表


    }

    public static String[][] getAllArrays(SchoolDO schoolDO) {
        int baseDays = schoolDO.getBaseDays();
        int oneDaySourceCount = schoolDO.getOneDaySourceCount();
        String[][] arrays = new String[baseDays][oneDaySourceCount];
        return arrays;
    }
}