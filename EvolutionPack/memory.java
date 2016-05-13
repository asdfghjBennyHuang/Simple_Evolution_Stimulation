package EvolutionPack;

public class memory extends situation{
	static final int bound = 131,center = 65,dur=4,othermax=5;
	short[] home = {-1,-1};
	short[] mem_motion = new short[dur];
	short[][] mem_location = new short[dur][2];
	short[][][][] mem_ground = new short[dur][ownName.length][bound][bound]; 
	short totalOthers = 0;
	short totalSlime = 0;
	short[][] mem_health = new short[dur][healthName.length];
	short[][] mem_own = new short[dur][ownName.length];
	
	// c series store the information of the memorized stuff.
	public class cOthers{
		short name = 0;
		short[] location = new short[2];
		short movement = 0;
	}
	public class cSlimes{
		short name = 0;
		short[] location = new short[2];
	}
	// motion goal
	public class mg{
		short[] attackPos = new short[2];
		short hitSlime;
		people givePeo;
		short giveAim;
		short giveNum;
		short[] movedir = new short[2];
		people robPeo;
		short robAim;
		short eatNum;
		int putWhat;
		int putNum;
	}
	
	cOthers[] mem_others;
	cSlimes[] mem_slime;
	mg motion_goal;
	
	memory(){
		mem_others = new cOthers[othermax];
		mem_slime = new cSlimes[othermax];
		motion_goal = new mg();
		for(int i=0;i<othermax;i++){
			mem_others[i] = new cOthers();
			mem_slime[i] = new cSlimes();
		}
	}
}
