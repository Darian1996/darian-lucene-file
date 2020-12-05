package com.darian.darianlucenefile.rangeClass.config;

import com.darian.darianlucenefile.rangeClass.domain.ClassDO;
import com.darian.darianlucenefile.rangeClass.domain.CourseDO;
import com.darian.darianlucenefile.rangeClass.domain.GradeDO;
import com.darian.darianlucenefile.rangeClass.domain.SchoolDO;
import com.darian.darianlucenefile.rangeClass.domain.TeacherDO;
import com.darian.darianlucenefile.rangeClass.domain.XYCoordinate;
import com.darian.darianlucenefile.utils.ClassPathFileReadUtils;
import com.darian.darianlucenefile.utils.JSONUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Data
@Slf4j
@Configuration
@NoArgsConstructor
public class RangeClassConfig implements InitializingBean {

    private SchoolDO schoolDO;

    /**
     * 年级的配置，每个年级多少节课
     */
    private Map<Integer, GradeDO> gradeDOMap = new HashMap();

    /**
     * 全局课程的配置
     */
    private Map<String, CourseDO> courseDOMap = new HashMap<>();

    /**
     * 老师的配置
     */
    private Map<Integer, TeacherDO> teacherDOMap = new HashMap<>();

    /**
     * 每个班级的配置
     */
    private Map<String, ClassDO> classDOMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            afterPropertiesSetException();
        } catch (Exception e) {
            log.error("选课配置初始化出错", e);
        }
    }

    private void afterPropertiesSetException() {
        String schoolJsonString = ClassPathFileReadUtils.readClassPathResource("config/rangeClass/school.json");
        schoolDO = JSONUtils.jsonToBean(schoolJsonString, SchoolDO.class);
        String gradeJsonString = ClassPathFileReadUtils.readClassPathResource("config/rangeClass/grade.json");
        gradeDOMap = JSONUtils.jsonToMap(gradeJsonString, Integer.class, GradeDO.class);
        String courseJsonString = ClassPathFileReadUtils.readClassPathResource("config/rangeClass/course.json");
        courseDOMap = JSONUtils.jsonToMap(courseJsonString, String.class, CourseDO.class);
        String teacherJsonString = ClassPathFileReadUtils.readClassPathResource("config/rangeClass/teacher.json");
        teacherDOMap = JSONUtils.jsonToMap(teacherJsonString, Integer.class, TeacherDO.class);
        String classJsonString = ClassPathFileReadUtils.readClassPathResource("config/rangeClass/class.json");
        classDOMap = JSONUtils.jsonToMap(classJsonString, String.class, ClassDO.class);

        // 处理学校不用上的课程
        schoolDO.setMustNotMakeClassXYSet(generatorSetFromString(schoolDO.getMustNotMakeClassString()));
        // 计算学校一周多少个课时
        int weekSourceCount = schoolDO.getBaseDays() * schoolDO.getOneDaySourceCount() -
                schoolDO.getMustNotMakeClassXYSet().size();
        schoolDO.setWeekSourceCount(weekSourceCount);

        // 计算每个年级多少课时
        for (Entry<Integer, GradeDO> integerGradeDOEntry : gradeDOMap.entrySet()) {
            GradeDO gradeDO = integerGradeDOEntry.getValue();
            int courseCount = gradeDO.getCourseCountMap().values()
                    .stream()
                    .mapToInt(Integer::valueOf)
                    .sum();
            gradeDO.setCourseCount(courseCount);
            int selfStudyCount = weekSourceCount - courseCount;
            gradeDO.setSelfStudyCount(selfStudyCount);

            gradeDO.getCourseCountMap().put("SELF_STUDY", selfStudyCount);
        }

        // 补充班级的 自习数
        for (Entry<String, ClassDO> stringClassDOEntry : classDOMap.entrySet()) {
            ClassDO classDO = stringClassDOEntry.getValue();
            int gradeNum = classDO.getGradeNum();
            int selfStudyCount = gradeDOMap.get(gradeNum).getSelfStudyCount();

            Map<String, List<Integer>> courseTeacherIdMap = classDO.getCourseTeacherIdMap();
            courseTeacherIdMap.put("SELF_STUDY", Arrays.asList());
        }

        // 处理 每个课程 不用上的课程 / 必须存在一节课的时间段
        for (Entry<String, CourseDO> stringCourseDOEntry : courseDOMap.entrySet()) {
            CourseDO courseDO = stringCourseDOEntry.getValue();
            // 不能有课程的时间段
            courseDO.setMustNotMakeClassXYSet(generatorSetFromString(courseDO.getMustNotMakeClassString()));
            List<Set<XYCoordinate>> XYCoordinateList = new ArrayList<>();

            List<String> mustHaveOneClassStringList = courseDO.getMustHaveOneClassStringList();
            // 必须有一节课的集合
            for (String mustHaveOneClassString : mustHaveOneClassStringList) {
                XYCoordinateList.add(generatorSetFromString(mustHaveOneClassString));
            }
            courseDO.setMustHaveOneClassSetList(XYCoordinateList);
        }

        // 处理老师的不能上的课程
        for (Entry<Integer, TeacherDO> integerTeacherDOEntry : teacherDOMap.entrySet()) {
            TeacherDO teacherDO = integerTeacherDOEntry.getValue();
            teacherDO.setMustNotMakeClassXYSet(generatorSetFromString(teacherDO.getMustNotMakeClassString()));
        }
    }

    /**
     * “1,2;3,3” 转化为 x,y 的 set 值
     *
     * @param mustNotMakeClassString
     * @return
     */
    private static Set<XYCoordinate> generatorSetFromString(String mustNotMakeClassString) {
        try {
            Set<XYCoordinate> mustNotMakeClassSet = new HashSet<>();
            if (mustNotMakeClassString == null || mustNotMakeClassString.length() == 0) {
                return mustNotMakeClassSet;
            }

            String[] split = mustNotMakeClassString.split(";");
            for (String s : split) {
                XYCoordinate xyCoordinate = new XYCoordinate();
                String[] xyArray = s.split(",");
                xyCoordinate.setX(Integer.valueOf(xyArray[0]));
                xyCoordinate.setY(Integer.valueOf(xyArray[1]));
                mustNotMakeClassSet.add(xyCoordinate);
            }
            return mustNotMakeClassSet;
        } catch (Exception e) {
            log.error("[RangeClassConfig.generatorSetFromString]转化异常：", e);
        }
        return new HashSet<>();
    }
}