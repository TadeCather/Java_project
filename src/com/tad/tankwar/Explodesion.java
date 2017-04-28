package com.tad.tankwar;

import java.awt.Color;
import java.awt.Graphics;

public class Explodesion {
	
	int x, y;
	private boolean live = true;
	
	int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};
	int step;
	
	private TankWarClient tc;
	
	public Explodesion(int x, int y, TankWarClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if (!live) {
			tc.explodesions.remove(this);
			return;
		}
		
		if(step == diameter.length){
			live = false;
			step = 0;
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step ++;
 	}
	
	
}
