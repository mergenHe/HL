package com.hh.content.service;

import com.hh.base.model.PageParams;
import com.hh.base.model.PageResult;
import com.hh.content.mapper.CourseBaseMapper;
import com.hh.content.model.dto.QueryCourseParamsDto;
import com.hh.content.model.po.CourseBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CourseBaseInfoTests {
    @Autowired
    CourseBaseInfoService courseBaseInfoService;
    @Test
    public void testCourseBaseServiceInfo() {
        //查询条件类
        QueryCourseParamsDto queryCourseParamsDto =new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        queryCourseParamsDto.setAuditStatus("202004");
        //分页参数对象
        PageParams pageParams =new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(2L);
        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);
        System.out.println(courseBasePageResult);

    }
}
