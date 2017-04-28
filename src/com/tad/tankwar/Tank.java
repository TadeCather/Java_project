package com.tad.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;



//Tank 包装成新的类

public class Tank {
	/**
	 * 整个坦克游戏的横向速度
	 */
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	private int x, y;
	private int oldX, oldY;
	private int life = 100;
	
	private int step =rand.nextInt(12) + 3;

	TankWarClient tc;
	private BloodBar bb = new BloodBar();
	
	private boolean good;
	private boolean live = true;


	private boolean bL =  false, bU = false, bR = false, bD =false;
	enum Direction {L, LU, U, RU, R, RD, D, LD,STOP};
	
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	private static Random rand = new Random();
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean isGood() {
		return good;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankWarClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		
		if (!live) {
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		
		Color c = g.getColor();
		//设置不同的背景色
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		//画出血条
		if(good) bb.draw(g);
		
		switch (ptDir){
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x , y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x , y + Tank.HEIGHT);
			break;
		case STOP:
			break;
			
		}
		
		move();
		
	}
	
	public void move(){
		
//记录上次的位置
		this.oldX = x;
		this.oldY = y;
		
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
		case STOP:
			break;
			
		}
		if (this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankWarClient.GAME_WIDTH)
			x = TankWarClient.GAME_WIDTH-Tank.WIDTH;
		if(y + Tank.HEIGHT > TankWarClient.GAME_HEIGHT)
			y = TankWarClient.GAME_HEIGHT - Tank.HEIGHT; 
		
		if(!good){
			Direction[] dirs = Direction.values();
			if(step == 0){
				step = rand.nextInt(12) + 3;
				int rn = rand.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step --;
			
			if(rand.nextInt(40) > 38) fire();
		}
		
		
	}
	
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch (key){
		case KeyEvent.VK_F2 :
			if (!this.live){
				this.live = true;
				this.life = 100;
			}
		
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU =true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		case KeyEvent.VK_A :
			superFire();
		}
		locateDirection();
	}
	
	private void locateDirection(){
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	//处理键盘抬起来的消息


	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key){
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		}
		locateDirection();
	}
	
	public Missile fire(){
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, ptDir, tc);
		tc.missile.add(m);
		return m;
	}
	
	
	//super Missile
	public Missile fire(Direction dir){
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir, tc);
		tc.missile.add(m);
		return m;
	}
	
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i = 0; i < 8; i++){
			fire(dirs[i]);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT); 
	}
	
	/**
	 * 撞墙
	 * @param w 被撞的墙
	 * @return 撞上返回true，否则返回true
	 */
	public boolean collideWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			
			this.stay();
			return true;
		}
		return false;
	}
	//改变坦克黏住墙的情况
	private void stay(){
		x = oldX;
		y = oldY;
	}
	
	public boolean collideWithTank(List<Tank> tanks){
		
		for(Tank t : tanks){
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		
		return false;
	}

	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * life /100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}
	public boolean eatBloob(Blood b){
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
		
	}
	
	
	
}














