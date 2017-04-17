package controllers;

import java.io.File;
import java.util.ArrayList;

import edu.princeton.cs.introcs.In;
import models.FamilyTree;
import models.Member;

public class Main {
	
	ArrayList<Member> members;
	FamilyTree familyTree;
	
	public static void main(String[] args){
		try {
			new Main().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		members = new ArrayList<Member>();
		loadMemebers();
		familyTree = new FamilyTree(members);
		//if(!familyTree.findMemeber("Alexander").equals("Alexander")) System.out.println("YAY!");
		
	}
	
	public void loadMemebers(){
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
            	members.add(new Member(memeberTokens[0],memeberTokens[1].charAt(0)
            			,Integer.parseInt(memeberTokens[2]),memeberTokens[3],memeberTokens[4]));
            }else{
                try {
					throw new Exception("Invalid member length: "+memeberTokens.length);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
	}
}
