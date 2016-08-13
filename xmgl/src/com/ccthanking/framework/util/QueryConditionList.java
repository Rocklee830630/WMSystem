package com.ccthanking.framework.util;

import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * 根据条件组织查询语句. <br>
 * 需在页面中用xml或json传入查询条件. <code>
{
    "querycondition": {
        "conditions": [
            {
                "value": "2013",
                "fieldname": "ND",
                "fieldformat":"yyyy",
                "fieldtype":"year",
                "operation": "=",
                "logic": "and"
            }
        ]
    }
}
</code>
 * 
 * @author <a href="mailto:jianggl88@gmail.com">蒋根亮</a>
 * @version v1.00
 * @since 1.00 2013-9-2
 * @see com.ccthanking.framework.util.RequestUtil#getConditionList(String);
 * 
 */
public class QueryConditionList {
    public QueryConditionList() {
    }

    private ArrayList arrquery = null;

    private ArrayList alExpQuery = null;

    private ArrayList<String> alOrQuerys = new ArrayList();

    private HashMap<String, ArrayList<String>> orQuerys = new HashMap();

    public void addCondition(QueryCondition condition) {
        if (arrquery == null)
            this.arrquery = new ArrayList();
        arrquery.add(condition);
    }

    public int size() {
        if (arrquery == null)
            return -1;
        else
            return arrquery.size();
    }

    public QueryCondition getQueryCondition(int index) {
        if (arrquery == null)
            return null;
        if (index < arrquery.size()) {
            return (QueryCondition) arrquery.get(index);
        }
        return null;
    }
    
    public void deleteQueryCondition(int index) {
    	 if (arrquery != null)
         if (index < arrquery.size()) {
              arrquery.remove(index);
         }
    }

    public void addExpCondition(ExpCondition p_Condition) {
        if (alExpQuery == null)
            alExpQuery = new ArrayList();
        alExpQuery.add(p_Condition);
    }

