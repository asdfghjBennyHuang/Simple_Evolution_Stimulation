package EvolutionPack;
import java.util.Random;

public class slime {
	static short slimeAmount = 0;
	static short slimeNum = 0;
	static final short slimeMax = 1000;
	static boolean[] slimealive = new boolean[slimeMax];
	short number, hp;
	short[] location = new short[2];
	static final int ironRange = 25, ironEffect = 1, fernRange = 20;
	Random dice = new Random();

	// slimeAmount is for number of all slimes ever born. slimeNum is only for living ones.	
	slime(){
		for(short p=0;p<slimeMax;p++){
			if(slimealive[p] == false){
				number = p;
				slimealive[p] = true; 
				break;
			}
		}
		slimeAmount++;
		slimeNum++;
		hp = 60;
		location[0] = (short)dice.nextInt(400);
		location[1] = (short)dice.nextInt(400);
	}
	slime(int i,short self){
		number = self;
		slimealive[self] = true; 
		slimeAmount++;
		slimeNum++;
		hp = 20;
		location[0] = (short)Apples.snum[i].location[0];
		location[1] = (short)Apples.snum[i].location[1];
	}
	void sdie(){
			Apples.groundthing[1][3][ location[0] ][ location[1] ] ++;
			slimealive[number] = false;
			slimeNum--;
			Apples.snum[number] = null;
	}
	void bornslime(){
		if(slimeNum>=slimeMax){
			System.out.println("Slime more than slimeMax!!");
	//		try{
	//			Thread.sleep(1000000);
	//		}catch(InterruptedException e){}
		}else{
			for(short i=0;i<slime.slimeMax;i++){
				if(slimealive[i]==false){
					Apples.snum[i] = new slime(number,i);
					break;
				}
			}
			hp -= 80;
		}
	}
	void recover(){
		int slimeiron = 0;
		int tempj0 = people.adjustRange(location[0]-slime.ironRange);
		int tempk0 = people.adjustRange(location[1]-slime.ironRange);
		int tempj1 = people.adjustRange(location[0]+slime.ironRange);
		int tempk1 = people.adjustRange(location[1]+slime.ironRange);
		for(int j=tempj0;j<=tempj1;j++){
			for(int k=tempk0;k<=tempk1;k++){
				slimeiron += Apples.groundthing[0][situation.iron][ j ][ k ];
			}
		}
		hp += slimeiron*slime.ironEffect;
	}
	public void move(){
		int ifmove = dice.nextInt(2);
		short[] wherefern = gofern().clone();
		if(ifmove>0){
			location[0]+= dice.nextInt(3)-1 + wherefern[0];
			location[1]+= dice.nextInt(3)-1 + wherefern[1];
		}
	}
	public short[] gofern(){
		short[] re = new short[2];
		for(int i=0-fernRange;i<=fernRange;i++){
			if(location[0]+i>=0 && location[0]+i<Apples.boundx){
				int x = location[0]+i;
				for(int j=0-fernRange;j<=fernRange;j++){
					if(location[1]+j>=0 && location[1]+j<Apples.boundy)
						if(Apples.groundthing[0][situation.fern][x][location[1]+j]>0)
							if(dice.nextInt(10)<2){
								if(i>0){
									re[0]+=1;
								}else if(i<0){
									re[0]-=1;
								}
								if(j>0){
									re[1]+=1;
								}else if(j<0){
									re[1]-=1;
								}
							}
				}
			}
		}
		return re;
	}
	public String printslime(){
		String re = "";
		re += number + ": hp" + hp + " locarion: " + location[0] + " " + location[1] + "<br>";
		return re;
	}
}
	
