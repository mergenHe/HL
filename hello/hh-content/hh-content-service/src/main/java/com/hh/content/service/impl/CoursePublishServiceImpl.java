package com.hh.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.hh.base.exception.CommonError;
import com.hh.base.exception.HHException;
import com.hh.content.mapper.CourseBaseMapper;
import com.hh.content.mapper.CourseMarketMapper;
import com.hh.content.mapper.CoursePublishMapper;
import com.hh.content.mapper.CoursePublishPreMapper;
import com.hh.content.model.dto.CourseBaseInfoDto;
import com.hh.content.model.dto.CoursePreviewDto;
import com.hh.content.model.dto.TeachplanDto;
import com.hh.content.model.po.CourseBase;
import com.hh.content.model.po.CourseMarket;
import com.hh.content.model.po.CoursePublish;
import com.hh.content.model.po.CoursePublishPre;
import com.hh.content.service.CourseBaseInfoService;
import com.hh.content.service.CoursePublishService;
import com.hh.content.service.TeachplanService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.messagesdk.service.impl.MqMessageServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/9/16 15:37
 * @version 1.0
 */
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

 @Autowired
 CourseBaseInfoService courseBaseInfoService;

 @Autowired
 TeachplanService teachplanService;
@Autowired
 CourseBaseMapper courseBaseMapper;
@Autowired
 CourseMarketMapper courseMarketMapper;
@Autowired
 CoursePublishPreMapper coursePublishPreMapper;
@Autowired
CoursePublishMapper coursePublishMapper;
//@Autowired
//MqMessageService mqMessageService;
 @Override
 public CoursePreviewDto getCoursePreviewInfo(Long courseId) {

  //课程基本信息、营销信息
  CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);

  //课程计划信息
  List<TeachplanDto> teachplanTree= teachplanService.findTeachplanTree(courseId);

  CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
  coursePreviewDto.setCourseBase(courseBaseInfo);
  coursePreviewDto.setTeachplans(teachplanTree);
  return coursePreviewDto;
 }

 @Override
 public void commitAudit(Long companyId, Long courseId) {

  //约束校验
  CourseBase courseBase = courseBaseMapper.selectById(courseId);
  //课程审核状态
  String auditStatus = courseBase.getAuditStatus();
  //当前审核状态为已提交不允许再次提交
  if("202003".equals(auditStatus)){
   HHException.cast("当前为等待审核状态，审核完成可以再次提交。");
  }
  //本机构只允许提交本机构的课程
  if(!courseBase.getCompanyId().equals(companyId)){
   HHException.cast("不允许提交其它机构的课程。");
  }

  //课程图片是否填写
  if(StringUtils.isEmpty(courseBase.getPic())){
   HHException.cast("提交失败，请上传课程图片");
  }

  //添加课程预发布记录
  CoursePublishPre coursePublishPre = new CoursePublishPre();
  //课程基本信息加部分营销信息
  CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
  BeanUtils.copyProperties(courseBaseInfo,coursePublishPre);
  //课程营销信息
  CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
  //转为json
  String courseMarketJson = JSON.toJSONString(courseMarket);
  //将课程营销信息json数据放入课程预发布表
  coursePublishPre.setMarket(courseMarketJson);

  //查询课程计划信息
  List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
  if(teachplanTree.size()<=0){
   HHException.cast("提交失败，还没有添加课程计划");
  }
  //转json
  String teachplanTreeString = JSON.toJSONString(teachplanTree);
  coursePublishPre.setTeachplan(teachplanTreeString);

  //设置预发布记录状态,已提交
  coursePublishPre.setStatus("202003");
  //教学机构id
  coursePublishPre.setCompanyId(companyId);
  //提交时间
  coursePublishPre.setCreateDate(LocalDateTime.now());
  CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
  if(coursePublishPreUpdate == null){
   //添加课程预发布记录
   coursePublishPreMapper.insert(coursePublishPre);
  }else{
   coursePublishPreMapper.updateById(coursePublishPre);
  }

  //更新课程基本表的审核状态
  courseBase.setAuditStatus("202003");
  courseBaseMapper.updateById(courseBase);
 }
 @Transactional
 @Override
 public void publish(Long companyId, Long courseId) {

  //约束校验
  //查询课程预发布表
  CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
  if(coursePublishPre == null){
   HHException.cast("请先提交课程审核，审核通过才可以发布");
  }
  //本机构只允许提交本机构的课程
  if(!coursePublishPre.getCompanyId().equals(companyId)){
   HHException.cast("不允许提交其它机构的课程。");
  }


  //课程审核状态
  String auditStatus = coursePublishPre.getStatus();
  //审核通过方可发布
  if(!"202004".equals(auditStatus)){
   HHException.cast("操作失败，课程审核通过方可发布。");
  }

  //保存课程发布信息
  saveCoursePublish(courseId);

  //保存消息表
  saveCoursePublishMessage(courseId);

  //删除课程预发布表对应记录
  coursePublishPreMapper.deleteById(courseId);

 }
 private void saveCoursePublish(Long courseId){
  //整合课程发布信息
  //查询课程预发布表
  CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
  if(coursePublishPre == null){
   HHException.cast("课程预发布数据为空");
  }

  CoursePublish coursePublish = new CoursePublish();

  //拷贝到课程发布对象
  BeanUtils.copyProperties(coursePublishPre,coursePublish);
  coursePublish.setStatus("203002");
  CoursePublish coursePublishUpdate = coursePublishMapper.selectById(courseId);
  if(coursePublishUpdate == null){
   coursePublishMapper.insert(coursePublish);
  }else{
   coursePublishMapper.updateById(coursePublish);
  }
  //更新课程基本表的发布状态
  CourseBase courseBase = courseBaseMapper.selectById(courseId);
  courseBase.setStatus("203002");
  courseBaseMapper.updateById(courseBase);

//  mqMessageService.addMessage("course_publish",String.valueOf(courseId),null,null );
//
 }

 private void saveCoursePublishMessage(Long courseId){
  MqMessageService mqMessageService = new MqMessageServiceImpl();
  MqMessage mqMessage = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
  if(mqMessage==null){
   HHException.cast(CommonError.UNKOWN_ERROR);
  }
 }



}
