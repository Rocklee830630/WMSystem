//v20090409-1
//v20090409-2
package com.ccthanking.framework.coreapp.freequery.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 用于计算多表的关联关系
 * @author Administrator
 *
 */
public class Relation 
{
	//把表间关系构造成无向图
	private void buildNet(String[][] info)
	{
		Node pNode = new Node();
		for(int i=0; i<info.length; i++)
		{
			String[] obj = info[i];
			
			for(int j=0; j<2; j++)
			{
				Node node = (Node)vertex.get(obj[j]);
				if(node==null)
				{
					node = new Node();
					node.setName(obj[1-j]);
					vertex.put(obj[j], node);
				}
				else
				{
					while(null!=node)
					{
						if(null!=node && obj[1-j].equals(node.getName()))
						{
							break;
						}
						pNode = node;
						node = node.getNNode();
					}
					if(null==node)
					{
						node = new Node();
						node.setName(obj[1-j]);
						pNode.setNNode(node);
					}
				}
				
				/*
				 * 表间关联关系暂不添加
				 * 
				List<String> relations = node.getRelations();
				if(null==relations)
				{
					relations = new ArrayList<String>(0);
					node.setRelations(relations);
				}
				relations.add(obj[2]);
				*/
			}
		}
	}
	
	//深度遍历
	private void deepSearch(Map usedTname)
	{
		usedTables = usedTname;
		visited = new HashMap(0);
		Iterator iter = vertex.keySet().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			String flag = (String)usedTname.get(key);
			if(null!=flag && visited.get(key)==null)
			{
				recursion(key);
			}
		}
	}
	
	//递归方法
	private void recursion(String key)
	{
		if(visited.containsKey(key))
		{
			return;
		}
		
		Node node = (Node)vertex.get(key);
		while(null!=node)
		{
			visited.put(key, null);
			String flag = (String)usedTables.get(key);
			if(null!=flag)
				setTables();
			
			recursion(node.getName());
//			printVisitedMap();
			node = node.getNNode();
		}
		visited.remove(key);
	}

	//测试用的方法，输出无向图的邻接表存储结构
	private void printNet()
	{
		Iterator it = vertex.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			Node node = (Node)vertex.get(key);
			while(null!=node)
			{
				node = node.getNNode();
			}
		}
	}

	//测试用的方法，输出访问过的节点
	private void printVisitedMap()
	{
		Iterator it = visited.keySet().iterator();
		while(it.hasNext())
		{
		}
	}
	
	//添加有效节点
	private void setTables()
	{
		Iterator it = visited.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			if(!tMap.containsKey(key))
			{
				tMap.put(key, "1");
			}
		}
	}
	

	//测试用的方法，输出结果
	private void printResult()
	{
		Iterator it = tMap.keySet().iterator();
		while(it.hasNext())
		{
		}
	}
	
	//供外部调用的方法
	public Map getTables(String[][] info, Map usedTname)
	{
		tMap = new HashMap(0);
		buildNet(info);
		this.printNet();
		deepSearch(usedTname);
		this.printResult();

		Iterator it = tMap.keySet().iterator();

		while(it.hasNext())
		{
			String key = (String)it.next();
			usedTname.put(key, tMap.get(key));
		}

		visited = null;
		vertex = null;
		usedTables = null;
		tMap = null;
		
		return usedTname;
	}
	
	private Map visited = null;					//保存访问过的节点
	private Map vertex = new HashMap(0);		//邻接表
	private Map usedTables = null;				//记录使用的表名
	private Map tMap = null;					//存放结果数据
	
}
