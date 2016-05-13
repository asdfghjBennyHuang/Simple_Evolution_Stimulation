package EvolutionPack;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

public class gui extends JPanel{
		int pixel = 5;
		public static int enlarge = 2;
		public static int showSee=0;
		public static int showGrid=0;
		
	public gui(){
		JButton bba[] = new JButton[3];
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		for(int i=0;i<3;i++)
			bba[i] = new JButton();
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = -24;
		c.ipady = 0;
		add(bba[0],c);
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 1000*enlarge - 30;
		c.ipady = 0;
		add(bba[1],c);
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = -24;
		c.ipady = 1000*enlarge - 7;
		add(bba[2],c);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawRect(10, 10, 1000*enlarge, 1000*enlarge);
		// draw the grids
		if(showGrid==1){
			for(short i=0;i<Apples.boundx;i+=100){
				g.drawLine(i*enlarge+10, 10, i*enlarge+10, Apples.boundy*enlarge+10);
			}
			for(short j=0;j<Apples.boundy;j+=100){
				g.drawLine(10, j*enlarge+10, Apples.boundx*enlarge+10, j*enlarge+10);
			}
		}
		/* draw people
		 * normal : blue
		 * hit : green
		 * sleep : black
		 * attack : red X
		 * */
		for(int i=0;i<people.peoMax;i++){
			g.setColor(Color.blue);
			if(people.alive[i] == true){
				if( Apples.thinkreport[i] == 10 ){
					g.setColor(Color.black);
				}else if( Apples.thinkreport[i] == 5 ){
					g.setColor(Color.green);
				}
				int drawx = Apples.locationcollect[i][0]*enlarge + 10;
				int drawy = Apples.locationcollect[i][1]*enlarge + 10;
				g.drawRect( drawx, drawy, pixel, pixel);
				
				// draw the see blue lines and the attack red lines
				if( Apples.thinkreport[i] == 2 && showSee==1 ){
					int[] tt = new int[4];
					tt[0] = people.adjustRange( Apples.locationcollect[i][0]-50 )*enlarge + (short)10;
					tt[1] = people.adjustRange( Apples.locationcollect[i][1]-50 )*enlarge + (short)10;
					tt[2] = people.adjustRange( Apples.locationcollect[i][0]+50 )*enlarge + (short)10;
					tt[3] = people.adjustRange( Apples.locationcollect[i][1]+50 )*enlarge + (short)10;
					g.setColor(Color.blue);
					g.drawLine(drawx, drawy, tt[0], tt[1]);
					g.drawLine(drawx, drawy, tt[2], tt[1]);
					g.drawLine(drawx, drawy, tt[0], tt[3]);
					g.drawLine(drawx, drawy, tt[2], tt[3]);
				}
				if( Apples.thinkreport[i] == 4){
					g.setColor(Color.red);
					int[] tt = new int[4];
					tt[0] = ( Apples.locationcollect[i][0]-1>=0?Apples.locationcollect[i][0]-1:0 )*enlarge + 10;
					tt[1] = ( Apples.locationcollect[i][1]-1>=0?Apples.locationcollect[i][1]-1:0 )*enlarge + 10;
					tt[2] = ( Apples.locationcollect[i][0]+1<=999?Apples.locationcollect[i][0]+1:999 )*enlarge + 10;
					tt[3] = ( Apples.locationcollect[i][1]+1<=999?Apples.locationcollect[i][1]+1:999 )*enlarge + 10;
					g.drawLine(drawx, drawy, tt[0], tt[1]);
					g.drawLine(drawx, drawy, tt[2], tt[1]);
					g.drawLine(drawx, drawy, tt[0], tt[3]);
					g.drawLine(drawx, drawy, tt[2], tt[3]);
				}
			}
		}
		
		//draw slimes
		g.setColor(Color.red);
		for(int i=0;i<slime.slimeMax;i++){
			if(slime.slimealive[i] == true){
				g.drawOval( Apples.snum[i].location[0]*enlarge + 10, Apples.snum[i].location[1]*enlarge + 10, pixel, pixel);
			}
		}
		
		// draw groundthings
		for(int i=0;i<Apples.boundx;i++){
			for(int j=0;j<Apples.boundy;j++){
				if(Apples.groundthing[0][0][i][j] > 0){
					g.setColor(Color.white);
					g.fillRect( i*enlarge + 10, j*enlarge + 10, pixel, pixel);
				}
				if(Apples.groundthing[0][1][i][j] > 0){
					g.setColor(Color.gray);
					g.fillRect( i*enlarge + 10, j*enlarge + 10, pixel, pixel);
				}
				if(Apples.groundthing[0][2][i][j] > 0){
					g.setColor(Color.yellow);
					g.fillRect( i*enlarge + 10, j*enlarge + 10, pixel, pixel);
				}
				if(Apples.groundthing[0][3][i][j] > 0){
					g.setColor( new Color( (float)0.5, (float)0, (float)0.2) );
					g.fillRect( i*enlarge + 10, j*enlarge + 10, pixel, pixel);
				}
				if(Apples.groundthing[0][4][i][j] > 0){
					g.setColor( Color.blue );
					g.fillRect( i*enlarge + 10, j*enlarge + 10, pixel, pixel);
				}
				if( Apples.groundthing[0][5][i][j] > 0){
					g.setColor( Color.green );
					g.fillRect( i*enlarge + 10, j*enlarge + 10, pixel, pixel);
				}
			}
		}
	}
}
