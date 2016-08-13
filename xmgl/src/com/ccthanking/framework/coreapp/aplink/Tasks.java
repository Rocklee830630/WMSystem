//Source file: D:\\projects\\rkyw\\src\\com\\ccthanking\\aplink\\Tasks.java

package com.ccthanking.framework.coreapp.aplink;


/**
@author leo <oss@tom.com>
任务类型，例如 ‘出生登记’，‘死亡注销’ 等
 */
public class Tasks
{
				private int operationOID;
				private String eventType;
				private String memo;
                private String dwdm;
                private String fjdm;
                private String sjdm;

                public void setDwdm(String dwdm)
                {
                  this.dwdm = dwdm;
                }
                public void setFjdm(String fjdm)
                {
                  this.fjdm = fjdm;
                }
                public void setSjdm(String sjdm)
                {
                  this.sjdm = sjdm;
                }
                public String getDwdm()
                {
                  return this.dwdm;
                }
                public String getFjdm()
                {
                  return this.fjdm;
                }
                public String getSjdm()
                {
                  return this.sjdm;
                }

				/**
				@roseuid 42DF713B028D
				 */
				public Tasks()
				{

				}

				/**
				Access method for the operationOID property.

				@return   the current value of the operationOID property
				 */
				public int getOperationOID()
				{
								return operationOID;
				}

				/**
				Sets the value of the operationOID property.

				@param aOperationOID the new value of the operationOID property
				 */
				public void setOperationOID(int aOperationOID)
				{
								operationOID = aOperationOID;
				}

				/**
				Access method for the eventType property.

				@return   the current value of the eventType property
				 */
				public String getEventType()
				{
								return eventType;
				}

				/**
				Sets the value of the eventType property.

				@param aEventType the new value of the eventType property
				 */
				public void setEventType(String aEventType)
				{
								eventType = aEventType;
				}

				/**
				Access method for the memo property.

				@return   the current value of the memo property
				 */
				public String getMemo()
				{
								return memo;
				}

				/**
				Sets the value of the memo property.

				@param aMemo the new value of the memo property
				 */
				public void setMemo(String aMemo)
				{
								memo = aMemo;
				}
}
