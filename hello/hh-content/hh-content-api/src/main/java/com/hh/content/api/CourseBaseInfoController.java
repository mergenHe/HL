package com.hh.content.api;

import com.hh.base.exception.ValidationGroups;
import com.hh.content.model.dto.AddCourseDto;
import com.hh.content.model.dto.CourseBaseInfoDto;
import com.hh.content.model.dto.EditCourseDto;
import com.hh.content.model.po.CourseBase;
import com.hh.content.model.dto.QueryCourseParamsDto;
import com.hh.base.model.PageParams;
import com.hh.base.model.PageResult;
import com.hh.content.service.CourseBaseInfoService;
import com.hh.content.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description 课程信息编辑接口
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
 @RestController
 //这个注解的目的就是为返回Json数据
public class CourseBaseInfoController {
@Autowired
    CourseBaseInfoService courseBaseInfoService;
 @PostMapping ("/course/list")
  public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParams){

     PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParams);
     return courseBasePageResult;

  }
    @ApiOperation("新增课程基础信息")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody  @Validated({ValidationGroups.Inster.class}) AddCourseDto addCourseDto){
        Long companyId = 1232141425L;
//       int i = 1/0;

        return courseBaseInfoService.createCourseBase(companyId,addCourseDto);
    }
    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId){
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取用户身份
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }
    @ApiOperation("修改课程基础信息")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated(ValidationGroups.Update.class) EditCourseDto editCourseDto){
        Long companyId = 1232141425L;
     return courseBaseInfoService.updateCourseBase(companyId,editCourseDto);

    }




}
