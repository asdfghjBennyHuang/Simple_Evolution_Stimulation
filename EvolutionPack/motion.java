package EvolutionPack;
import java.util.Objects;
import java.util.Random;
// 1.feel	
// 2.see	3.pick	4.attack 5.hit 6.give 7.move 8.rob 9.eat 10.sleep	11.born 12.put
// 100.seereal
// checkInRange seeslime seepeople seeground
/*
 * When decide to see, the see funciton do nothing, after everyone finish deciding, 
 * there will be functions(seeslime, seepeople, seeground) for people to feel. 
 * than people who see will call seereal instead. seereal function will call (seeslime, seepeople, seeground) 
 * but passing different variable to enlarge the range.
 */
public class motion{
	public static Random dice = new Random();
	static Thread th = new Thread();
	
	public static String[] motionName = {
		"feel","see","pick","attack","hit","give","move","rob","eat","sleep","born","put"
	};
	
//1.feel
	public static short feel(people a){
		if(a.old_health[situation.mental]>0){
			a.mem_location[Apples.spacenum] = a.old_location.clone();
			a.mem_own[Apples.spacenum] = a.old_own;
			a.mem_health[Apples.spacenum] = a.old_health;
			a.mem_motion[Apples.spacenum] = Apples.thinkreport[a.number];
			motion.seeslime(a,0);
			motion.seepeople(a, 0);
			motion.seeground(a, 0);
		}
		return 1;
	}
//2.see
	public static short see(people a){
		return 2;
	}
//3.pick
	public static short pick(people a){
		if(checkPeopleInRange( a.old_location[0] , a.old_location[1] , 0, 0) == 0){
			for(int k=0;k<situation.ownName.length;k++){
				a.new_own[k] += Apples.groundthing[0][k][ a.old_location[0] ][ a.old_location[1] ];
				Apples.groundthing[1][k][ a.old_location[0] ][ a.old_location[1] ]=0;
			}
		}
//System.out.println("su!" + a.location[0][0] + " " + a.location[0][1]);
//System.out.println("win "+	(a.situ1.own[situation.meat]-a.situ0.own[situation.meat]));
		a.new_health[situation.mental] -= 5;
		return 3;
	}
//4.attack
	public static short attack(people a){
		short x = a.motion_goal.attackPos[0];
		short y = a.motion_goal.attackPos[1];
		if( Math.abs(a.old_location[0]-x) <= (a.old_health[situation.mental]>30?3:1) 
				&& Math.abs(a.old_location[1]-y) <= (a.old_health[situation.mental]>30?3:1) ){		
			for(int i=0;i<people.peoMax;i++){
				if(people.alive[i]==true){
					if(Apples.locationcollect[i][0]==x && Apples.locationcollect[i][1]==y){
						Apples.pnum[i].new_health[situation.hp] -= a.old_health[situation.hp]*0.1;
					}
				}
			}
		}
System.out.println("attack!!" + x + " " + y);
		a.new_health[situation.hp] -= 40;
		return 4;
	}
//5.hit
	public static short hit(people a){
		short n = a.motion_goal.hitSlime;
		if(slime.slimealive[n]){
			slime c = Apples.snum[n];
			if( Math.abs(a.old_location[0]-c.location[0]) <= (a.old_health[situation.mental]>30?3:1) ){
				if( Math.abs(a.old_location[1]-c.location[1]) <= (a.old_health[situation.mental]>30?3:1) ){
					//here gene
					c.hp-= (a.old_health[situation.hp]*0.1) + (a.old_health[situation.hp]*0.1*5/3) ;
				}
			}
		}
		return 5;
	}
//6.give
	public static short give(people a){
		people b = a.motion_goal.givePeo;
		if( Math.abs(a.old_location[0]-b.old_location[0]) <= 1 ){
			if( Math.abs(a.old_location[1]-b.old_location[1]) <= 1 ){
				a.new_own[a.motion_goal.giveAim] -= a.motion_goal.giveNum;
				b.new_own[a.motion_goal.giveAim] += a.motion_goal.giveNum;
				a.new_health[situation.hp] -= 5;
			}
		}
		return 6;
	}
//7.move
	public static short move(people a){
		int x = a.motion_goal.movedir[0];
		int y = a.motion_goal.movedir[1];
		//here gene
		int lactateindex = 5/100;
		if(a.old_health[situation.lactate] < 100){
			if( (x<=1 && x>=-1 && y==0) || (x==0 && y<=1 &&y>=-1)){
				//here gene
				a.new_health[situation.hp] -= (short)5 + a.old_health[situation.lactate]/20 ;
				a.new_location[0]+=x;
				a.new_location[1]+=y;
				a.new_health[situation.mental] -= 1;
				a.new_health[situation.lactate] = (short)(1 + (a.new_health[situation.lactate]*(1.25- lactateindex )));
			}else{
				System.out.println("Breaking move rule!! " + a.number + " " + x + " " + y);
			}
		}else{
			System.out.println("too many lactate!!");
		}
		return 7;
	}
//8.rob
	public static short rob(people a){
		people b = a.motion_goal.robPeo;
		if( Math.abs(a.old_location[0]-b.old_location[0]) <= 1 ){
			if( Math.abs(a.old_location[1]-b.old_location[1]) <= 1 ){
				a.new_own[a.motion_goal.robAim] += b.old_own[a.motion_goal.robAim];
				b.new_own[a.motion_goal.robAim] = 0;
				a.new_health[situation.hp]-=100;
			}
		}
		return 8;
	}
//9.eat
	public static short eat(people a){
System.out.print("eat!! " + a.new_own[situation.meat] + " ");
		for(int i=a.motion_goal.eatNum;i>0;i--){
			if(a.old_health[situation.hp] < 4000)
				a.new_health[situation.hp] += 1000;
			else if(a.old_health[situation.hp] < 6000)
				a.new_health[situation.hp] += 700;
			else if(a.old_health[situation.hp] < 8000)
				a.new_health[situation.hp] += 300;
			else if(a.old_health[situation.hp] < 1000)
				a.new_health[situation.hp] += 100;
			else
				a.new_health[situation.hp] += 100;
		}
		a.new_own[situation.meat] -= a.motion_goal.eatNum;
System.out.println("eatleft " + a.new_own[situation.meat]);
		return 9;
	}
//10.sleep
	public static short sleep(people a){
		a.mem_motion[Apples.spacenum]=10;
		a.mem_location[Apples.spacenum]=a.old_location.clone();
		if(a.old_health[situation.mental]<=90)
			a.new_health[situation.mental]+=10;
		else
			a.new_health[situation.mental] = 100;
		if(a.old_health[situation.lactate] > 15){
			a.new_health[situation.lactate] -= 15;
		}else{
			a.new_health[situation.lactate] = 1;
		}
		return 10;
	}
//11.born
	public static short born(people a){
		if(people.Num>people.peoMax){
System.out.println("Too many people!! ");
			try{
				th.sleep(1000000);
			}catch(InterruptedException e){}
		}
		for(short i=0;i<people.peoMax;i++){
			if(people.alive[i]==false){
System.out.println("born!! " + i);
				Apples.pnum[i] = new people(a,i);
				break;
			}
		}
		a.new_health[situation.hp] -= 3000;
		return 11;
	}
//12.put
	public static short put(people a){
		if(a.old_own[a.motion_goal.putWhat]>=a.motion_goal.putNum){
			Apples.groundthing[1][a.motion_goal.putWhat][a.old_location[0]][a.old_location[1]]+=a.motion_goal.putNum;
			a.new_own[a.motion_goal.putWhat]-=a.motion_goal.putNum;
		}
		a.new_health[situation.lactate]-=2;
		return 12;
	}
//101.seereal
	public static short seereal(people a){
		a.mem_location[Apples.spacenum] = a.old_location.clone();
		a.mem_own[Apples.spacenum] = a.old_own.clone();
		a.mem_health[Apples.spacenum] = a.old_health.clone();
		a.mem_motion[Apples.spacenum] = Apples.thinkreport[a.number];
		motion.seeslime(a, 1);
		motion.seepeople(a, 1);
		motion.seeground(a, 1);
		//here gene
		a.new_health[situation.hp] -= 5 + 5/10;
		return 100;
	}
//////////////////////////////////////////////////////

//checkInRange
	public static int checkPeopleInRange(int chx,int chy,int distance,int wantnum){
		// wantnum: 0.return if people exist 1.return the amount of people 
		int count=-1;
		for(int i=0;i<people.peoMax;i++){
			if(people.alive[i]==true){
				if( Math.abs( Apples.locationcollect[i][0] - chx ) <= distance ){
					if( Math.abs( Apples.locationcollect[i][1] - chy ) <= distance ){
						count++;
					}
				}
			}
		}
		if(wantnum==1){
			return count;
		}else{
			if(count==0)
				return 0;
			else
				return 1;
		}
	}
// See slime function
	public static short seeslime(people me, int range){
		// range: 0 for feel, 1 for see
		short tempsnum = 0;
		short tempslime[] = new short[50];
		short therange = (short)(range==0?(5)/2-3:(5)*3-14);
		for(short i=0;i<slime.slimeMax;i++){
			if(slime.slimealive[i] == true){
				if(Math.abs( Apples.snum[i].location[0] - me.old_location[0] ) <= (range==0?10:50) + therange ){
					if(Math.abs( Apples.snum[i].location[1] - me.old_location[1] ) <= (range==0?10:50) + therange ){
						tempslime[tempsnum] = i;
						tempsnum++;
						if(tempsnum==50)break;
					}
				}
			}
		}
		if(tempsnum<memory.othermax){
			me.totalSlime = tempsnum;
			for(int i=0;i<tempsnum;i++){
				me.mem_slime[i].name = tempslime[i];
				me.mem_slime[i].location = Apples.snum[tempslime[i]].location.clone();
			}
		}else{
			me.totalSlime = memory.othermax;
			for(int i=0;i<memory.othermax;i++){
				me.mem_slime[i].name = tempslime[i];
				me.mem_slime[i].location = Apples.snum[ tempslime[ dice.nextInt(tempsnum) ] ].location.clone();
		
			}
		}
		return tempsnum;
	}
// See people function
	public static short seepeople(people me, int range){
		// range: 0 for feel, 1 for see
		// wantnum: 0 for 
		short temppnum = 0;
		short temppeo[] = new short[50];
		//here gene
		short therange = (short)(range==0?5/2-3:5*3-14);
		for(short i=0;i<people.peoMax;i++){
			if(people.alive[i] == true){
				if( Math.abs(Apples.locationcollect[i][0] - me.old_location[0]) <= (range==0?10:50) + therange){
					if( Math.abs(Apples.locationcollect[i][1] - me.old_location[1]) <= (range==0?10:50) + therange){
						if( i != me.number){
							temppeo[temppnum] = i;
							temppnum++;
							if(temppnum==50)break;
						}
					}
				}
			}
		}
		if(temppnum<memory.othermax){
			me.totalOthers = temppnum;
			for(short i=0;i<temppnum;i++){
				short j = temppeo[i];
				me.mem_others[i].name = j;
				me.mem_others[i].location = Apples.pnum[j].old_location.clone();
				me.mem_others[i].movement = Apples.thinkreport[j];
			}
		}else{
			me.totalOthers = memory.othermax;
			for(short i=0;i<memory.othermax;i++){
				short choose = temppeo[dice.nextInt(temppnum)];
				me.mem_others[i].name = choose;
				me.mem_others[i].location = Apples.pnum[ choose ].old_location.clone();
				me.mem_others[i].movement = Apples.thinkreport[ choose ];
			}
		}			
		return temppnum;
	}
// See ground function
	public static short seeground(people me, int therange){
		// range: 0 for feel, 1 for see
		// here gene
		short range = (short)( therange==0?10:50 );
		int[] boardx = new int[2];
		int[] boardy = new int[2];
		int ii,ij;
		boardx[0] = people.adjustRange( me.old_location[0]-range );
		boardx[1] = people.adjustRange( me.old_location[0]+range );
		boardy[0] = people.adjustRange( me.old_location[1]-range );
		boardy[1] = people.adjustRange( me.old_location[1]+range );
		for(int k=0;k<situation.ownName.length;k++){
			ii = boardx[0] - me.old_location[0] + 65;
			for(int i=boardx[0];i<=boardx[1];i++){
				ij = boardy[0] - me.old_location[1] + 65;
				for(int j=boardy[0];j<=boardy[1];j++){
					me.mem_ground[Apples.spacenum][k][ii][ij] = Apples.groundthing[0][k][i][j];
					ij++;
				}
				ii++;
			}
		}
		return 0;
	}
	// -------------------------------------------------------------------------------//
	// Converting functions ----------------------------------------------------------//
	public static short perform(people me,short a){
		switch(a){
		case 1:feel(me);break;
		case 2:see(me);break;
		case 3:pick(me);break;
		case 4:attack(me);break;
		case 5:hit(me);break;
		case 6:give(me);break;
		case 7:move(me);break;
		case 8:rob(me);break;
		case 9:eat(me);break;
		case 10:sleep(me);break;
		case 11:born(me);break;
		case 12:put(me);break;
		default:
			try{
				System.out.println("Unknown perform!!: " + a + " from :" + me.number );
				th.sleep(5);
			}catch(InterruptedException e){}
		}
		return a;
	}
	
	public static short convertMotion(String x){
		for(short i=0;i<motionName.length;i++){
			if(Objects.equals(x, motionName[i])){
				return (short)(i+1);
			}
		}
		System.out.println("Problem in convertMotion in class motion!");
		try{
			Thread.sleep(5000);
		}catch(InterruptedException e){}
		return 0;
	}
	
	public static String convertnum(short x){
		switch(x){
			case 1: return "feel";
			case 2: return "see";
			case 3: return "pick";
			case 4: return "attack";
			case 5: return "hit";
			case 6: return "give";
			case 7: return "move";
			case 8: return "rob";
			case 9: return "eat";
			case 10: return "sleep";
			case 11: return "born";
			case 12: return "put";
			default : return "unknown";
		}
	}
}