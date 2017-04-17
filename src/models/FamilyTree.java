package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class FamilyTree {
	
	public FamilyTree(ArrayList<Member> members){
		ArrayList<Node> nodes = new ArrayList<Node>();
		for(Member member : members){
			Node newNode = new Node(member.getName(), member.getGender(), member.getYear(),
					member.getParent1(), member.getParent2());
			nodes.add(newNode);
		}
		for (Node currentNode : nodes){
			for (Node node : nodes){
				if (currentNode.equals(node)) continue;
				if(currentNode.p1.equals(node.name)){
					currentNode.parent1 = node;
				}else if(currentNode.p2.equals(node.name)){
					currentNode.parent2 = node;
				}
			}
		}
		Queue<Node> pQ = new PriorityQueue<Node>();
		pQ.addAll(nodes);
		/*
		for (Node node : nodes){
			System.out.print(node.name+" "+node.gender+" "+node.year+" ");
			System.out.print((node.parent1!=null)? node.parent1.name:"?"+" ");
			System.out.print((node.parent2!=null)? node.parent2.name:"?"+" ");
			System.out.println();
		}*/
		for(int i=0;i<50;++i){
			Node current = pQ.remove();
			printFamily(current,1);
			System.out.println("\n\n");
		}
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