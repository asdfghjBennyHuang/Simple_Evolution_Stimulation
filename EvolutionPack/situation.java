package EvolutionPack;

import java.util.Objects;

public class situation {
	short[] old_location = new short[2];
	short[] new_location = new short[2];
	// health : no 2 and 3
	static final public String[] healthName = 
		{"hp","mental","energybox","","lactate"};
	static final public String[] ownName = 
		{"stone","wood","iron","meat","water","fern"};
	
	short[] new_health = {3000, 80, 0, 0, 0};
	short[] old_health = {3000, 80, 0, 0, 0};
	static final public int hp=0,
							mental=1,
							energybox=2,
							
							lactate=4;
	// own
	short[] new_own = new short[20];
	short[] old_own = new short[20];
	static final public int stone=0,
							wood=1,
							iron=2,
							meat=3,
							water=4,
							fern=5;
	
	public void situ_new_to_old(){
		old_health = new_health.clone();
		old_own = new_own.clone();
		if(new_location[0]<0){
			new_location[0] = 0;
		}else if(new_location[0]>Apples.boundx){
			new_location[0] = Apples.boundx;
		}
		if(new_location[1]<0){
			new_location[1] = 0;
		}else if(new_location[1]>Apples.boundy){
			new_location[1] = Apples.boundy;
		}
		old_location = new_location.clone();
	}
	
	public static int convertsitu(String n, String opt){
		if(Objects.equals(opt, "own")){
			for(int i=0;i<ownName.length;i++){
				if(Objects.equals(ownName[i],n)){
					return i;
				}
			}
			return -1;
		}else if(Objects.equals(opt, "health")){
			for(int i=0;i<healthName.length;i++){
				if(Objects.equals(healthName[i],n)){
					return i;
				}
			}
			return -1;
		}else{
			return -1;
		}
	}
	public static String convertsitu(int n, String opt){
		if(Objects.equals(opt, "own")){
			if( n>=0 && n<ownName.length ){
				return ownName[n];
			}else{
				return "Unknown";
			}
		}else if(Objects.equals(opt, "health")){
			if( n>=0 && n<healthName.length ){
				return healthName[n];
			}else{
				return "Unknown";
			}
		}else{
			return "Unknown";
		}	
	}
	// -- Constructor -- Constructor -- Constructor -- Constructor -- Constructor -- Constructor -- //
	public situation(){
	}
	
	// Tool functions -- Tool functions -- Tool functions -- Tool functions -- Tool functions -- Tool functions --//
	private short boolcount(boolean[] g,int start, int end){
		short result = 0;
		for(int i=start;i<end+1;i++)
			if(g[i])
				result++;
		return result;
	}
	static public short adjustRange(int x){
		if(x<0){
			return 0;
		}else if(x>=1000){
			return 999;
		}
		return (short)x;
	}
}