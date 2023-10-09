package com.hh.base.exception;

public class HHException extends RuntimeException {

   private String errMessage;

   public HHException() {
      super();
   }

   public HHException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   public String getErrMessage() {
      return errMessage;
   }

   public static void cast(CommonError commonError){
       throw new HHException(commonError.getErrMessage());
   }
   public static void cast(String errMessage){
       throw new HHException(errMessage);
   }

}
