package EvolutionPack;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Timer;

public class cmd extends JPanel{
	JTextField cmdl = new JTextField("cmd",30);
	String prestr = new String("cmd<br>");
	String htmlini = new String("<HTML><font size=5>--------------------------------------------"
			+ "-------------------------------------<br>");
	String htmllast = new String("</font></HTML>");
	static final private String prob = "Problem with condition!";
	JLabel show = new JLabel();
	Timer time = new Timer();
	String gotini = new String();
	String[] got = new String[5];
	int TheTask = -1;
	String[] taskarray;
	private static boolean[] pointedlist = new boolean[people.peoMax];
	
	public cmd(){
		got[0] = "default";
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		cmdl.addActionListener(new cmdevent());
		c.gridx=0;c.gridy=0;c.fill=c.HORIZONTAL;
		add(cmdl,c);
		c.gridy=1;c.fill=c.HORIZONTAL;
		add(show,c);
	}
	public class cmdevent implements ActionListener{
		public void actionPerformed(ActionEvent e){
			gotini = cmdl.getText();
			String[] gottemp = gotini.split(" ");
			for(int i=0;i<5;i++){
				if(i<gottemp.length){
					got[i]=gottemp[i];
				}else{
					got[i]="-1";
				}
			}
			getInput();
			changePanel();
		}
	}
	/*
	pos:		Show all people's position.
	hp :		Show all people's hp.
	all:		show all people's main information include hp, position, motion.
	allp:		Show all people's main information in pretty format, slow down the speed.
	item:		Input x and y coordinate. Show ground things at that place
				2par
	sall: 		Show main information of all slime.
	mempeo:		Show people in the memory.
	fpos:		Input x and y of a block. Show people in the pointed block.
				2 par
	pgene:		Show all the people's gene.
	goal: 		Show all the people's goal;
	pointed:	Show detail information of the people that are pointed.
	poiadd: 	Add the people to the pointed list.
				1-4 apr
	poisub: 	Remove the people from the pointed list.
				1-4 par
	ground: 	Input x and y of a block. Show all the things on the ground in the pointed block.
				You can specify which exact item you want to know by adding the name as third parameter.
	 			2-3 par
 	 */
	// ----------------------------------------------------------------------------------------------------- //
	// Get the input -- Get the input -- Get the input -- Get the input -- Get the input -- Get the input -- //
	public void getInput(){
		switch(got[0]){
			case "all":
				TheTask=2;break;
			case "allp":
				TheTask=3;break;
			case "item":
				TheTask=4;taskarray = got;break;
			case "sall":
				TheTask=5;taskarray = got;break;
			case "mempeo":
				TheTask=6;break;
			case "fpos":
				TheTask=7;taskarray = got;break;
			case "pgene":
				TheTask=8;taskarray = got;break;
			case "goal":
				TheTask=9;break;
			case "pointed":
				TheTask=10;taskarray = got;break;
			case "own":
				TheTask=11;break;
			case "poiadd":
				for(int i=1;i<5;i++)
					if(toint(got[i])>=0)
						pointedlist[ toint(got[i]) ] = true;
				TheTask=10;taskarray = got;
				break;
			case "poisub":
				for(int i=1;i<5;i++){
					if(toint(got[i])>=0)
						pointedlist[ toint(got[i]) ] = false;
				}
				if(toint(got[1])==-1){
					for(int i=0;i<people.peoMax;i++){
						pointedlist[i] = false;
					}
				}
				TheTask=10;taskarray = got;
				break;
			case "guilar":
				if(got.length>1)
					gui.enlarge=( toint(got[1]) > 1 ? 1 : toint(got[1]) );
				break;
			case "ground":
				TheTask=12;taskarray=got;
				break;
			default:
				formstr(got[0]);break;
		}
	}
	// ----------------------------------------------------------------------------------------------------- //
	// Change the Panel -- Change the Panel -- Change the Panel -- Change the Panel -- Change the Panel ---- // 
	void changePanel(){
		switch(TheTask){
			case 0:break;
			case 2:
				formstr(showAll());break;
			case 3:
				formstr(showAllp());break;
			case 4:
				int px = toint(taskarray[1]);
				int py = toint(taskarray[2]);
				int what = motion.convertMotion(taskarray[3]);
				formstr( showItem( px, py, what ));break;
			case 5:
				formstr(sall());break;
			case 6:
				formstr(mempeople());break;
			case 7:
				int px1 = toint(taskarray[1]);
				int py1 = toint(taskarray[2]);
				formstr( showAll(px1,py1) );break;
			case 8:
				formstr(peogene());break;
			case 9:
				formstr(showGoal());break;
			case 10:
				formstr(showAll(pointedlist));break;
			case 11:
				formstr(showOwn());break;
			case 12:
				int px2 = toint(taskarray[1]);
				int py2 = toint(taskarray[2]);
				formstr(showGround(px2,py2,situation.convertsitu(taskarray[3],"own")));break;
		}
	}

//////////////////function called when change the panel////////////////////
	private String showItem(int x, int y, int what){
		String result = new String();
		if(x!=people.adjustRange(x)||y!=people.adjustRange(y)){
			return prob;
		}else{
			result += "(" + x + "," + y + "): ";
			if(what == -1){
				for(int i=0;i<situation.ownName.length;i++){
					result += " " + Apples.groundthing[0][ i ][ x ][ y ] + " ";
				}
			}else{
				result += " " + Apples.groundthing[0][ what ][ x ][ y ] + " ";
			}
			
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String showAll(){
		String re = "";
		for(short i=0;i<people.peoMax;i++){
			if(people.alive[i]){
				re += " " + i + " loc: " + Apples.pnum[i].old_location[0] + "," + Apples.pnum[i].old_location[1] + 
						" hp: " + Apples.pnum[i].old_health[situation.hp] + 
						" meat: " + Apples.pnum[i].old_own[situation.meat] +
						" age: " + Apples.pnum[i].age +
						" motion: " + motion.convertnum(Apples.thinkreport[i]) + 
						"<br>";
			}
		}
		return re;
	}
	// -----------------------------------------------------------------------------------------------//
	private String showAllp(){
		String result = "<table><tr>"
				+ "<td>Num</td>"
				+ "<td>location</td>"
				+ "<td>HP</td>"
				+ "<td>mental</td>"
				+ "<td>lactate</td>"
				+ "<td>motion</td></tr>";
		for(short i=0;i<people.peoMax;i++){
			if(people.alive[i]){
				result += "<tr><td>" + i + "</td>" 
				+ "<td>" + Apples.pnum[i].new_location[0] + "," + Apples.pnum[i].new_location[1] + "</td>"
				+ "<td>" + Apples.pnum[i].old_health[situation.hp] + "</td>" 
				+ "<td>" + Apples.pnum[i].old_health[situation.mental] + "</td>"  
				+ "<td>" + Apples.pnum[i].old_health[situation.lactate] + "</td>" 
				+ "<td>" + motion.convertnum(Apples.thinkreport[i]) + "</td></tr>";
			}else{
				result += "<tr><td>" + i + "</td><td>dead</td></tr>";
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String showAll(boolean[] list){
		String result = "";
		for(int i=0;i<people.peoMax;i++){
			if(list[i]==true){
				if(people.alive[i]==true){
					result += i + " : " + Apples.pnum[i].new_location[0] + ", " + Apples.pnum[i].new_location[1];
					for(int j=0;j<3;j++){
						result += " " + situation.convertsitu(j, "health") + ": " + Apples.pnum[i].old_health[j];
					}
					result += "<br>";
					for(int j=3;j<situation.healthName.length;j++){
						result += " " + situation.convertsitu(j, "health") + ": " + Apples.pnum[i].old_health[j];
					}
					result += "<br>motion: " + motion.convertnum( Apples.thinkreport[i] );
					result += "<br>gene: "; 
					for(int j=0;j<20;j++){
						if( Apples.pnum[i].gene[j]==true){
							result += "1";
						}else{
							result += "0";
						}
					}
					result += "<br>goal: ";
					for(int j=0;j<3;j++){
						result += Apples.pnum[i].goal[j] + " ";
					}
					result += "<br>home: ";
					for(int j=0;j<3;j++){
						result += Apples.pnum[i].home[j] + " ";
					}
					result += "<br>farm: ";
					for(int j=0;j<3;j++){
						result += Apples.pnum[i].farm[j] + " ";
					}
					result += "<br>own: ";
					for(int ii=0;ii<6;ii++){
						result += Apples.pnum[i].old_own[ii] + " ";
					}
					result += "<br>~~~~~~~~~~~~~~~~<br>";
				}
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String showAll(int sx,int sy){
		String result = new String();
		if(sy<1||sx<1){
			return prob;
		}else{
			for(short i=0;i<people.peoMax;i++){
				if(people.alive[i] == true){
					if(Apples.locationcollect[i][0]<sx*100 && Apples.locationcollect[i][0]>=(sx-1)*100){
						if(Apples.locationcollect[i][1]<sy*100 && Apples.locationcollect[i][1]>=(sy-1)*100){
							result += i + " " + Apples.pnum[i].new_location[0] + ", " + Apples.pnum[i].new_location[1] + " HP: " +
								Apples.pnum[i].new_health[situation.hp] + 
								" mental: " + Apples.pnum[i].new_health[situation.mental] + 
								" lactate: " + Apples.pnum[i].new_health[situation.lactate] + 
								" motion: " + motion.convertnum(Apples.thinkreport[i]) + "<br>";
						}
					}
				}
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String showOwn(){
		String result = "";
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i]==true){
				result += i + ": ";
				for(int j=0;j<situation.ownName.length;j++){
					result += Apples.pnum[i].old_own[j] + " ";
				}
				result += "<br>";
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String sall(){
		String result = new String();
		for(int i=0;i<slime.slimeMax;i++){
			if(slime.slimealive[i]==true)
				result += Apples.snum[i].printslime();
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String mempeople(){
		String result = "";
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i]==true){
				result += i + " : ";
				for(int j=0;j<5;j++){
					result += Apples.pnum[i].mem_others[j].name + ", ";
				}
				result += "<br>";
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String peogene(){
		String result = "";
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i]==true){
				result += i + " : ";
				for(int j=0;j<20;j++){
					if(Apples.pnum[i].gene[j]==true){
						result += "1";
					}else{
						result += "0";
					}
				}
				result += "<br>";
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	private String showGoal(){
		String result = "";
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i]==true){
				result += i + " : " + Apples.pnum[i].goal[0] + " " 
						+ Apples.pnum[i].goal[1] + " " + Apples.pnum[i].goal[2] + "<br>";
			}
		}
		return result;
	}
	// -----------------------------------------------------------------------------------------------//
	// exist is used for check if the position have already added to the text.
	private String showGround(int px, int py,int item){
		String re = "";
		short exist = 0;
		if(item==-1){
			for(int i=(px-1)*100;i<px*100;i++){
				for(int j=(py-1)*100;j<py*100;j++){
					for(int k=0;k<situation.ownName.length;k++){
						if(Apples.groundthing[0][k][i][j]>0){
							if(exist==0){
								exist = 1;
								re += i + " " + j + ": ";
							}
							re += situation.convertsitu(k, "own") + " " + Apples.groundthing[0][k][i][j];
						}
					}
					if(exist==1){
						re += "<br>";
						exist = 0;
					}
				}
			}
		}else{
			for(int i=(px-1)*100;i<px*100;i++)
				for(int j=(py-1)*100;j<py*100;j++)
					if(Apples.groundthing[0][item][i][j]>0){
						re += i + " " + j + ": " + situation.convertsitu(item, "own") + " " + 
						Apples.groundthing[0][item][i][j] + "<br>";
					}
		}		
		return re;
	}
	// -----------------------------------------------------------------------------------------------//
	private int outPutFile(){
		try{
			PrintWriter ff = new PrintWriter("data.txt");
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return 0;
	}
	// -----------------------------------------------------------------------------------------------//
	private void formstr(String a){
		prestr = a;
		try{
			show.setText(htmlini + prestr + htmllast);
		}catch(NullPointerException e){}
	}
	private int toint(String a){
		try{
			return Integer.parseInt(a);
		}catch (NumberFormatException e) {
		    return -1;
		}
	}
}
