package com.ccthanking.spflow;
/**
 * 
 * @author
 * @des    审批归档自定义异常类,误删
 */
public class CustomException extends Exception
{
   public CustomException(String errmsg)
   {
     super(errmsg);	   
   }
}
