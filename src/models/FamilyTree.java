package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import utils.XMLSerializer;

public class FamilyTree {
	Map<String, Node> nodes;	//Name to Node dictionary
	ArrayList<Node> trees;		//Family Tree's
	XMLSerializer serializer;
	
	public FamilyTree(XMLSerializer serializer){
		this.serializer = serializer;
	}
	
	public FamilyTree(ArrayList<String[]> dataNodes, File file){
		nodes = new HashMap<String, Node>();
		trees = new ArrayList<Node>();
		serializer = new XMLSerializer(file);
		PriorityQueue<Node> pr = new PriorityQueue<Node>();
		for(String[] memberData : dataNodes){
			Node newNode = new Node(memberData[0],memberData[1].charAt(0),Integer.valueOf(memberData[2]),
					memberData[3],memberData[4]);
			nodes.put(newNode.name, newNode);
			pr.add(newNode);
		}
		
		refactorConnections();
		
		trees.add(pr.remove());
		
		while(!pr.isEmpty()){
			Node currentNode = pr.remove();
			if(!checkTrees(currentNode)) trees.add(currentNode);
		}
	}
	
	private boolean findNodeParent(Node currentNode, Node target){
		if(currentNode.equals(target)) return true;
		boolean fatherSide = (currentNode.father==null)? false : findNodeParent(currentNode.father, target);
		boolean motherSide = (currentNode.mother==null)? false : findNodeParent(currentNode.mother, target);
		return (fatherSide)? fatherSide : motherSide;
	}
	
	private boolean findNodeChild(Node currentNode, Node target){
		if(currentNode.equals(target)) return true;
		for(Node child : currentNode.children){
			if(findNodeChild(child, target)) return true;
		}
		return false;
	}
	
	private boolean checkTrees(Node currentNode){
		for(Node rootNode : trees){
			if(getSiblings(rootNode, new ArrayList<Node>()).contains(currentNode)){
				return true;
			}
			if(findNodeParent(rootNode, currentNode)){
				return true;
			}
			if(findNodeChild(rootNode, currentNode)){
				return true;
			}
		}
		return false;
	}
	