    private String getDateCondition(String p_Type, String p_Operation, String p_Value) {
        String format = "'YYYYMMDDHH24MISS'";
        String strResult = "";
        if ("like".equalsIgnoreCase(p_Operation))
            p_Operation = "=";
        if (p_Value == null || "".equals(p_Value))
            p_Value = "0";
        if ("1".equals(p_Type)) {
            int realYear = DateTimeUtil.getCurrentYear() - Integer.parseInt(p_Value);
            if (">".equals(p_Operation))
                strResult = " < to_date('" + realYear + "0101000000" + "'," + format + ")";
            else if (">=".equals(p_Operation))
                strResult = " <= to_date('" + realYear + "1231235959" + "'," + format + ")";
            else if ("<".equals(p_Operation))
                strResult = " > to_date('" + realYear + "1231235959" + "'," + format + ")";
            else if ("<=".equals(p_Operation))
                strResult = " >= to_date('" + realYear + "0101000000" + "'," + format + ")";
            else
                strResult = " between to_date('" + realYear + "0101000000" + "'," + format + ") and to_date('" + realYear + "1231235959"
                        + "'," + format + ")";
        } else if ("2".equals(p_Type)) {
            String[] arrAge = p_Value.split("-");
            int intAge1 = 0;
            int intAge2 = 150;
            try {
                if (arrAge.length == 1)
                    intAge1 = Integer.parseInt(arrAge[0]);
                else if (arrAge.length == 2) {
                    if (arrAge[0] != "")
                        intAge1 = Integer.parseInt(arrAge[0]);
                    if (arrAge[1] != "")
                        intAge2 = Integer.parseInt(arrAge[1]);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            int realAge1Year = DateTimeUtil.getCurrentYear() - intAge1;
            int realAge2Year = DateTimeUtil.getCurrentYear() - intAge2;
            strResult = " between to_date('" + realAge2Year + "0101000000" + "'," + format + ") and to_date('" + realAge1Year
                    + "1231235959" + "'," + format + ")";
        } else
            strResult = " " + p_Operation + " to_date('" + p_Value + "'," + format + ")";
        return strResult;
    }

    private String getRowCondition(QueryCondition objTempQuery) {
        String strRowCondition = null;
        String strField = objTempQuery.getField();
        String strOperation = objTempQuery.getOperation();
        String strValue = objTempQuery.getValue();
        String strKind = objTempQuery.getKind();
        String strFormat = objTempQuery.getFieldFormat();

        if (strKind.equalsIgnoreCase("text")) {
            if ("3".equals(strFormat)) {
                strValue = com.ccthanking.framework.coreapp.orgmanage.SpellCache.getInstance().getAspell(strValue);
                strField = " toASpell(" + strField + ")";
            }
            if ("6".equals(strFormat)) {
                strValue = com.ccthanking.framework.coreapp.orgmanage.SpellCache.getInstance().getSpell(strValue);
                strField = " toSpell(" + strField + ")";
            } else if ("4".equals(strFormat))
                strValue = com.ccthanking.framework.coreapp.orgmanage.SpellCache.getInstance().getSpell(strValue);
            else if ("5".equals(strFormat))
                strValue = com.ccthanking.framework.coreapp.orgmanage.SpellCache.getInstance().getAspell(strValue);
            // 尾号去零功能
            if ("7".equals(strFormat)) {
                strValue = Pub.trimChar(strValue, '0', 0) + "%";
                strOperation = " like ";
            }
            if (strOperation.trim().equalsIgnoreCase("in") || strOperation.trim().equalsIgnoreCase("not in")) {
                strValue = strValue.replaceAll(",", "','");
                strRowCondition = strField + " " + strOperation + " ('" + strValue + "')";
            } else if (strOperation.equalsIgnoreCase("is") && strValue.equalsIgnoreCase("null")) {
                strRowCondition = strField + " " + strOperation + "  " + strValue;
            } else
                strRowCondition = strField + " " + strOperation + " '" + strValue + "'";
        } else if (strKind.equalsIgnoreCase("date")) {
            if ("1".equals(strFormat) || "2".equals(strFormat)) {
                strRowCondition = strField + getDateCondition(strFormat, strOperation, strValue);
            } else {
                strFormat = "YYYYMMDDHH24MISS";
                if (strValue != null && strValue.indexOf("-") >= 0) {
                    strValue = strValue.replaceAll("-", "");
                }
                if (strOperation.trim().equals("=")) {
                    strRowCondition = strField + " >= to_date('" + strValue + "000000','" + strFormat + "') and " + strField
                            + " <= to_date('" + strValue + "235959','" + strFormat + "')";
                } else if (strOperation.equals("!=") || strOperation.equals("<>")) {
                    strRowCondition = strField + " <= to_date('" + strValue + "000000','" + strFormat + "') or " + strField
                            + " >= to_date('" + strValue + "235959','" + strFormat + "')";
                } else if (strOperation.equals(">")) {
                    strRowCondition = strField + " > to_date('" + strValue + "235959','" + strFormat + "')";
                } else if (strOperation.equals("<")) {
                    strRowCondition = strField + " < to_date('" + strValue + "000000','" + strFormat + "')";
                } else if (strOperation.equals(">=")) {
                    strRowCondition = strField + " >= to_date('" + strValue + "000000','" + strFormat + "')";
                } else if (strOperation.trim().equals("<=")) {
                    strRowCondition = strField + " <= to_date('" + strValue + "235959','" + strFormat + "')";
                } else {
                    strRowCondition = strField + " " + strOperation + " to_date('" + strValue + "','" + strFormat + "')";
                }
            }
        } else if (strKind.equalsIgnoreCase("datetime")) {
            strFormat = "YYYYMMDDHH24MISS";
            strValue = "to_date('" + strValue + "','" + strFormat + "')";
            strRowCondition = strField + " " + strOperation + " " + strValue;
        } else if (strKind.equalsIgnoreCase("month")) {
            strFormat = "YYYY-MM";
            strValue = "to_date('" + strValue + "','" + strFormat + "')";
            strRowCondition = strField + " " + strOperation + " " + strValue;
        } else if (strKind.equalsIgnoreCase("year")) {
            strFormat = "YYYY";
            // strValue = "to_date('" + strValue + "','" + strFormat + "')";
            // strRowCondition = strField + " " + strOperation + " " + strValue;
            strRowCondition = "'" + strValue + "' " + strOperation;
            strValue = " to_char(" + strField + ",'" + strFormat + "')";
            strRowCondition += strValue;
        } else if (strKind.equalsIgnoreCase("int")){
            strRowCondition = strField + " " + strOperation + " " + strValue;
        } else if (strKind.equalsIgnoreCase("query")) {
        	if (strOperation.trim().equalsIgnoreCase("in") || strOperation.trim().equalsIgnoreCase("not in")) {
        		strValue = strValue.replaceAll("''", "'");
                strRowCondition = strField + " " + strOperation + " (" + strValue + ")";
            } 
        }else{
            strRowCondition = strField + " " + strOperation + " '" + strValue + "'";
        }
        return strRowCondition;
    }

    public String getConditionWhere() {
        String strWhere = "";
        if (arrquery != null) {
            String strRowCondition = null;
            QueryCondition objTempQuery = null;
            if (alExpQuery != null && alExpQuery.size() > 0) {
                for (int i = 0; i < alExpQuery.size(); i++) {
                    ExpCondition objTempExp = (ExpCondition) alExpQuery.get(i);
                    int iIndex = Integer.parseInt(objTempExp.getItemValue()) - 1;
                    objTempQuery = (QueryCondition) arrquery.get(iIndex);
                    strRowCondition = null;
                    if (objTempQuery != null)
                        strRowCondition = getRowCondition(objTempQuery);
                    if (objTempExp != null && strRowCondition != null) {
                        strWhere += objTempExp.getStrLogic() + objTempExp.getLeftSign() + strRowCondition + objTempExp.getRightSign();
                    }
                }
            } else {
                for (int i = 0; i < arrquery.size(); i++) {
                    objTempQuery = (QueryCondition) arrquery.get(i);
                    strRowCondition = null;
                    if (objTempQuery != null)
                        strRowCondition = getRowCondition(objTempQuery);
                    if (strRowCondition == null)
                        continue;
                    if (objTempQuery.getLogic().equals("or")) {
                        if (orQuerys.containsKey(objTempQuery.getField())) {
                            ArrayList<String> al = orQuerys.get(objTempQuery.getField());
                            al.add(strRowCondition);
                        } else {
                            ArrayList<String> al = new ArrayList();
                            al.add(strRowCondition);
                            orQuerys.put(objTempQuery.getField(), al);
                        }
                        // alOrQuerys.add(strRowCondition);
                        continue;
                    }
                    if (strWhere.length() > 0)
                        // strWhere += " and " + strRowCondition;
                        strWhere += " " + objTempQuery.getLogic() + " " + strRowCondition;
                    else
                        strWhere += strRowCondition;
                }
                // if (alOrQuerys.size()>0){
                // if (strWhere.length() > 0)
                // //strWhere += " and " + strRowCondition;
                // strWhere +=" and (";
                // else
                // strWhere += " ( ";
                // String orWhere="";
                // for (int i=0;i<alOrQuerys.size();i++){
                // if (orWhere.length() > 0)
                // //strWhere += " and " + strRowCondition;
                // orWhere +=" or "+ alOrQuerys.get(i);
                // else
                // orWhere += alOrQuerys.get(i);
                // }
                // strWhere=strWhere+orWhere+" )";
                // }
                String orQueryCondition = generateOrQuery(orQuerys);
                if (strWhere.length() > 0 && orQueryCondition.length() > 0)

                    strWhere += " and " + orQueryCondition;
                else
                    strWhere += orQueryCondition;

            }
        }
        if (StringUtils.isBlank(strWhere)) {
            strWhere = " 1=1 ";
        }
        return strWhere;
    }

    private String generateOrQuery(Map orQuerys) {
        String result = "";
        Iterator ite = orQuerys.keySet().iterator();
        while (ite.hasNext()) {
            if ("".equals(result)) {
                result += " ( ";
            } else {
                result += " and ( ";

            }

            ArrayList<String> al = (ArrayList<String>) orQuerys.get(ite.next());
            String orWhere = "";
            for (int i = 0; i < al.size(); i++) {
                if (orWhere.length() > 0)
                    // strWhere += " and " + strRowCondition;
                    orWhere += " or " + al.get(i);
                else
                    orWhere += al.get(i);
            }
            result += orWhere + " ) ";
        }
        return result;
    }

}
