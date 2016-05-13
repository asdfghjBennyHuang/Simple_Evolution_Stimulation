package EvolutionPack;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.Random;

public class Apples{
	static short day = 0;
	static short hour = 0;
	static short minute = 0;
	static short spacenum = 0;
	static short nowtime = 0;
	static final short boundx = 1000, boundy = 1000;
	static short[] thinkreport=new short[100];
	static public people pnum[] = new people[200];
	static public slime snum[] = new slime[slime.slimeMax];
	static Thread th = new Thread();
	static public short[][][][] groundthing = new short[2][6][boundx][boundy];
	static public short[][] locationcollect = new short[100][2];
	static public Random dice = new Random();
	static public int sleepTime = 100;
	static gui evogui = new gui();
	

	public static void main(String arg[]){
	// Setting the graphic interface
		JFrame evoframe = new JFrame();
		JScrollPane mainjsp = new JScrollPane();
		mainjsp.setViewportView(evogui);
		evoframe.add(mainjsp);
		evoframe.setTitle("Evolution");
		evoframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		evoframe.setVisible(true);
		evoframe.setSize(800, 800);
		
		control conp = new control();
		cmd cmdpanel = new cmd();
		JScrollPane jsp2 = new JScrollPane();
		jsp2.setViewportView(cmdpanel);
		JPanel user1 = new JPanel();
		user1.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;c.gridy=0;c.ipadx=100;c.ipady=900;
		user1.add(conp,c);
		c.gridx=1;c.ipadx=500;
		user1.add(jsp2,c);
		JFrame user1f = new JFrame();
		user1f.add(user1);
		user1f.setVisible(true);
		user1f.setSize(1000, 1000);
		user1f.setTitle("User1");
		user1f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		CreateEveryThing();
		while(day<200 && people.Num>0){
			while(control.ifPause==1){
				try{
					th.sleep(100);
				}catch(InterruptedException e){}
			}
			System.out.println(String.format("day %s hour %s minute %s" , day , hour , minute));
			conp.setNowtime();
			AllDecide();
			checkSlime();
			checkdie();
			System.out.println("slime: " + slime.slimeNum + " people: " + people.Num);
			writeNew();
			SetTime();
			evogui.repaint();
			cmdpanel.changePanel();
			try{
				th.sleep(sleepTime);
			}catch(InterruptedException e){}
		}
	}
//////////////////////////////////////////////////////
	/*
	location 
		0:original		1:new
		0:x				1:y
	 */
// Write the new information into old box
	static int writeNew(){
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i] == true){
				pnum[i].situ_new_to_old();
				locationcollect[i] = pnum[i].old_location.clone();
				if(hour==0 && minute==0){
					pnum[i].age++;
				}
			}
		}
		groundthing[0] = groundthing[1].clone();
		return 0;
	}
// All people decide what to do. Feel if not report 0. Minus the hp.
	static public void AllDecide(){
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i] == true){
				thinkreport[i] = pnum[i].think();
				pnum[i].innerChange();
				if(minute==0){
					if(pnum[i].age<20){
						pnum[i].new_health[situation.hp] -= 10; 
						pnum[i].new_health[situation.mental] -= 3;
					}else if(pnum[i].age<60){
						pnum[i].new_health[situation.hp] -= 20; 
						pnum[i].new_health[situation.mental] -= 5;
					}else if(pnum[i].age<120){
						pnum[i].new_health[situation.hp] -= 60; 
						pnum[i].new_health[situation.mental] -= 10;
					}else if(pnum[i].age<160){
						pnum[i].new_health[situation.hp] -= 200;
						pnum[i].new_health[situation.mental] -= 30;
					}else if(pnum[i].age<180){
						pnum[i].new_health[situation.hp] -= 500; 
						pnum[i].new_health[situation.mental] -= 60;
					}else if(pnum[i].age<200){
						pnum[i].new_health[situation.hp] -= 1000; 
						pnum[i].new_health[situation.mental] -= 90;
					}else{
						pnum[i].new_health[situation.hp] -= 2000; 
						pnum[i].new_health[situation.mental] -= 120;
					}
				}
			}
		}
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i] == true){
				if(thinkreport[i]!=10){
					if(thinkreport[i]==2){
						motion.seereal(pnum[i]);
					}else{
						motion.feel(pnum[i]);
					}
				}
			}
		}
	}
// Check die
	static void checkdie(){
		for(int i=0;i<people.peoMax;i++)
			checkdie(i);
	}
	static void checkdie(int i){
		int live = 1;
		if(people.alive[i] == true){
			if(pnum[i].new_health[situation.hp] <= 0){
				for(int j=0;j<6;j++){
					groundthing[1][j][ pnum[i].old_location[0] ][ pnum[i].old_location[1] ] += pnum[i].old_own[j];
				}
				groundthing[1][situation.meat][pnum[i].old_location[0]][pnum[i].old_location[1]] += 1;
				pnum[i] = null;
				people.Num -= 1;
				people.alive[i] = false;
				live = 0;
				System.out.println("die!" + i + " and " + people.Num + " left");
			}
		}
	}
// Check slime
	static void checkSlime(){
		for(int i=0;i<slime.slimeMax;i++){
			checkSlime(i);
		}
	}
	static void checkSlime(int i){
		if(slime.slimealive[i]==true){
			if(minute==0){
				snum[i].hp -= 10;
			}
			if(snum[i].hp>120){
				if(dice.nextInt(5)<1)
					snum[i].bornslime();
			}
			snum[i].recover();
			if(snum[i].hp<=0){
				snum[i].sdie();
			}else{
				snum[i].move();
			}
			if(slime.slimealive[i] == true){
				if(snum[i].location[0] >= boundx)	snum[i].location[0] = (short)(boundx-1);
				else if(snum[i].location[0] < 0)	snum[i].location[0] = 0;
				if(snum[i].location[1] >= boundy)	snum[i].location[1] = (short)(boundy-1);
				else if(snum[i].location[1] < 0)	snum[i].location[1] = 0;
			}
		}
	}
// Initialize the world and Create Every Thing
// Nop/Nos : number of person/slime
	public static void CreateEveryThing(){
		int Nop = 50;
		int Nos = 100;
		for(short i=0;i<Nop;i++){
			pnum[i] = new people(i);
			locationcollect[i]=pnum[i].old_location.clone();
		}
		for(int i=0;i<Nos;i++)
			snum[i] = new slime();
		for(int i=0;i<100;i++){
			groundthing[0][situation.iron][ dice.nextInt(600) ][ dice.nextInt(600) ] ++ ;
			groundthing[0][situation.meat][ dice.nextInt(600) ][ dice.nextInt(600) ] ++ ;
			groundthing[0][situation.fern][ dice.nextInt(600) ][ dice.nextInt(600) ] ++ ;
		}
		groundthing[1] = groundthing[0].clone();
	}
// SetTime function
	public static void SetTime(){
		minute += 10;
		if(minute==60){
			minute=0;
			hour+=1;
			if(hour==24){
				hour=0;
				day+=1;
			}
		}
		spacenum++;
		if(spacenum==memory.dur){
			spacenum = 0;
		}
	}
	// return the number of space
	public static int sp(int num){
		int a = spacenum + num;
		if(a>=0){
			return a;
		}else{
			while(true){
				a+=memory.dur;
				if(a>=0){
					return a;
				}
			}
		}
	}
}