	public void save() throws Exception{
		serializer.push(nodes);
		serializer.push(trees);
		serializer.write();
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws Exception{
		serializer.read();
		trees = (ArrayList<Node>) serializer.pop();
		nodes = (Map<String, Node>) serializer.pop();
	}
	
	
	public void addFamilyMember(String name, char gender, int year, String parent1, String parent2){
		Node newNode = new Node(name, gender, year, parent1, parent2);
		nodes.put(name, newNode);
		refactorConnections();
		if(!checkTrees(newNode)) trees.add(newNode);
	}
	
	public boolean removeFamilyMember(String name){
		if(!familyMemberExist(name)) return false;
		
		Node member = nodes.get(name);
		for(Node child : member.children){
			if(child.father.equals(member))
				child.father = null;
			else if(child.mother.equals(member))
				child.mother = null;
		}
		
		trees.remove(member);
		nodes.remove(name);
		return true;
	}
	
	public boolean familyMemberExist(String name){
		return (nodes.containsKey(name))? true : false;
	}
	
	private void refactorConnections(){
		for(Node node : nodes.values()){
			if(nodes.get(node.parent1Name) != null){
				if(nodes.get(node.parent1Name).gender == 'M')
					node.father = nodes.get(node.parent1Name);
				else
					node.mother = nodes.get(node.parent1Name);
			}
			if(nodes.get(node.parent2Name) != null){
				if(nodes.get(node.parent2Name).gender == 'M')
					node.father = nodes.get(node.parent2Name);
				else
					node.mother = nodes.get(node.parent2Name);
			}
			for(Node child : nodes.values()){
				if(child.equals(node)) continue;
				if((child.father != null && child.father == node)||
						(child.mother != null && child.mother == node)){
					if(!node.children.contains(child)) node.children.add(child);
				}
			}
		}
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
	private TreeMap<Node, Integer> getParents(Node current, int gen, TreeMap<Node, Integer> parents){
		parents.put(current, gen);
		if(current.father != null) parents = getParents(current.father, gen+1, parents);
		if(current.mother != null) parents = getParents(current.mother, gen+1, parents);
		
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
			if( (node.father != null && (node.father == current.father || node.father == current.mother) ) ||
					(node.mother != null && (node.mother == current.father || node.mother == current.mother)) )
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
	private TreeMap<Node, Integer> getChildren(Node current, int gen, TreeMap<Node, Integer> children) {
		for(Node node : nodes.values()){
			if(node.equals(current)) continue;
			if(node.father == current || node.mother == current){
				children.put(node, gen);
				children = getChildren(node, gen+1, children);
			}
		}
		
		return children;
	}
	
	public ArrayList<String> getTreeRootNames(){
		ArrayList<String> members = new ArrayList<String>();
		for(Node node : trees){
			members.add(node.name);
		}
		Collections.sort(members);
		return members;
	}
	
	public ArrayList<String> getAllNodeNames(){
		ArrayList<String> members = new ArrayList<String>();
		for(Node node : nodes.values())
			members.add(node.name);
		Collections.sort(members);
		return members;
	}
	
	public String[] getNode(String name){
		if(!nodes.containsKey(name)) return null;
		String[] node = new String[5];
		Node returnNode = nodes.get(name);
		node[0] = returnNode.name;
		node[1] = String.valueOf(returnNode.gender);
		node[2] = String.valueOf(returnNode.year);
		node[3] = returnNode.father==null? null : returnNode.father.name;
		node[4] = returnNode.mother==null? null : returnNode.mother.name;
		return node;
	}
	
	public String getFamilyTree(String node){
		return printNode(nodes.get(node));
	}
	
	
	public <T extends Comparable<?>> String printNode(Node root) {
        int maxLevel = maxLevel(root);
        
        return printNodeInternal(Collections.singletonList(root), 1, maxLevel, "");
    }

    private <T extends Comparable<?>> String printNodeInternal(List<Node> nodes, int level, int maxLevel, String tree) {
        if (nodes.isEmpty() || isAllElementsNull(nodes))
            return tree;
        
        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        tree+=printWhitespaces(firstSpaces);

        List<Node> newNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node != null) {
            	ArrayList<Node> siblings = getSiblings(node, new ArrayList<Node>());
                tree+=node.name;
                tree+=" ( ";
                for(int i=0; i<siblings.size(); i++){
                	tree+=siblings.get(i).name+",";
                }
                tree = tree.substring(0, tree.length()-1);
                tree+=" )";
                newNodes.add(node.father);
                newNodes.add(node.mother);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                tree+=" ";
            }

            tree+=printWhitespaces(betweenSpaces);
        }
        tree+="\n";

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                tree+=printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    tree+=printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).father != null)
                    tree+="/";
                else
                	tree+=printWhitespaces(1);

                tree+= printWhitespaces(i + i - 1);

                if (nodes.get(j).mother != null)
                	tree+="\\";
                else
                	tree+=printWhitespaces(1);

                tree+=printWhitespaces(endgeLines + endgeLines - i);
            }
            tree+="\n";
        }

        return printNodeInternal(newNodes, level + 1, maxLevel,tree);
    }

    private static String printWhitespaces(int count) {
    	String space = "";
        for (int i = 0; i < count; i++)
            space+=" ";
        return space;
    }

    private static <T extends Comparable<?>> int maxLevel(Node node) {
        if (node == null)
            return 0;

        return Math.max(maxLevel(node.father), maxLevel(node.mother)) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }
}

class Node implements Comparable<Node>{
	String name;
	char gender;
	int year;
	Node father;
	Node mother;
	String parent1Name;
	String parent2Name;
	ArrayList<Node> children;
	
	public Node(String name,char gender, int year, String parent1Name, String parent2Name){
		this.name = name;
		this.gender = Character.toUpperCase(gender);
		this.year = year;
		this.parent1Name = parent1Name;
		this.parent2Name = parent2Name;
		children = new ArrayList<Node>();
		father = null;
		mother = null;
	}

	@Override
	public int compareTo(Node other) {
		return other.year - this.year;
	}
}
