package EvolutionPack;
import java.util.Random;

public class people extends memory{
	Random dice = new Random();
	static final int peoMax = 100;
	static boolean alive[] = new boolean[peoMax];
	static short Num = 0,All = 0;
	short number;
	// All is for number of all people ever born. Num is only total amount for living ones. 
	// Number is like a name for the person itself.
	boolean[] gene = new boolean[200];
	
	short sleept = 0;
	
	short[] goal = new short[3];
	short[] home = new short[3];
	short[] farm = new short[3];
	
	short age = 0;
	short seeRange;


/*
	location 
		0:original		1:new
		0:x				1:y
	goal:
		0: no goal
		1: leave the baby
		2: just travel
		3: leave because crowded
		4: going home
		5: build a farm
*/
	
// Think function -- Think function -- Think function -- Think function -- Think function --//
// Think function -- Think function -- Think function -- Think function -- Think function --//
	public short think(){
		// if have to rest
		short ifrest = checkRest();
		if(ifrest!=0){
			return motion.perform(this,ifrest);
		}
		
		short doFarm = setFarm();
		if(doFarm!=0){
			return motion.perform(this, doFarm);
		}
		
		short ifpickmeat = pickmeat2(situation.meat);
		if(ifpickmeat!=0){
			return motion.perform(this,ifpickmeat);
		}
		if(old_own[situation.iron]>0 && farm[0]==0){
			motion_goal.putWhat = situation.iron;
			motion_goal.putNum = new_own[situation.iron];
			return motion.put(this);
		}
		if(old_health[situation.hp]>5000)	{
			if(dice.nextInt(10)>6){
				goal[0]=1;
				goal[1] = adjustRange(old_location[0]+10);
				goal[2] = adjustRange(old_location[1]+10);
				return motion.born(this);
			}
		}
		
		if(hitSlime()==1){
			return motion.hit(this);
		}
		if(goal[0]==1){
			motion_goal.movedir = reachgoal().clone();
			return motion.move(this);
		}
		
		if(goal[0]!=3){
			if(home[0]==1 && old_own[situation.meat]>10){
				goal[0] = 4;
				goal[1] = home[1];
				goal[2] = home[2];
			}else{
				goal = nearthing(3).clone();
			}
		}
		
		if(goal[0]==2 || goal[0]==3 || goal[0]==4){
			motion_goal.movedir = reachgoal().clone();
			return motion.move(this);
		}else if(goal[0]!=1){
			goal = nearthing(2).clone();
		}
		if(dice.nextInt(2)==0){
			goal[0] = 2;
			goal[1] = adjustRange(old_location[0]+dice.nextInt(201)-100);
			goal[2] = adjustRange(old_location[1]+dice.nextInt(201)-100);
		}
		//Default motions
		return motion.see(this);
	}
	
// innerChange function -- innerChange function -- innerChange function -- innerChange function -- //
	// stroe the hp into the energy box
	public short innerChange(){
		if(old_health[situation.hp]>5000){
			int change = dice.nextInt(500)+500;
			new_health[situation.hp] -= change;
			new_health[situation.energybox] += change*0.9;
		}
		if(old_health[situation.hp] < 1000){
			if(old_health[situation.hp] > 1000){
				int change = dice.nextInt(500)+500;
				new_health[situation.energybox] -= change;
				new_health[situation.hp] += change*0.9;
			}
		}
		return 0;
	}

// Constructors -- Constructors -- Constructors -- Constructors -- Constructors -- Constructors --
	// first kind: for the function createEverything to initialize the world
	public people(short i){
		alive[i] = true;
		number = i;
		for(int j = 0;j<gene.length;j++)
			gene[j]= dice.nextBoolean();
		
		old_location[0] = (short)dice.nextInt(500);
		old_location[1] = (short)dice.nextInt(500);
		new_location = old_location.clone();
		Apples.locationcollect[i] = old_location.clone();
		Num++;
		All++;
	}
	// second kind: when it is born by a person
	public people(people one,short self){
		alive[self] = true;
		number = self;
		int tempge=0;
		gene = one.gene.clone();
		// decide the gene type
		for(int i = 0;i<gene.length;i++){
			tempge = dice.nextInt(3);
			if(tempge==0){
				if(gene[i]==false){
					gene[i]=true;
				}else{
					gene[i]=false;
				}
			}
		}
		old_location = mem_location[Apples.sp(-1)] = mem_location[Apples.sp(0)] = one.old_location.clone();
		new_location = one.old_location.clone();
		Apples.locationcollect[self] = old_location.clone();
		Num++;
		All++;
	}
	// third kind: for other class to call the function
	public people(){}

/////////////// Functions called in the think function.////////////////////////////////////
	//-----------------------------------------------------------------------------------------//
	// seeking the nearest meat, the dis variable is used for checking which is the nearest thing.
	private short[] nearthing(int num){
		int dis = 130;
		short[] vector = new short[3];
		vector[0]=0;
		for(short i=-65;i<=65;i++){
			for(short j=-65;j<=65;j++){
				if(mem_ground[ Apples.sp(-1) ][num][65+i][65+j]>0){
					if( Math.abs(i) + Math.abs(j) < dis){
						dis = Math.abs(i) + Math.abs(j);
						vector[0]=1;
						vector[1]=(short)(i+mem_location[Apples.sp(-1)][0]);
						vector[2]=(short)(j+mem_location[Apples.sp(-1)][1]);
					}
				}
			}
		}
		return vector;
	}
	//-----------------------------------------------------------------------------------------//
	// return if there is meat can be picked right away,also check if there is anyone nearby.
	// num = -1:get all, otherwise only pick if there is specific thing.
	private short pickmeat2(int num){
		short nearIt = 0;
		if( (Apples.day!=0||Apples.hour!=0) && mem_motion[Apples.sp(-1)]!=10){
			int[] movement = new int[2];
			movement[0]=old_location[0]-mem_location[Apples.sp(-1)][0];
			movement[1]=old_location[1]-mem_location[Apples.sp(-1)][1];
			if(num==-1){
				for(int i=0;i<ownName.length;i++){
					System.out.println("");
					if(mem_ground[ Apples.sp(-1) ][i][65+movement[0]][65+movement[1]] > 0){
						nearIt = 1;
					}
				}
			}else{
				if(mem_ground[ Apples.sp(-1) ][num][65-movement[0]][65-movement[1]] > 0){
					nearIt = 1;
				}
			}
		}
		if(nearIt==0){
//if(farm[0]>0)System.out.println("nothing here");
			return 0;
		}
		// if there is meat, check if there is anyone nearby. 
		for(int k=0;k<totalOthers;k++){
			if( mem_others[k].location[0] == old_location[0] && mem_others[k].location[1] == old_location[1] ){
//if(farm[0]>0)System.out.print("People here");
				//		System.out.print(number + " near!! " + m_others[Apples.ptime][k][0] + " ");
				if(gene[0]==true){
					motion_goal.attackPos = mem_others[k].location;
					return motion.convertMotion("attack");
				}else if(gene[1]==true){
					//		System.out.println(number + "Run! ");
					// leave and be polite
					goal[0]=3;
					goal[1] = adjustRange(old_location[0]+(gene[2]==true?10:-10));
					goal[2] = adjustRange(old_location[1]+(gene[3]==true?10:-10));
				}
			}
		}
		if(goal[0]==3){
//if(farm[0]>0)System.out.println("  Not picking!");
			return 0;
		}else{
//if(farm[0]>0)System.out.println("  picking!");
			return motion.convertMotion("pick");
		}
	}
	//-----------------------------------------------------------------------------------------//
	// gives next step to take to get to the goal. 
	private short[] reachgoal(){
		short[] a = new short[2];
		if(goal[0]==0){
			return a;
		}
		goal[1] = adjustRange(goal[1]);
		goal[2] = adjustRange(goal[2]);
		if( goal[1]-old_location[0]!=0 ){
			a[0] = (short)(goal[1]-old_location[0]>0 ? 1 : -1);
		}else if( goal[2]-old_location[1]!=0 ){
			a[1] = (short)(goal[2]-old_location[1]>0 ? 1 : -1);
		}
		if( Math.abs(old_location[0]+a[0])==goal[1] && Math.abs(old_location[1]+a[1])==goal[2] ){
			goal[0]=0;
			goal[1]=-1;
			goal[2]=-1;
		}
		return a;
	}
	//-----------------------------------------------------------------------------------------//
	// if there is a slime to hit, hit range is usually 3.
	private short hitSlime(){
		for(int i=0;i<totalSlime;i++){
			if(slime.slimealive[mem_slime[i].name]){
				if(Math.abs(mem_slime[i].location[0] - old_location[0])<=3 ){
					if(Math.abs(mem_slime[i].location[1] - old_location[1])<=3 ){
						motion_goal.hitSlime = mem_slime[i].name;
						return 1;
					}
				}
			}
		}
		return 0;
	}
	//-----------------------------------------------------------------------------------------//
	// if it should rest, or if hungry eat would be conduced.
	private short checkRest(){
		if(sleept>0){
			sleept-=1;
			return motion.sleep(this);
		}
		if(old_health[situation.lactate]>90)sleept=(short)(old_health[situation.lactate]/15);
		if(old_health[situation.mental]<10)sleept=(short)((100-old_health[situation.mental])/10);
		if(old_own[situation.meat] > 0){
			if(old_health[situation.hp] < 2000){
				if(old_own[situation.meat]<5){
					motion_goal.eatNum = old_own[situation.meat];
				}else{
					motion_goal.eatNum = 5;
				}
				return motion.convertMotion("eat");
			}
		}
		return 0;
	}
	//-----------------------------------------------------------------------------------------//
	
