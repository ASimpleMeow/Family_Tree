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
		nodes.put(name, newNode);
		refactorConnections();
	}
	
	private void refactorConnections(){
		for(Node node : nodes.values()){
			node.parent1 = nodes.get(node.p1);
			node.parent2 = nodes.get(node.p2);
		}
	}
	
	public String getFamilyTree(String member){
		if(!nodes.containsKey(member)) throw new NullPointerException();
		//BTreePrinter.printNode(nodes.get(member));
		String tree = printFamily(nodes.get(member));
		System.out.println(tree);
		return tree;
	}
	
	private String printFamily(Node node){
		String familyTree = "";
		
		//check for parents
		HashMap<Node, Integer> parents = getParents(node, 0, new HashMap<Node, Integer>());
		
		//check for siblings
		ArrayList<Node> siblings = getSiblings(node, new ArrayList<Node>());
		
		//check for children
		HashMap<Node, Integer> children = getChildren(node, 1, new HashMap<Node, Integer>());
		
		for(Node parent : parents.keySet()){
			System.out.println("Generation : "+ parents.get(parent)+"Parent : "+parent.name);
		}
		System.out.println("\n");
		
		for(Node sibling : siblings){
			System.out.println("Sibling : "+sibling.name);
		}
		System.out.println("\n");
		
		for(Node child : children.keySet()){
			System.out.println("Generation : "+ children.get(child)+"Child : "+child.name);
		}
		System.out.println("\n");
		
		return familyTree;
		/*
		familyTree += "Generation "+gen+"  ";
		familyTree += node.name+" "+node.gender+" "+node.year+"  ";
		familyTree += (node.parent1!=null)? node.parent1.name:" ? "+"  ";
		familyTree += (node.parent2!=null)? node.parent2.name:" ? "+"  ";
		familyTree += "\n";
		if(node.parent1!=null) familyTree = printFamily(node.parent1, gen+1, familyTree);
		if(node.parent2!=null) familyTree = printFamily(node.parent2, gen+1, familyTree);
		return familyTree;
		*/
	}
	
	/**
	 * Retrieves parents of the node using recursion
	 * The parents map stores the Node and the generation it belongs to
	 * 
	 * @param current Current node
	 * @param gen Generation at which the current node is at
	 * @param parents Map of parents to the current node
	 * @return 
	 */
	private HashMap<Node, Integer> getParents(Node current, int gen, HashMap<Node, Integer> parents){
		parents.put(current, gen);
		if(current.parent1 != null) parents = getParents(current.parent1, gen+1, parents);
		if(current.parent2 != null) parents = getParents(current.parent2, gen+1, parents);
		
		return parents;
	}
	
	/**
	 * 
	 * @param current Current node 
	 * @param siblings ArrayList of siblings to the current node
	 * @return List of sibling nodes
	 */
	private ArrayList<Node> getSiblings(Node current, ArrayList<Node> siblings) {
		for(Node node : nodes.values()){
			if(node.equals(current)) continue;
			if( (node.parent1 != null && (node.parent1 == current.parent1 || node.parent1 == current.parent2) ) ||
					(node.parent2 != null && (node.parent2 == current.parent1 || node.parent2 == current.parent2)) )
					if(!siblings.contains(node)) siblings.add(node);
		}
		
		return siblings;
	}
	
	/**
	 * Retrieves children of the node using recursion
	 * The children map stores the Node and the generation it belongs to
	 * 
	 * @param current Current Node
	 * @param children Map of children to the current node
	 * @return
	 */
	private HashMap<Node, Integer> getChildren(Node current, int gen, HashMap<Node, Integer> children) {
		for(Node node : nodes.values()){
			if(node.equals(current)) continue;
			if(node.parent1 == current || node.parent2 == current){
				children.put(node, gen);
				children = getChildren(node, gen+1, children);
			}
		}
		
		return children;
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