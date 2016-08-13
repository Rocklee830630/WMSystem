package com.ccthanking.framework.util;

/**
 * <p>Title: ppx</p>
 * <p>Description: 实现菜单排序</p>
 * @version 1.0
 */

import java.util.*;

import com.ccthanking.framework.common.*;
public class MenuComparator
    implements Comparator{

     public int compare(Object o1,Object o2) {
      MenuVo m1=(MenuVo)o1;
      MenuVo m2=(MenuVo)o2;
      if(Integer.parseInt(m1.getOrderNo())< Integer.parseInt(m2.getOrderNo()))
          return 0;
      else
          return 1;
     }

}

