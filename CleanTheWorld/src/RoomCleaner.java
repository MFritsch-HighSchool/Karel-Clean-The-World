import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import kareltherobot.Directions;
import kareltherobot.Robot;
import kareltherobot.World;
/*
 * This program will prompt the user for the room to be cleaned
 * basically a world to be read in.  Then it prompts the user for
 * the starting location of the robot.  It makes the robot at that
 * location, facing North with 0 beepers.  Then, the robot picks up
 * every pile of beepers, until the room is clean.  When done, the
 * Robot reports the following information:
 * 1.  Number of piles
 * 2.  Total number of beepers
 * 3.  Area of the room (width*height)
 * 4.  Average pile size (beepers / piles)
 * 5.  Average messiness (beepers / area)
 * 6.  Biggest pile of beepers
 * 6b) location of the biggest pile size relative to the room how many
 *     steps east and north from lower left-hand corner
 * 7.  Median size of pile
 * You will use a List to hold the number of beepers picked up for each 
 * location
 */

public class RoomCleaner {
	Robot r;
	int pile = 0;
	int totalBeeps = 0;
	int a = 0;
	int bpile = 0;
	float area = 0;
	int hsteps = 0;
	int wsteps = 0;
	int st1;
	int ave1; 
	int looper;
	int median;
	List<Integer> l = new ArrayList<Integer>();
	JFileChooser chooser;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RoomCleaner().start();
		
		
	}
	private void start() {
		openWorld();
		makeRobot();
		goToCorner();
		cleanRoom();
		reportStats();
		
	}
	private void openWorld() {
//	    chooser = new JFileChooser();
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
//				"Robot Worlds", "kwld");
//		chooser.setFileFilter(filter);
//		int returnVal = chooser.showOpenDialog(null);
//		if(returnVal == JFileChooser.APPROVE_OPTION) {
//			System.out.println("You chose to open this file: " +
//					chooser.getSelectedFile().getAbsolutePath());
//		}
//	    World.readWorld(chooser.getSelectedFile().getAbsolutePath());
		String world = JOptionPane.showInputDialog("Enter world name ____.kwld");
		World.readWorld(world+".kwld");
	    World.setVisible(true);
		World.setDelay(0);
	}
	private void makeRobot() {
		String st = JOptionPane.showInputDialog("Enter the street for robot.");
		int street = Integer.parseInt(st);
		String ave = JOptionPane.showInputDialog("Enter the avenue for robot.");
		int avenue = Integer.parseInt(ave);
		List<String> optionList = new ArrayList<String>();
		optionList.add("North");
		optionList.add("South");
		optionList.add("East");
		optionList.add("West");
		Object[] options = optionList.toArray();
		Object value = JOptionPane.showInputDialog(null, "Which direction would you like to begin?", "input box", 0, null, options, 0);
		if(value.equals("North")){
			r = new Robot(street,avenue,Directions.North,0);
		}
		else if(value.equals("South")){
			r = new Robot(street,avenue,Directions.South,0);
		}		
		else if(value.equals("East")){
			r = new Robot(street,avenue,Directions.East,0);
		}
		else if(value.equals("West")){
			r = new Robot(street,avenue,Directions.West,0);	
		}
	}
	private void cleanRoom() {
		countSide();
		if(r.facingNorth() || r.facingSouth()){
			for(int looper = wsteps-1; looper > 0; looper--){
				cleanStreet();
				r.turnLeft();
				r.move();
				r.turnLeft();
			}
		}
		else if(r.facingEast() || r.facingWest()){
			for(int looper = hsteps-1; looper > 0; looper--){
				cleanStreet();
				r.turnLeft();
				r.move();
				r.turnLeft();
			}

		}
		cleanStreet();
	}
	private void countSide() {
		System.out.println("I am now counting side");
		if(r.facingNorth() || r.facingSouth()){
			hsteps=1;
			while(r.frontIsClear()){
				r.move();
				hsteps++;
			}
		}
		else if(r.facingEast() || r.facingWest()){
			wsteps = 1;
			while(r.frontIsClear()){
				r.move();
				wsteps++;
			}
		}
		while(r.frontIsClear() == false)
			r.turnLeft();
		while(r.frontIsClear())
			r.move();
		r.turnLeft();
		if(r.facingNorth() || r.facingSouth()){
			hsteps=1;
			while(r.frontIsClear()){
				r.move();
				hsteps++;
			}
		}
		else if(r.facingEast() || r.facingWest()){
			wsteps = 1;
			while(r.frontIsClear()){
				r.move();
				wsteps++;
			}
		}
		r.turnLeft();
		r.turnLeft();
		while(r.frontIsClear()){
			r.move();
		}
		turnRight();
		System.out.println("I am done counting side");
	}
	private void cleanStreet() {
		while(r.frontIsClear()){
			if(r.nextToABeeper()){
				pile++;
			}
			while(r.nextToABeeper()){
				r.pickBeeper();
				totalBeeps++;
				a++;
				if(r.nextToABeeper()==false)
					l.add(a);
			}
			while(a > bpile){
				bpile =a;
				st1 = r.street();
				ave1 = r.avenue();
			}
			while(r.nextToABeeper()==false & r.frontIsClear()){
				r.move();
				a = 0;
			}
		}
		if(r.nextToABeeper()){
			pile++;
		}
		while(r.nextToABeeper()){
			r.pickBeeper();
			totalBeeps++;
			a++;
			if(r.nextToABeeper()==false)
				l.add(a);
		}
		while(a > bpile){
			bpile =a;
			st1 = r.street();
			ave1 = r.avenue();
		}
		a = 0;
		r.turnLeft();
		r.turnLeft();
		while(r.frontIsClear())
			r.move();
	}

	private void goToCorner() {
		while(r.frontIsClear())
			r.move();
		r.turnLeft();
		while(r.frontIsClear())
			r.move();
		while(r.frontIsClear()==false)
			r.turnLeft();
		r.turnLeft();
		System.out.println("I have made it to the corner.");
	}
	private void reportStats() {
		System.out.println("There were " +pile+ " piles");
		System.out.println("The total number of beepers is " +totalBeeps+ " beepers.");
		area=hsteps*wsteps;
		System.out.println("Height is "+hsteps+" and width is "+wsteps);
		System.out.println("The area was "+area);
		System.out.println("Largest pile size was "+bpile);
		float avg = totalBeeps/pile;
		System.out.println("The average pile size was "+avg);
		float messiness = totalBeeps/area;
		System.out.println("The general messiness of the room was "+messiness);
		System.out.println("The biggest pile was located at "+st1+" "+ave1);
		Collections.sort(l);
		//sortMedian();
		System.out.println(l);
		int spec = l.size()/2;
		double specs = l.size()/2;
		double finSpec = specs-spec;
		if(finSpec == 0)
			median = l.get(spec-1);
		else
			median = l.get(spec);
		System.out.println("The median is "+median);

	}
//	private void sortMedian() {
//		for(int looper = 1; looper < l.size(); looper++){
//			int temp3 = 0;
//			if(l.get(looper) <= l.get(looper-1)){
//				temp3 = l.get(looper);
//				l.
//			}
//		}
//		//Because it removes the element entirely, you could run a for loop AND recursion.
//		//You have to compare the last element to the one before it EACH TIME.
//		//Then compare the 3rd element to the 2nd, and then the 2nd to the 1st.
//		//Do this in a for loop until l.size has run allllll of the array into order.
//		//Then its just a matter of doing l.size/2+1 is the median
//		System.out.println(l);
//		l.remove(0);
//		System.out.println(l);
//	}
	private void turnRight(){
		r.turnLeft();
		r.turnLeft();
		r.turnLeft();
	}
	
}














