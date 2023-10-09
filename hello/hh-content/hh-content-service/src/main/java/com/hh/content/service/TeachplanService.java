package com.hh.content.service;

import com.hh.content.model.dto.BindTeachplanMediaDto;
import com.hh.content.model.dto.SaveTeachplanDto;
import com.hh.content.model.dto.TeachplanDto;
import com.hh.content.model.po.TeachplanMedia;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TeachplanService {

/**
 * @description 查询课程计划树型结构
 * @param courseId  课程id
 * @return List<TeachplanDto>
 * @author Mr.M
 * @date 2022/9/9 11:13
*/
 public List<TeachplanDto> findTeachplanTree(long courseId);
 public void saveTeachplan(SaveTeachplanDto teachplanDto);
 /**
  * @description 教学计划绑定媒资
  * @param bindTeachplanMediaDto
  * @return com.xuecheng.content.model.po.TeachplanMedia
  * @author Mr.M
  * @date 2022/9/14 22:20
  */
 public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);



}
