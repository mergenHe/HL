package com.hh.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @description 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

   @ResponseBody
   @ExceptionHandler(HHException.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse customException(HHException e) {
      log.error("【系统异常】{}",e.getErrMessage(),e);
      return new RestErrorResponse(e.getErrMessage());
      //return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

   }

   @ResponseBody
   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse exception(Exception e) {

      log.error("【系统异常】{}",e.getMessage(),e);

      return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

   }
   public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
      BindingResult bindingResult = e.getBindingResult();
      List<String> msgList = new ArrayList<>();
      //将错误信息放在msgList
      bindingResult.getFieldErrors().stream().forEach(item->msgList.add(item.getDefaultMessage()));
      //拼接错误信息
      String msg = StringUtils.join(msgList, ",");
      log.error("【系统异常】{}",msg);
      return new RestErrorResponse(msg);
   }

}