	//-----------------------------------------------------------------------------------------//
	// Set a place to be home, bring food to home
	private short setHome(short x,short y){
		home[0] = 1;
		home[1] = x;
		home[2] = y;
		return 0;
	}
	//-------------------------------------------------------------------------------------//
	// 	Farming Functions -- Farming Functions -- Farming Functions -- Farming Functions -- //
	// 	first setting
	/* 	goal[0] = 5 => already set farm
		farm[0] = 
			1 : no iron yet.
			2 : got iron but, no fern yet.
			3 : got material but haven't put it to farm.
			4 : have put it to farm, but no slime yet. 
	*/
	private short setFarm(){	
		if(farm[0]==0){
			if(old_own[situation.meat]>100){
				farm[0] = 1;
				int a = dice.nextInt(51)-25;
				if(a>0){
					a+=100;
				}else{
					a-=100;
				}
				int b = dice.nextInt(51)-25;
				if(b>0){
					b+=100;
				}else{
					b-=100;
				}
				System.out.println(number +  "farm[1]: " + adjustRange(old_location[0]+a) + " \n");
				farm[1] = adjustRange(old_location[0]+a);
				farm[2] = adjustRange(old_location[1]+b);
				if(old_own[situation.iron]>0){
					if(old_own[situation.fern]>0){
						farm[0] = 3;
					}else{
						farm[0] = 2;
					}
				}else{
					farm[0] = 1;
				}
			}else{
				return 0;
			}
		}
		if(farm[0]==1){
			if(old_own[situation.iron]==0){
				System.out.println(number + "Check picking");
				if(pickmeat2(situation.iron)>0){
					System.out.println(number + " is picking");
					return motion.convertMotion("pick");
				}
				System.out.println("Not picking");
				short[] temp = nearthing(situation.iron);
				System.out.println("Near " + temp[0] + " " + temp[1] + " " + temp[2]);
				if(temp[0]!=0){
					goal[0] = 5;
					goal[1] = temp[1];
					goal[2] = temp[2];
					motion_goal.movedir = reachgoal().clone();
					System.out.println("Moving...");
					return motion.convertMotion("move");
				}else{
					System.out.println("Seeing..");
					return motion.convertMotion("see");
				}
			}else{
				farm[0] = 2;
			}
			System.out.println(number + "Leaving...");
		}
		if(farm[0]==2){
			System.out.println(number + " into stage 2 ");
			if(old_own[situation.fern]==0){
				System.out.println(number + "Check picking fern");
				if(pickmeat2(situation.fern)>0){
					return motion.convertMotion("pick");
				}
				short[] temp = nearthing(situation.fern);
				if(temp[0]!=0){
					goal[0] = 5;
					goal[1] = temp[1];
					goal[2] = temp[2];
					motion_goal.movedir = reachgoal().clone();
					return motion.convertMotion("move");
				}else{
					return motion.convertMotion("see");
				}
			}else{
				farm[0] = 3;
			}
		}
		return 0;
	}
	
}