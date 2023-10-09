package com.hh.content.service;

import com.hh.base.model.PageParams;
import com.hh.base.model.PageResult;
import com.hh.content.model.dto.AddCourseDto;
import com.hh.content.model.dto.CourseBaseInfoDto;
import com.hh.content.model.dto.EditCourseDto;
import com.hh.content.model.dto.QueryCourseParamsDto;
import com.hh.content.model.po.CourseBase;

public interface CourseBaseInfoService {
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
    //新增课程接口
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);
     CourseBaseInfoDto getCourseBaseInfo(Long courseId);
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);
}

