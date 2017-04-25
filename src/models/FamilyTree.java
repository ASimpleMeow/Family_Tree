package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.XMLSerializer;

/**
 * Family Tree class which is also adapted for forest of trees
 * and has saving functionality
 * 
 * @author Oleksandr Kononov
 * @version 25-04-2017
 *
 */
public class FamilyTree {
	
	private Map<String, Node> nodes;		//Name to Node dictionary
	private HashMap<Integer, Node> trees;	//Family Count to Node dictionary
	private XMLSerializer serializer;
	
	private HashMap<String, Integer> families;	//Who belongs to which family
	private int familyCount;
	
	/**
	 * If there is a save file, then use this constructor
	 * to load previously created family trees
	 * 
	 * @param serializer XMLSerializer
	 */
	public FamilyTree(XMLSerializer serializer){
		this.serializer = serializer;
	}
	
	/**
	 * Constructor to create new family trees
	 * 
	 * @param dataNodes ArrayList of String[] containing nodes
	 * @param file File to save data to
	 */
	public FamilyTree(ArrayList<String[]> dataNodes, File file){
		
		nodes = new HashMap<String, Node>();  //Used to contain all nodes
		serializer = new XMLSerializer(file); //Serialise data
		trees = new HashMap<Integer, Node>(); //Contains root nodes for combo box
		
		//Populate the nodes
		for(String[] memberData : dataNodes){
			Node newNode = new Node(memberData[0],memberData[1].charAt(0),Integer.valueOf(memberData[2]),
					memberData[3],memberData[4]);
			nodes.put(newNode.name, newNode);
		}
		
		refactorConnections();	//Connect all the nodes to each other
		calculateFamilies();	//Determine the amount of families (trees)
		fillTrees();			//Populate the trees (root nodes)
	}
	
	/**
	 * Builds the family trees by establishing family relationships
	 * and connections.
	 */
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
	 * Calculates the number of families
	 */
	private void calculateFamilies(){
		//setup the familyCount and families
		familyCount = 0;
		families = new HashMap<String, Integer>();
		
	    for (Node node : nodes.values()) {
	    	if(families.containsKey(node.name)) continue;
	    	setFamily(node.name, familyCount);
    		familyCount++;
	    }
	    familyCount--; //Since we went over by 1
	}
	
	/**
	 * Recursively adds the family members to the family count
	 * @param name Name of the family member
	 * @param count The family count
	 */
	private void setFamily(String name, int count) {
	    if (families.containsKey(name)) return;
	    
	    families.put(name, count);        // add me to current family 
	    
	    if(nodes.get(name).father != null) setFamily(nodes.get(name).father.name, count); //add father
	    if(nodes.get(name).mother != null) setFamily(nodes.get(name).mother.name, count); //add mother
	    for (Node child: nodes.get(name).children) 
	        setFamily(child.name, count);    // add children to family
	}
	
	/**
	 * Populates and determines the root nodes by year to later 
	 * be inserted into the comboBox for quick selection
	 */
	private void fillTrees(){
		
		//For every familyCount, if you found a younger member
		//replace the current member
		for(String name : families.keySet()){
			Node currentNode = nodes.get(name);
			int currentFamily = families.get(name);
			
			if(trees.containsKey(currentFamily)){
				if(trees.get(currentFamily).year > currentNode.year){
					trees.put(currentFamily, currentNode);
				}
			}else{
				trees.put(currentFamily, currentNode);
			}
		}
	}
	
	/**
	 * Finds the target node from parents
	 * @param currentNode Node that is currently being looked at
	 * @param target Node that is targeted to find
	 * @return
	 */
	private boolean findNodeParent(Node currentNode, Node target){
		if(currentNode.equals(target)) return true;
		boolean fatherSide = (currentNode.father==null)? false : findNodeParent(currentNode.father, target);
		boolean motherSide = (currentNode.mother==null)? false : findNodeParent(currentNode.mother, target);
		return (fatherSide)? fatherSide : motherSide;
	}
	
