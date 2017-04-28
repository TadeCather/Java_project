package com.tad.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Blood {
	
	int x, y, w, h;
	private boolean live = true;
	
	

	TankWarClient tc;
	int step;
	
	//指明血块运动的轨迹，由pos中的各个点构成
	private int[][] pos= {
			{350, 300}, {360, 300}, {375, 275},{400, 200}, 
			{360, 270}, {278, 452}, {350 ,280}};
	
	
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public void draw(Graphics g) {
		if(!live) return;
		
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}
	
	private void move() {
		step ++;
		if(step == pos.length)
			step = 0;
		x = pos[step][0];
		y = pos[step][0];
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
	}

}








