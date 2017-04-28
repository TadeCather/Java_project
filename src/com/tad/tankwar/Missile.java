package com.tad.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.tad.tankwar.Tank.Direction;

public class Missile {
	
	public static final int  XSPEED = 10;
	public static final int  YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT =10;
	
	private boolean live = true;
	
	
	int x, y;
	
	Tank.Direction dir;
	private TankWarClient tc;
	private boolean good;
	
	public boolean isLive() {
		return  live;
	}
	
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankWarClient tc){
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	public void draw(Graphics g){
		
		if (!live) {
			tc.missile.remove(this);
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
	private void move() {
		switch (dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		
		
		if(x <= 0 || y <=0 || x >= TankWarClient.GAME_WIDTH 
				|| y >= TankWarClient.GAME_HEIGHT)
			live = false;
			//tc.missile.remove(this);
		
	}
	
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT); 
	}
	
	public boolean hitTank(Tank t){
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()){
			if(t.isGood()){
				t.setLife(t.getLife() - 20);
				if(t.getLife() <= 0) t.setLive(false);
			}else{
				t.setLive(false);
			}
			
			this.live = false;
			Explodesion e = new Explodesion(x, y, tc);
			tc.explodesions.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(Tank t : tanks){
			if(hitTank(t))
				return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w){
		
		if(this.live && this.getRect().intersects(w.getRect())){
			this.live = false;
			return true;
		}
		return false;
		
		
	}

}