	/**
	 * Checks whether you can add this currentNode as a root
	 * node of the trees in the comboBox
	 * 
	 * @param currentNode Node
	 * @return
	 */
	private boolean checkTrees(Node currentNode){
		for(Node rootNode : trees.values()){
			if(getSiblings(rootNode).contains(currentNode)){
				return true;
			}
			if(findNodeParent(rootNode, currentNode)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Save data
	 * @throws Exception
	 */
	public void save() throws Exception{
		serializer.push(nodes);
		serializer.push(trees);
		serializer.push(new Integer(familyCount));
		serializer.write();
	}
	
	/**
	 * Load data
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void load() throws Exception{
		serializer.read();
		familyCount = (Integer) serializer.pop();
		trees = (HashMap<Integer, Node>) serializer.pop();
		nodes = (Map<String, Node>) serializer.pop();
	}
	
	/**
	 * Adds a new person to the family trees
	 * 
	 * @param name Name of person
	 * @param gender Gender of person
	 * @param year Year of birth of person
	 * @param parent1 First parent's name
	 * @param parent2 Second parent's name
	 */
	public void addFamilyMember(String name, char gender, int year, String parent1, String parent2){
		Node newNode = new Node(name, gender, year, parent1, parent2);
		nodes.put(name, newNode);
		refactorConnections();
		
		if(!checkTrees(newNode)){	//If not related to anyone, make root node in trees
			familyCount++;
			trees.put(familyCount, newNode);
		}
	}
	
	/**
	 * Removes a member from the family trees
	 * @param name Name of person
	 * @return If removed successfully
	 */
	public boolean removeFamilyMember(String name){
		if(!familyMemberExist(name)) return false;
		
		Node member = nodes.get(name);
		
		//Disconnect the children
		for(Node child : member.children){
			if(child.father.equals(member))
				child.father = null;
			else if(child.mother.equals(member))
				child.mother = null;
		}
		
		//If member is a root node
		if(trees.containsValue(member)){
			for(int family : trees.keySet()){
				if(trees.get(family).equals(member)){
					replaceRoot(family);
					familyCount--;
					break;
				}
			}
		}
		nodes.remove(name);
		return true;
	}
	
	/**
	 * Replaces the root for the current family
	 * @param family
	 */
	private void replaceRoot(int family){
		Node toBeRemoved = trees.get(family);
		Node newRoot = null;
		ArrayList<Node> siblings = getSiblings(toBeRemoved);
		int yongestYear = 9999;
		for(Node sibling : siblings){
			if(sibling.year < yongestYear){
				newRoot = sibling;
				yongestYear = sibling.year;
			}
		}
		
		if(newRoot != null){
			trees.put(family, newRoot);
			return;
		}
		
		//If there are not siblings, make the two parents their own roots
		if(toBeRemoved.father != null) trees.put(family, toBeRemoved.father);
		else if(toBeRemoved.mother != null) trees.put(family, toBeRemoved.mother);
		else{
			trees.remove(family);
			return;
		}
		
		//If mother is to null then make her into her own root as well
		if(toBeRemoved.father != null && trees.get(family).equals(toBeRemoved.father)){
			if(toBeRemoved.mother != null){
				familyCount++;
				trees.put(familyCount, toBeRemoved.mother);
			}
		}
	}
	
	public boolean familyMemberExist(String name){
		return (nodes.containsKey(name))? true : false;
	}
	
	/**
	 * Return all information about a member
	 * @param name String name of the member
	 * @return
	 */
	public ArrayList<String> getNodeInformation(String name){
		ArrayList<String> familyInformation = new ArrayList<String>();
		if(!nodes.containsKey(name)) return familyInformation;
		Node current = nodes.get(name);
		String myInformation = "Name : "+current.name+
				"  Year : "+current.year+"  Gender : "+current.gender+"\n\n";
		
		familyInformation.add(myInformation);
		
		String parents = "";
		ArrayList<String> parentsStrings
		= getParents(Collections.singletonList(current), 0 , new ArrayList<String>());
		for(int i = 1; i< parentsStrings.size(); ++i)
			parents += parentsStrings.get(i);
		familyInformation.add(parents);
		
		String siblings = "";
		for(Node sibling : getSiblings(current)){
			siblings+= "Name : "+sibling.name+
				"  Year : "+sibling.year+"  Gender : "+sibling.gender+"\n";
		}
		
		familyInformation.add(siblings);
		familyInformation.add(getChildren(current, current, 0, ""));
		
		return familyInformation;
	}
	
	/**
	 * @param current Current node 
	 * @return List of sibling nodes
	 */
	private ArrayList<Node> getSiblings(Node current) {
		ArrayList<Node> siblings = new ArrayList<Node>();
		
		if(current.father != null){
			for(Node sibling : current.father.children){
				if(sibling.equals(current)) continue;
				if(!siblings.contains(sibling)) siblings.add(sibling);
			}
		}
		
		if(current.mother != null){
			for(Node sibling : current.mother.children){
				if(sibling.equals(current)) continue;
				if(!siblings.contains(sibling)) siblings.add(sibling);
			}
		}
		
		return siblings;
	}
	
	/**
	 * Recursively traverse the tree level by level to get 
	 * every generation of parents
	 * @param nodes List of nodes
	 * @param gen Current generation
	 * @param parents The parents list
	 * @return
	 */
	private ArrayList<String> getParents(List<Node> nodes, int gen, ArrayList<String> parents){
		List<Node> next = new ArrayList<Node>();
		String thisGeneration = "";
	    for (Node parent : nodes) {
	        if (parent != null) {
	        	thisGeneration += "Generation : "+gen+"  Name : "+parent.name+
				"  Year : "+parent.year+"  Gender : "+parent.gender+"\n";
	            next.add(parent.father);
	            next.add(parent.mother);
	        }
	    }
    	parents.add(thisGeneration);
	    if(next.size() > 0)parents = getParents(next, gen+1, parents);
	    
	    return parents;
	}
	
	/**
	 * Recursively get all childrens information
	 * @param original Original Node
	 * @param current Current Node
	 * @param gen Current Generation
	 * @param children String of children's information
	 * @return
	 */
	private String getChildren(Node original, Node current, int gen, String children) {
		for(Node node : nodes.values()){
			if(node.equals(current)) continue;
			if(node.father == current || node.mother == current){
				children += "Generation : "+gen+"  Name : "+current.name+
						"  Year : "+current.year+"  Gender : "+current.gender+"\n";
				children = getChildren(original, node, gen+1, children);
			}
		}
		
		return children;
	}
	
	/**
	 * Get trees root nodes names
	 */
	public ArrayList<String> getTreeRootNames(){
		ArrayList<String> members = new ArrayList<String>();
		for(Node node : trees.values()){
			members.add(node.name);
		}
		Collections.sort(members);
		return members;
	}
	
	/**
	 * Get all node names
	 */
	public ArrayList<String> getAllNodeNames(){
		ArrayList<String> members = new ArrayList<String>();
		for(Node node : nodes.values())
			members.add(node.name);
		Collections.sort(members);
		return members;
	}
	
	/**
	 * Get family count
	 */
	public int getFamilyCount(){
		return familyCount+1; //Since I counted from zero
	}
	
	/**
	 * Get all information about a node
	 * @param name Name of Member
	 * @return
	 */
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
	
	/**
	 * Returns String of a family tree from a node
	 * @param node
	 * @return
	 */
	public String getFamilyTree(String node){
		return printNode(nodes.get(node));
	}
	
	/**
	 * Initial start of the printing process
	 * 
	 * Reference : Method originally belonged to BTPrinter, but altered by me
	 * @param root
	 * @return
	 */
	public <T extends Comparable<?>> String printNode(Node root) {
        int maxLevel = maxLevel(root);
        
        return printNodeInternal(Collections.singletonList(root), 1, maxLevel, "");
    }
	
	/**
	 * Recursively prints parents (as well as siblings in brackets)
	 * Reference : Method originally belonged to BTPrinter, but altered by me
	 * @param nodes
	 * @param level
	 * @param maxLevel
	 * @param tree
	 * @return
	 */
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
            	ArrayList<Node> siblings = getSiblings(node);
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

    /**
     * Reference : Method originally belonged to BTPrinter, but altered by me
     * @param count Amount of whitespaces needed
     * @return
     */
    private static String printWhitespaces(int count) {
    	String space = "";
        for (int i = 0; i < count; i++)
            space+=" ";
        return space;
    }
    
    /**
     * Get the max level of the tree
     * Reference : Method originally belonged to BTPrinter, but altered by me
     * @param node
     * @return
     */
    private static <T extends Comparable<?>> int maxLevel(Node node) {
        if (node == null)
            return 0;

        return Math.max(maxLevel(node.father), maxLevel(node.mother)) + 1;
    }

    /**
     * Checks if all the elements of the list are null
     * Reference : Method originally belonged to BTPrinter, but altered by me
     * @param list
     * @return
     */
    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }
}

/**
 * Node class for the family tree
 * @author Oleksandr Kononov
 */
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
