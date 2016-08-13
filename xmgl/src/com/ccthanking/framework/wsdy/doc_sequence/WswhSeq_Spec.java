package com.ccthanking.framework.wsdy.doc_sequence;

import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import java.sql.SQLException;
import java.sql.Connection;
/**
 *
 * @author andy 20080311
 *  增加一个类，当有特殊文号时，用这个类来配置
 */
public class WswhSeq_Spec
{
	public WswhSeq_Spec(){}

	public static String getWswhSeq_Spec(String ywlx, String sjbh, String bmbh ,String ws_template_id ,String hzms ,Connection conn ,String writerID ,String curYear)
	           throws SQLException
	{

		String seq = "123";
		switch(Integer.parseInt(ws_template_id))
		{
		   case 273:
                // 分局三级处理意见审批通过后出据的处罚决定书的文号
                // 文号范围：2020870051-2020884999
			     String temp273 = writerID;
			     if(Integer.parseInt(writerID)<=9999)
			     {
                     //不足4位补0
				     while(temp273.length()<4)
				    	 temp273 = "0"+temp273;
				     temp273 = "7"+temp273;
			     }else
			     {
			    	 int n = 70000+Integer.parseInt(writerID);
			    	 if(n>84999)
			    		 n = 70001;
			    	 temp273 = String.valueOf(n);
			     }
			     seq = "第 "+"202"+curYear.substring(2)+temp273+" 号";
		   break;
           case 2731:
               // 派出所一级处理意见审批通过后出据的处罚决定书文号
               // 文号范围：2020885000-2020899999
               String temp2731 = writerID;
               if(Integer.parseInt(writerID)<=999)
               {
                   //不足3位补0
                   while(temp2731.length()<3)
                       temp2731 = "0"+temp2731;
                   temp2731 = "85"+temp2731;
               }else
               {
                   int n = 85000+Integer.parseInt(writerID);
                   if(n>99999)
                       n = 85001;
                   temp2731 = String.valueOf(n);
               }
               seq = "第 "+"202"+curYear.substring(2)+temp2731+" 号";
           break;
		   case 253:
		   case 254:
                 //罚款缴纳通知书:3020850001 － 3020899999
			     //倒数第五位超过5，直接取writerID,否则就是"5"
			     String temp = writerID;
			     if(Integer.parseInt(writerID)<=9999)
			     {
                   //不足4位补0
				     while(temp.length()<4)
				    	 temp = "0"+temp;
				     temp = "5"+temp;
			     }else
			     {
			    	 int n = 50000+Integer.parseInt(writerID);
			    	 if(n>99999)
			    		 n = 50001;
			    	 temp = String.valueOf(n);
			     }
			     seq = "第 "+"302"+curYear.substring(2)+temp+" 号";
			break;
			case 358:
                // 强制戒毒决定书：05001 - 99999
                String temp358 = writerID;
                if(Integer.parseInt(writerID)<=999){
                    // 不足3位补0
                    while(temp358.length()<3)
                        temp358 = "0" + temp358;
                    temp358 = "05" + temp358;
                }else{
                    int n358 = 5000 + Integer.parseInt(writerID);
                    if(n358>99999)
                        n358 = 5001;
                    temp358 = String.valueOf(n358);
                    while(temp358.length()<5)
                        temp358 = "0" + temp358;
                }
                seq = hzms + "["+curYear+"]"+"第 "+temp358+" 号(电)";
                break;
            case 359:
                //社区戒毒决定书:00501 - 99999
                String temp359 = writerID;
                if(Integer.parseInt(writerID)<=99){
                    // 不足2位补0
                    while(temp359.length()<2)
                        temp359 = "0" + temp359;
                    temp359 = "005" + temp359;
                }else{
                    int n359 = 500 + Integer.parseInt(writerID);
                    if(n359 > 99999)
                        n359 = 501;
                    temp359 = String.valueOf(n359);
                    while(temp359.length()<5)
                        temp359 = "0" + temp359;
                }
                seq = hzms + "[" + curYear + "]" + "第 " + temp359 + " 号(电)";
                break;
            case 213:
            	//拘留通知书与拘留证同一文号
            	String temp213 = "";
            	String sql = " select a.wswh from pub_blob a,za_zfba_xsaj_aj_jlxxdj b,za_zfba_xsaj_aj_jlzxqkxx c ";
            	sql += " where a.sjbh = b.sjbh and a.ywlx = b.ywlx and b.jlxxxh = c.jlxxxh and c.sjbh='"+sjbh+"'";
            	try
            	{
	            	QuerySet qs = DBUtil.executeQuery(sql, null, conn);
	            	if(qs.getRowCount()>0)
	            	{
	            		String wswh = qs.getString(1, 1);
	            		int s = wswh.indexOf("第 ")+2;
	            		int e = wswh.indexOf(" 号");
	            		temp213 = wswh.substring(s, e);
	            	}else
            		   throw new Exception("文书文号生成错误：没有制作拘留证");
            	}catch(Exception e)
            	{
            		System.out.println("WswhSeq_Spec.getWswhSeq_Spec方法报错:"+e.getMessage());
            	}
            	seq = hzms + "[" + curYear + "]" + "第 " + temp213 + " 号(电)";
            	break;
             case 209:
            	//逮捕通知书与逮捕证同一文号
            	String temp209 = "";
            	String sql_db = " select a.wswh from pub_blob a,za_zfba_xsaj_aj_tqdbxx b,za_zfba_xsaj_aj_dbzxqkxx c ";
            	sql_db += " where a.sjbh = b.sjbh and a.ywlx = b.ywlx and b.tqdbxxxh = c.tqdbxxxh and c.sjbh='"+sjbh+"'";
            	try
            	{
	            	QuerySet qs = DBUtil.executeQuery(sql_db, null, conn);
	            	if(qs.getRowCount()>0)
	            	{
	            		String wswh = qs.getString(1, 1);
	            		int s = wswh.indexOf("第 ")+2;
	            		int e = wswh.indexOf(" 号");
	            		temp209 = wswh.substring(s, e);
	            	}else
            		   throw new Exception("文书文号生成错误：没有制作逮捕证");
            	}catch(Exception e)
            	{
            		System.out.println("WswhSeq_Spec.getWswhSeq_Spec方法报错:"+e.getMessage());
            	}
            	seq = hzms + "[" + curYear + "]" + "第 " + temp209 + " 号(电)";
            	break;
             case 330:
             	//提请逮捕意见书和提请逮捕意见书文稿同一文号
             	String temp3301 = "";
             	String sql3301 = "SELECT T3.WSWH FROM ZA_ZFBA_JCXX_WS_TYCQBG t1,ZA_ZFBA_JCXX_WS_WGB t2,PUB_BLOB T3 "
             		+ " WHERE T1.CQXH = T2.CQXH AND T3.SJBH = T2.SJBH AND T3.ZFBS = '0' AND T1.SJBH='"+sjbh+"'";
             	try
             	{
 	            	QuerySet qs = DBUtil.executeQuery(sql3301, null, conn);
 	            	if(qs.getRowCount()>0)
 	            	{
 	            		String wswh = qs.getString(1, 1);
 	            		int s = wswh.indexOf("第 ")+2;
 	            		int e = wswh.indexOf(" 号");
 	            		temp3301 = wswh.substring(s, e);
 	            	}else
             		   throw new Exception("文书文号生成错误：没有制作《提请批准逮捕书（文稿）》");
             	}catch(Exception e)
             	{
             		System.out.println("WswhSeq_Spec.getWswhSeq_Spec方法报错:"+e.getMessage());
             	}
             	if(!"".equals(temp3301)){
             		seq = hzms + "[" + curYear + "]" + " " + temp3301 + " 号";
             	}
             	break;
             case 373:
             	//移送起诉意见书和移送起诉意见书文稿同一文号
             	String temp3731 = "";
             	String sql3731 = "SELECT T1.WSWH FROM PUB_BLOB T1 WHERE T1.WS_TEMPLATE_ID = '328' " 
             		+ " AND T1.YWLX='040433' AND T1.ZFBS = '0' AND T1.SJBH='"+sjbh+"'";             	
             	try
             	{
 	            	QuerySet qs = DBUtil.executeQuery(sql3731, null, conn);
 	            	if(qs.getRowCount()>0)
 	            	{
 	            		String wswh = qs.getString(1, 1);
 	            		int s = wswh.indexOf("第 ")+2;
 	            		int e = wswh.indexOf(" 号");
 	            		temp3731 = wswh.substring(s, e);
 	            	}else
             		   throw new Exception("文书文号生成错误：没有制作《起诉意见书（文稿）》");
             	}catch(Exception e)
             	{
             		System.out.println("WswhSeq_Spec.getWswhSeq_Spec方法报错:"+e.getMessage());
             	}
             	if(!"".equals(temp3731)){
             		seq = hzms + "[" + curYear + "]" + " " + temp3731 + " 号";
             	}
             	break;             	
            case 3281:  // 提请逮捕意见书按照第九大队规则进行派号
            case 3282:  // 起诉意见书按照第九大队规则进行派号
            // modified by guanchb@2009-01-11 start
            // 解决问题：提请逮捕意见书（wsid=330）和起诉意见书（wsid=373）按照九大队提供的规则进行派号
                String temp330 = "";
                int n330 = Integer.parseInt(writerID) ;
                if( n330 > 999 )
                    n330 = n330 - 999 ;
                if(bmbh.equals("310115050400")){       // 刑侦支队一队（310115050400） 文号范围：刑一队：1001-1999
                    n330 = 1000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115050500")){ // 刑侦支队二队（310115050500） 文号范围：刑二队：2001-2999
                    n330 = 2000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115050600")){ // 刑侦支队三队（310115050600） 文号范围：刑三队：3001-3999
                    n330 = 3000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115050700")){ // 刑侦支队四队（310115050700） 文号范围：刑四队：4001-4999
                    n330 = 4000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115050800")){ // 刑侦支队五队（310115050800） 文号范围：刑五队：5001-5999
                    n330 = 5000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115050900")){ // 刑侦支队六队（310115050900） 文号范围：刑六队：无（不办案）
                    seq = "";
                    break;
                }else if(bmbh.equals("310115051000")){ // 刑侦支队七队（310115051000） 文号范围：刑七队：无（不办案）
                    seq = "";
                    break;
                }else if(bmbh.equals("310115051100")){ // 刑侦支队八队（310115051100） 文号范围：刑八队：8001-8999
                    n330 = 8000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115051200")){ // 刑侦支队九队（310115051200） 文号范围：刑九队：9001-9999
                    n330 = 9000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115051300")){ // 刑侦支队十队（310115051300） 文号范围：刑十队：7001-7999
                    n330 = 7000 + n330 ;
                    temp330 = String.valueOf(n330);
                }else if(bmbh.equals("310115051400")){ // 刑侦支队十一队（310115051400） 文号范围：刑十一队： （责）4001-（责）4999
                    n330 = 4000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115051500")){ // 刑侦支队十二队（310115051500） 文号范围：刑十二队： （责）5001-（责）5999
                    n330 = 5000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115051600")){ // 刑侦支队十三队（310115051600） 文号范围：刑十三队： （责）6001-（责）6999
                    n330 = 6000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115051700")){ // 刑侦支队十四队（310115051700） 文号范围：刑十四队： （责）7001-（责）7999
                    n330 = 7000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115051800")){ // 刑侦支队十五队（310115051800） 文号范围：刑十五队： （责）8001-（责）8999
                    n330 = 8000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115051900")){ // 刑侦支队十六队（310115051900） 文号范围：刑十六队： （责）9001-（责）9999
                    n330 = 9000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115052000")){ // 刑侦支队十七队（310115052000） 文号范围：刑十七队： （责）3001-（责）3999
                    n330 = 3000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }else if(bmbh.equals("310115052100")){ // 刑侦支队十八队（310115052100） 文号范围：刑十八队： （责）2001-（责）2999
                    n330 = 2000 + n330 ;
                    temp330 = "（责）"+String.valueOf(n330);
                }
                // add by guanchb@2009-02-06:如果部门不是刑队，返回空串 start
                if(!temp330.equals("")){
                	seq = hzms + "[" + curYear + "]" + "第 " + temp330 + " 号（电）";
                }
                // add by guanchb@2009-02-06:end
                break;
            case 3381: // 按照九队规则重新设定《退回补充侦查文稿》文书文号规则
                String temp3381 = "";
                int n3381 = Integer.parseInt(writerID) ;
                if(n3381 > 899)
                	n3381 = n3381 - 899 ;
                if(bmbh.equals("310115050400")){ // 刑侦支队一队（310115050400） 文号范围：沪公浦刑补侦字第[2009]1101号——1999号
                	n3381 = 1100 + n3381 ;
                	temp3381 = String.valueOf(n3381);
                }else if(bmbh.equals("310115050500") || bmbh.equals("310115050700") || bmbh.equals("310115050900") || bmbh.equals("310115051000") || bmbh.equals("310115051100") || bmbh.equals("310115052000") || bmbh.equals("310115052100")){ 
                	//     刑侦支队二队（310115050500）、    刑侦支队四队（310115050700）、      刑侦支队六队（310115050900）、      刑侦支队七队（310115051000）、    刑侦支队八队（310115051100） 、  刑侦支队十七队（310115052000）  、 刑侦支队十八队（310115052100）
					// 文号范围：无
                	seq = "";
                	break;
                }else if(bmbh.equals("310115050600")){ // 刑侦支队三队（310115050600） 文号范围：沪公浦刑补侦字第[2009]3101号——3999号
                	n3381 = 3100 + n3381 ;
                	temp3381 = String.valueOf(n3381);
                }else if(bmbh.equals("310115050800")){ // 刑侦支队五队（310115050800） 文号范围：沪公浦刑补侦字第[2009]5101号——5999号
                	n3381 = 5100 + n3381 ;
                	temp3381 = String.valueOf(n3381);
                }else if(bmbh.equals("310115051200")){ // 刑侦支队九队（310115051200） 文号范围：沪公浦刑补侦字第[2009]9101号——9999号
                	n3381 = 9100 + n3381 ;
                	temp3381 = String.valueOf(n3381);
                }else if(bmbh.equals("310115051300")){ // 刑侦支队十队（310115051300） 文号范围：沪公浦刑补侦字第[2009]7101号——7999号
                	n3381 = 7100 + n3381 ;
                	temp3381 = String.valueOf(n3381);
                }else if(bmbh.equals("310115051400")){ // 刑侦支队十一队（310115051400） 文号范围：沪公浦刑补侦字第[2009]（责）4101号——4999号
                	n3381 = 4100 + n3381 ;
                	temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115051500")){ // 刑侦支队十二队（310115051500） 文号范围：沪公浦刑补侦字第[2009]（责）5101号——5999号
                	n3381 = 5100 + n3381 ;
                	temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115051600")){ // 刑侦支队十三队（310115051600） 文号范围：沪公浦刑补侦字第[2009]（责）6101号——6999号
                	n3381 = 6100 + n3381 ;
                	temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115051700")){ // 刑侦支队十四队（310115051700） 文号范围：沪公浦刑补侦字第[2009]（责）7101号——7999号
                	n3381 = 7100 + n3381 ;
                	temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115051800")){ // 刑侦支队十五队（310115051800） 文号范围：沪公浦刑补侦字第[2009]（责）8101号——8999号
                	n3381 = 8100 + n3381 ;
                	temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115051900")){ // 刑侦支队十六队（310115051900） 文号范围：沪公浦刑补侦字第[2009]（责）9101号——9999号
                	n3381 = 9100 + n3381 ;
                	temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115052000")){ // 刑侦支队十七队（310115052000） 文号范围：沪公浦刑补侦字第[2009]（责）3001号——3999号
                	n3381 = 3000 + n3381 ;
                    temp3381 = "（责）"+String.valueOf(n3381);
                }else if(bmbh.equals("310115052100")){ // 刑侦支队十八队（310115052100） 文号范围：沪公浦刑补侦字第[2009]（责）2001号——2999号
                	n3381 = 2000 + n3381 ;
                    temp3381 = "（责）"+String.valueOf(n3381);
                }
                if(!temp3381.equals("")){
                	seq = "沪公浦刑补侦字第" + "[" + curYear + "]" + temp3381 + "号（电）";
                }                
            	break;
            case 3401://退查公函文书给号规则（按九队规则派号）
            	String selWswh = "SELECT WSWH FROM PUB_BLOB WHERE SJBH='"+sjbh+"' AND YWLX='040434' AND WS_TEMPLATE_ID='338' AND ZFBS='0' ";
            	String wswh = "";
            	try{
	            	String[][] selWswhRes = DBUtil.query(conn, selWswh);
	            	if(selWswhRes == null)
	            		throw new Exception("获取退查文稿文书文号失败！");
	            	wswh = selWswhRes[0][0];
            	}catch(Exception e){
            		System.out.println("WswhSeq_Spec.getWswhSeq_Spec方法报错:"+e.getMessage());
            	}
            	if(!"".equals(wswh)){
            		seq = wswh ;
            	}
            	break;
            case 340:// 通用公函文书给号规则（按九队规则派号）
            	String temp340 = "";
                int n340 = Integer.parseInt(writerID) ;
                if(n340 > 899)
                	n340 = n340 - 899 ;
                if(bmbh.equals("310115050400")){// 刑侦支队一队（310115050400） 文号范围：沪公浦刑函字第[2009]1101号——1999号
                	n340 = 1100 + n340 ;
                	temp340 = String.valueOf(n340);
                }else if(bmbh.equals("310115050500") || bmbh.equals("310115050700") || bmbh.equals("310115050900") || bmbh.equals("310115051000") || bmbh.equals("310115051100") || bmbh.equals("310115052000") || bmbh.equals("310115052100")){ 
                	//     刑侦支队二队（310115050500）、    刑侦支队四队（310115050700）、      刑侦支队六队（310115050900）、      刑侦支队七队（310115051000）、    刑侦支队八队（310115051100） 、  刑侦支队十七队（310115052000）  、 刑侦支队十八队（310115052100）
					// 文号范围：无
                	seq = "";
                	break;
                }else if(bmbh.equals("310115050600")){ // 刑侦支队三队（310115050600） 文号范围：沪公浦刑函字第[2009]3101号——3999号
                	n340 = 3100 + n340 ;
                	temp340 = String.valueOf(n340);                	
                }else if(bmbh.equals("310115050800")){ // 刑侦支队五队（310115050800） 文号范围：沪公浦刑函字第[2009]5101号——5999号
                	n340 = 5100 + n340 ;
                	temp340 = String.valueOf(n340);                   	
                }else if(bmbh.equals("310115051200")){ // 刑侦支队九队（310115051200） 文号范围：沪公浦刑函字第[2009]9101号——9999号
                	n340 = 9100 + n340 ;
                	temp340 = String.valueOf(n340);                 	
                }else if(bmbh.equals("310115051300")){ // 刑侦支队十队（310115051300） 文号范围：沪公浦刑函字第[2009]7101号——7999号
                	n340 = 7100 + n340 ;
                	temp340 = String.valueOf(n340);                 	
                }else if(bmbh.equals("310115051400")){ // 刑侦支队十一队（310115051400） 文号范围：沪公浦刑函字第[2009]（责）4101号——4999号
                	n340 = 4100 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);                	
                }else if(bmbh.equals("310115051500")){ // 刑侦支队十二队（310115051500） 文号范围：沪公浦刑函字第[2009]（责）5101号——5999号
                	n340 = 5100 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);                 	
                }else if(bmbh.equals("310115051600")){ // 刑侦支队十三队（310115051600） 文号范围：沪公浦刑函字第[2009]（责）6101号——6999号
                	n340 = 6100 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);                 	
                }else if(bmbh.equals("310115051700")){ // 刑侦支队十四队（310115051700） 文号范围：沪公浦刑函字第[2009]（责）7101号——7999号
                	n340 = 7100 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);                 	
                }else if(bmbh.equals("310115051800")){ // 刑侦支队十五队（310115051800） 文号范围：沪公浦刑函字第[2009]（责）8101号——8999号
                	n340 = 8100 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);                	
                }else if(bmbh.equals("310115051900")){ // 刑侦支队十六队（310115051900） 文号范围：沪公浦刑函字第[2009]（责）9101号——9999号
                	n340 = 9100 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);                	
                }else if(bmbh.equals("310115052000")){ // 刑侦支队十七队（310115052000） 文号范围：沪公浦刑补侦字第[2009]（责）3001号——3999号
                	n340 = 3000 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);
                }else if(bmbh.equals("310115052100")){ // 刑侦支队十八队（310115052100） 文号范围：沪公浦刑补侦字第[2009]（责）2001号——2999号
                	n340 = 2000 + n340 ;
                	temp340 = "（责）"+String.valueOf(n340);
                }
                if(!temp340.equals("")){//沪公浦刑函字第[2009]（责）9101号——9999号
                	seq = hzms + "第" + "[" + curYear + "]" + temp340 + "号（电）";
                }             	
            	break;
            // modified by guanchb@2009-01-11  end 
		}
		return seq;
	}
}