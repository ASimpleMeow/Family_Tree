package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class FamilyTree {
	Map<String, Node> nodes;	//Name to Node dictionary
	
	public FamilyTree(){
		nodes = new HashMap<String, Node>();
		
		//Queue<Node> pQ = new PriorityQueue<Node>();
		//pQ.addAll(nodes);
		/*
		for (Node node : nodes){
			System.out.print(node.name+" "+node.gender+" "+node.year+" ");
			System.out.print((node.parent1!=null)? node.parent1.name:"?"+" ");
			System.out.print((node.parent2!=null)? node.parent2.name:"?"+" ");
			System.out.println();
		}*/
	}
	
	public void addFamilyMember(String name, char gender, int year, String parent1, String parent2){
		Node newNode = new Node(name, gender, year, parent1, parent2);
		newNode.parent1 = (nodes.containsKey(parent1))? nodes.get(parent1) : null;
		newNode.parent2 = (nodes.containsKey(parent2))? nodes.get(parent2) : null;
		nodes.put(name, newNode);
	}
	
	private void printFamily(Node node, int gen){
		System.out.print("Generation "+gen+"  ");
		System.out.print(node.name+" "+node.gender+" "+node.year+"  ");
		System.out.print((node.parent1!=null)? node.parent1.name:" ? "+"  ");
		System.out.print((node.parent2!=null)? node.parent2.name:" ? "+"  ");
		System.out.println();
		if(node.parent1!=null) printFamily(node.parent1,gen+1);
		if(node.parent2!=null) printFamily(node.parent2,gen+1);
	}
	
	public ArrayList<String> getNodes(){
		ArrayList<String> members = new ArrayList<String>();
		for(Node node : nodes.values()){
			members.add(node.name);
		}
		return members;
	}
}

class Node implements Comparable<Node>{
	String name;
	char gender;
	int year;
	Node parent1;
	Node parent2;
	String p1;
	String p2;
	
	public Node(String name,char gender, int year, String p1, String p2){
		this.name = name;
		this.gender = Character.toUpperCase(gender);
		this.year = year;
		this.p1 = p1;
		this.p2 = p2;
		parent1 = null;
		parent2 = null;
	}

	@Override
	public int compareTo(Node other) {
		return other.year - year;
	}
	
}