package models;

public class Memeber {

	private String name;
	private int year;
	private char gender;
	private String parent1;
	private String parent2;
	
	public Memeber(String name,char gender, int year, String parent1, String parent2){
		this.name = name;
		this.year = year;
		this.gender = Character.toUpperCase(gender);
		this.parent1 = parent1;
		this.parent2 = parent2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getParent1() {
		return parent1;
	}

	public void setParent1(String parent1) {
		this.parent1 = parent1;
	}

	public String getParent2() {
		return parent2;
	}

	public void setParent2(String parent2) {
		this.parent2 = parent2;
	}
	
	
}
