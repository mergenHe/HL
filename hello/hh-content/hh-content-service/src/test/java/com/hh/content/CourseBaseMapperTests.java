package com.hh.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.base.model.PageParams;
import com.hh.base.model.PageResult;
import com.hh.content.mapper.CourseBaseMapper;
import com.hh.content.model.dto.QueryCourseParamsDto;
import com.hh.content.model.po.CourseBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class CourseBaseMapperTests {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Test
    public void testCourseBaseMapper(){
        CourseBase courseBase = courseBaseMapper.selectById(18);
        Assertions.assertNotNull(courseBase);
        //分页参数单元测试
        //查询条件类
        QueryCourseParamsDto queryCourseParamsDto =new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(courseBase.getName()),
                CourseBase::getName,
                queryCourseParamsDto.getCourseName());
        //根据课程审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),
                CourseBase::getAuditStatus,
                queryCourseParamsDto.getAuditStatus());
        //分页参数对象
        PageParams pageParams =new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(2L);
        //创建分页参数
        Page<CourseBase> page  = new Page<>(1L,2L);
        //开始分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //数据列表
        List<CourseBase> items = pageResult.getRecords();
        //总记录数
        long total = pageResult.getSize();
        //根据接口，需要创建一个数据结果类
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(items,
                total,
                pageParams.getPageNo(),
                pageParams.getPageSize());
        System.out.println(courseBasePageResult);

    }
}
