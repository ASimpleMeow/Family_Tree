package controllers;

import java.io.File;
import java.util.ArrayList;

import edu.princeton.cs.introcs.In;
import models.FamilyTree;
import models.Memeber;

public class Main {
	
	ArrayList<Memeber> members;
	FamilyTree familyTree;
	
	public static void main(String[] args){
		try {
			new Main().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() throws Exception{
		members = new ArrayList<Memeber>();
		familyTree = new FamilyTree();
		loadMemebers();
		for(Memeber member : members){
			familyTree.addMember(member.getName(), member.getGender(), member.getYear(),
					member.getParent1(), member.getParent2());
		}
		
		if(!familyTree.findMemeber("Alexander").equals("Alexander")) System.out.println("YAY!");
		
	}
	
	public void loadMemebers() throws Exception{
		File memebersFile = new File("data/large-database.txt");
        In inMemebers = new In(memebersFile);
          //each field is separated(delimited) by a '|'
        String delims = "\\s+";
        while (!inMemebers.isEmpty()) {
            // get user and rating from data source
            String memeberDetails = inMemebers.readLine();

            // parse user details string
            String[] memeberTokens = memeberDetails.split(delims);
            
            if (memeberTokens.length == 5) {
            	members.add(new Memeber(memeberTokens[0],memeberTokens[1].charAt(0)
            			,Integer.parseInt(memeberTokens[2]),memeberTokens[3],memeberTokens[4]));
            }else{
                throw new Exception("Invalid member length: "+memeberTokens.length);
            }
        }
	}
}
