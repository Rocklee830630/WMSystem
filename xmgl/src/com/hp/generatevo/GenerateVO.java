package com.hp.generatevo;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 生成工作中用到的VO对象
 * 目前只能对单主键的表结构进行处理
 * @author hp
 * 2010-7-28
 */
public class GenerateVO {
    
    public static void main(String[] args) {
        GenerateVO gv = new GenerateVO();
        gv.setTableName("GC_TCJH_XMXDK_SP");
        gv.setJavaFilePath("E:\\");
        gv.doGenerateVo();
    }
    
    
    private String tableName = null;
    private String javaFilePath = null;
    StringBuffer fieldInit = new StringBuffer();
    StringBuffer fieldRQ = new StringBuffer();
    StringBuffer fieldMethod = new StringBuffer();
    StringBuffer fieldPKAndTableName = new StringBuffer();
    
    public GenerateVO(){}
    public GenerateVO(String tableName){
        setTableName(tableName);
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    public String getTableName(){
        return this.tableName;
    }
    public void setJavaFilePath(String path){
        this.javaFilePath = path;
    }
    public String getJavaFilePath(){
        return this.javaFilePath;
    }

    
    /**
     * 根据表名称得到VO名称
     * @return
     */
    public String getVOName(){
        String[] tempName = tableName.split("_");
        String javaName = "";
        for(String s : tempName){
            javaName += s.substring(0,1).toUpperCase()+s.substring(1).toLowerCase();
        }
        javaName = javaName + "VO";
        return javaName;
    }
    
    /**
     * 生成VO方法
     */
    public void doGenerateVo(){
        if(null == tableName){
            System.out.println("没有设置要生成的表名!");
            return;
        }
        if(null == javaFilePath){
            System.out.println("没有设置要生成java文件的路径!");
            return;
        }
        
        System.out.println("生成VO文件开始!");
        doGenerateVO();
        
        try {
            DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(getJavaFilePath()+getVOName()+".java")));
            StringBuffer temp = new StringBuffer();
            temp.append("package com.ccthanking.common.vo;");
            temp.append("\n");
            temp.append("import com.ccthanking.framework.base.BaseVO;");
            temp.append("\n");
            temp.append("import java.util.Date;");
            temp.append("\n\n");
            temp.append("public class "+getVOName()+" extends BaseVO{");
            temp.append("\n\n");
            appendTab(temp,1);
            temp.append("public "+getVOName()+"(){");
            temp.append("\n");
            
            out.writeBytes(temp.toString());
            out.writeBytes(fieldInit.toString());
            out.writeBytes(fieldRQ.toString());
            out.writeBytes(fieldPKAndTableName.toString());
            temp = new StringBuffer();
            appendTab(temp,1);
            temp.append("}");
            temp.append("\n\n");
            out.writeBytes(temp.toString());
            out.writeBytes(fieldMethod.toString());
            temp = new StringBuffer();
            temp.append("}");
            out.writeBytes(temp.toString());
            out.close();
            System.out.println("生成VO文件结束!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    
    public void doGenerateVO(){
        
        if(null == tableName){
            System.out.println("没有设置要生成的表名!");
            return;
        }

        String sql = "select * from " + tableName + " where 1=2";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        DatabaseMetaData dmd = null;  
        String pkColumn = null;
        boolean isGotPk = false;
        
        conn = getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            dmd = conn.getMetaData();
            rs = dmd.getPrimaryKeys(null,"XMGL",tableName);  
            while(rs.next()){  
                pkColumn = rs.getString(4).toUpperCase();  
            }  
            
            for(int i=1;i<=rsmd.getColumnCount();i++){
                String columnName = rsmd.getColumnName(i).toUpperCase();
                int columnType = rsmd.getColumnType(i);
                if(!isGotPk && null != pkColumn){
                    if(columnName.equals(pkColumn)){
                        appendTab(fieldInit,2);
                        fieldInit.append("this.addField(\""+columnName+"\","+getFiledType(columnType)+"|this.TP_PK);");
                        fieldInit.append("\n");
                        isGotPk = true;
                    }else{
                        appendTab(fieldInit,2);
                        fieldInit.append("this.addField(\""+columnName+"\","+getFiledType(columnType)+");");
                        fieldInit.append("\n");
                    }
                }else{
                    appendTab(fieldInit,2);
                    fieldInit.append("this.addField(\""+columnName+"\","+getFiledType(columnType)+");");
                    fieldInit.append("\n");
                }
                doGenerateRQ(fieldRQ,columnName,columnType);
                appendTab(fieldMethod,1);
                fieldMethod.append("public void set"+columnName.substring(0,1)+
                        columnName.substring(1).toLowerCase()+"(" +getFiledParaType(columnType)+ " "+columnName.toLowerCase()+"){");
                fieldMethod.append("\n\t");
                appendTab(fieldMethod,1);
                fieldMethod.append("this.setInternal(\""+columnName+"\","+columnName.toLowerCase()+");");
                fieldMethod.append("\n");
                appendTab(fieldMethod,1);
                fieldMethod.append("}");
                fieldMethod.append("\n");
                appendTab(fieldMethod,1);
                fieldMethod.append("public " +getFiledParaType(columnType)+ " get"+columnName.substring(0,1)
                        +columnName.substring(1).toLowerCase()+"(){");
                fieldMethod.append("\n\t");
                appendTab(fieldMethod,1);
                fieldMethod.append("return (" +getFiledParaType(columnType)+ ")this.getInternal(\""+columnName+"\");");
                fieldMethod.append("\n");
                appendTab(fieldMethod,1);
                fieldMethod.append("}");
                fieldMethod.append("\n");
            }
            appendTab(fieldPKAndTableName,2);
            fieldPKAndTableName.append("this.setVOTableName(\""+tableName+"\");");
            fieldPKAndTableName.append("\n");
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    
    /**
     * 获得字段set和get的参数类型
     * @param type
     * @return
     */
    private String getFiledParaType(int type){
        String result = "String";
        switch(type){
        case java.sql.Types.DATE:
            result = "Date";
            break;
        case java.sql.Types.BLOB:
            result = "byte[]";
            break;
        case java.sql.Types.VARCHAR:
            result = "String";
            break;
        default:
            break;
        }
        return result;
    }
    
    /**
     * 获得字段类型
     * @param type
     * @return
     */
    private String getFiledType(int type){
        String result = "OP_STRING";
        switch(type){
        case java.sql.Types.DATE:
            result = "OP_DATE";
            break;
        case java.sql.Types.BLOB:
            result = "OP_BLOB";
            break;
        default:
            break;
        }
        return result;
    }
    
    private void doGenerateRQ(StringBuffer sb,String name,int type){
        switch(type){
        case java.sql.Types.DATE:
            appendTab(sb,2);
            sb.append("this.setFieldDateFormat(\""+name+"\",\"yyyy-MM-dd\");");
            sb.append("\n");
            break;
        default:
            break;
        }
    }
    
    /**
     * 获取数据库连接
     * @return
     */
    private Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName ("oracle.jdbc.driver.OracleDriver"); 
            String url="jdbc:oracle:thin:@192.168.1.199:1521:ORCL"; //orcl为数据库的SID 
            String user="xmgl"; 
            String password="xmgl"; 
        	/**
            Class.forName ("com.microsoft.jdbc.sqlserver.SQLServerDriver"); 
            String url="jdbc:sqlserver://10.31.18.238:1433;DatabaseName=idcard"; //orcl为数据库的SID 
            String user="sa"; 
            String password="sa"; 
            **/
            conn = DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
        }catch (SQLException e4) {
            e4.printStackTrace();
        }finally{
            
        }
        return conn;
    }
    
    /**
     * 增加tab控制
     * @param sb 要加入的字符串
     * @param js 要加入tab键的个数
     */
    private void appendTab(StringBuffer sb,int js){
        for(int i=0;i<js;i++){
            sb.append("\t");
        }
    }

}
