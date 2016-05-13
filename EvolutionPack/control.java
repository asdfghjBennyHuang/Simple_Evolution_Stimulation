package EvolutionPack;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

public class control extends JPanel{
	public static int ifPause = 1; 
	JButton fast = new JButton("faster");
	JButton slow = new JButton("slower");
	JButton pause = new JButton("pause");
	JButton go = new JButton("go");
	JButton showsee = new JButton("show see");
	JButton enlarge = new JButton("enlarge");
	JButton pull = new JButton("pull");
	JButton showGrid = new JButton("show grid");
	JLabel time = new JLabel(tostr1(Apples.sleepTime));
	JLabel scale = new JLabel(tostr2(gui.enlarge));
	JLabel nowtime = new JLabel();
	JTextField cmd = new JTextField(30);
	
	
	public control(){
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		ego ego = new ego();
		epause epause = new epause();
		efast efast = new efast();
		eslow eslow = new eslow();
		eenlarge elar = new eenlarge();
		epull epl = new epull();
		eshowsee eshowsee = new eshowsee();
		eshowGrid esg = new eshowGrid();
		fast.addActionListener(efast);
		slow.addActionListener(eslow);
		go.addActionListener(ego);
		pause.addActionListener(epause);
		showsee.addActionListener(eshowsee);
		enlarge.addActionListener(elar);
		pull.addActionListener(epl);
		showGrid.addActionListener(esg);
		
		c.gridx=0;c.gridy=0;
		c.gridwidth=3;
		c.fill=c.HORIZONTAL;
		add(nowtime,c);
		c.ipadx=20;
		c.gridx=0;c.gridy=1;
		c.gridwidth=1;
		c.fill=c.NONE;
		add(fast,c);
		c.gridx=1;
		add(slow,c);
		c.gridx=2;
		add(pause,c);
		c.gridx=3;
		add(go,c);
		c.gridx=0;c.gridy=2;
		c.gridwidth=4;
		add(time,c);
		
		c.ipadx=0;
		c.gridwidth=1;
		c.gridy=3;c.gridx=0;
		c.fill=c.HORIZONTAL;
		add(enlarge,c);
		c.gridx=1;
		add(pull,c);
		c.gridx=2;c.gridwidth=2;
		add(scale,c);
		c.gridx=0;c.gridy=4;c.gridwidth=2;
		add(showsee,c);
		c.gridy=5;
		add(showGrid,c);
		
	}
	protected String tostr1(int t){
		return String.format("<HTML><font size=5>sleep time = %s</font></HTML>",t);
	}
	protected String tostr2(int t){
		return String.format("<HTML><font size=5>enlarge time = %s</font></HTML>",t);
	}
	public void setNowtime(){
		String start = "<HTML><font size=5>";
		String nowstr = String.format(" day %s hour %s minute %s " , Apples.day , Apples.hour , Apples.minute); 
		String end = "</font></HTML>";
		nowtime.setText(start+nowstr+end);
	}
	public class ego implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ifPause = 0;
			Apples.sleepTime = 100;
			time.setText(tostr1(Apples.sleepTime));
		}
	}
	public class epause implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ifPause = 1;
			time.setText(tostr1(Apples.sleepTime));
		}
	}
	public class efast implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Apples.sleepTime = (int)(Apples.sleepTime*0.8);
			time.setText(tostr1(Apples.sleepTime));
		}
	}
	public class eslow implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Apples.sleepTime = (int)(Apples.sleepTime*1.2);
			time.setText(tostr1(Apples.sleepTime));
		}
	}
	public class eshowsee implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(gui.showSee==0){
				gui.showSee = 1;
			}else{
				gui.showSee = 0;
			}
		}
	}
	public class eenlarge implements ActionListener{
		public void actionPerformed(ActionEvent e){
			gui.enlarge += 1;
			scale.setText(tostr2(gui.enlarge));
		}
	}
	public class epull implements ActionListener{
		public void actionPerformed(ActionEvent e){
			gui.enlarge -= 1;
			scale.setText(tostr2(gui.enlarge));
		}
	}
	public class eshowGrid implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(gui.showGrid==0){
				gui.showGrid=1;
			}else{
				gui.showGrid=0;
			}
			Apples.evogui.repaint();
		}
	}
}
