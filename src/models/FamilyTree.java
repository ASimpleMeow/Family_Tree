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
	
	public String getFamilyTree(String member){
		if(!nodes.containsKey(member)) throw new NullPointerException();
		BTreePrinter.printNode(nodes.get(member));
		return printFamily(nodes.get(member), 1, "");
	}
	
	private String printFamily(Node node, int gen, String familyTree){
		familyTree += "Generation "+gen+"  ";
		familyTree += node.name+" "+node.gender+" "+node.year+"  ";
		familyTree += (node.parent1!=null)? node.parent1.name:" ? "+"  ";
		familyTree += (node.parent2!=null)? node.parent2.name:" ? "+"  ";
		familyTree += "\n";
		if(node.parent1!=null) familyTree = printFamily(node.parent1, gen+1, familyTree);
		if(node.parent2!=null) familyTree = printFamily(node.parent2, gen+1, familyTree);
		return familyTree;
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

class BTreePrinter {

    public static <T extends Comparable<?>> void printNode(Node root) {
        int maxLevel = BTreePrinter.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T extends Comparable<?>> void printNodeInternal(List<Node> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BTreePrinter.printWhitespaces(firstSpaces);

        List<Node> newNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node != null) {
                System.out.print(node.name);
                newNodes.add(node.parent1);
                newNodes.add(node.parent2);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            BTreePrinter.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                BTreePrinter.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).parent1 != null)
                    System.out.print("/");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(i + i - 1);

                if (nodes.get(j).parent2 != null)
                    System.out.print("\\");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static <T extends Comparable<?>> int maxLevel(Node node) {
        if (node == null)
            return 0;

        return Math.max(BTreePrinter.maxLevel(node.parent1), BTreePrinter.maxLevel(node.parent2)) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }

}