//v20090409-1
package com.ccthanking.framework.coreapp.freequery.structure;

import java.util.List;

/**
 * 构造邻接表用的节点
 * @author Administrator
 *
 */
public class Node 
{
	private String name;
	private List relations;
	private Node nNode;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getRelations() {
		return relations;
	}

	public void setRelations(List relations) {
		this.relations = relations;
	}

	public Node getNNode() {
		return nNode;
	}

	public void setNNode(Node node) {
		nNode = node;
	}

	public static void main(String[] args)
	{
		
	}
}
