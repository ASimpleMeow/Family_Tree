package models;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class FamilyTree {

	private Map<Node,String> list = new TreeMap<Node,String>(new Comparator<Node>(){

		@Override
		public int compare(Node first, Node second) {
			return first.year-second.year;
		}
	});
	
	public void addMember(String name, char gender, int year, String parent1, String parent2){
		Node currentNode = new Node(name,gender,year);
		for(Node node :list.keySet()){
			if(node.name.equals(parent1)){
				currentNode.parent1 = node;
			}else if(node.name.equals(parent2)){
				currentNode.parent2 = node;
			}
		}
		list.put(currentNode, name);
	}
	
	public String findMemeber(String name){
		for(Node memeber : list.keySet()){
			if(memeber.name.equals(name)){
				return memeber.name;
			}
		}
		return null;
	}
}

class Node implements Comparable<Node>{
	String name;
	char gender;
	int year;
	Node parent1;
	Node parent2;
	
	public Node(String name,char gender, int year){
		this.name = name;
		this.gender = Character.toUpperCase(gender);
		this.year = year;
		parent1 = null;
		parent2 = null;
	}

	@Override
	public int compareTo(Node other) {
		return year - other.year;
	}
	
}
