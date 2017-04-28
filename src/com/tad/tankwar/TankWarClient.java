package com.tad.tankwar;

import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类的作用是坦克游戏的主窗口
 * @author TAD
 *
 */
public class TankWarClient extends Frame  {
	
	//常量化参数
	public static final int GAME_WIDTH = 700;
	public static final int GAME_HEIGHT = 600;
	
	
	Tank myTank = new Tank(150, 50, true,Tank.Direction.STOP, this);
	//Tank enemyTank = new Tank(100, 100, false, this);
	
	Wall w1 = new Wall(200, 300, 20, 100, this);
	Wall w2 = new Wall(300, 200, 200, 30, this);
	
	Blood b =new Blood();
	
	List<Tank> tanks = new ArrayList<Tank>();
 	List<Explodesion> explodesions = new ArrayList<Explodesion>();
	List<Missile> missile = new ArrayList<Missile>();
	
	Image offScreenImage  = null;
	
	public void paint(Graphics g){
		
		/*
		 * 指明子弹-爆炸-坦克的数量
		 * 以及坦克的生命值
		 */
		g.drawString("missiles count:" + missile.size(), 10, 60);
		g.drawString("Explosion count:" + explodesions.size(), 10, 80);
		g.drawString("Tanks count:" + tanks.size(), 10, 100);
		g.drawString("MyTank Life:" + myTank.getLife(), 10, 120);
		
		if(tanks.size() <= 0){
			for(int i = 0; i < 5; i ++){
				tanks.add(new Tank(50 + (i+1)* 40 , 50, false, Tank.Direction.D, this));
			}
		}
		
		for(int i =0; i < missile.size(); i++){
			
			Missile m = missile.get(i);
			//if(!m.isLive()) missile.remove(m);
			//else
			//m.hitTank(enemyTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitTank(myTank);
			m.hitTanks(tanks);
			m.draw(g);
		}
		
		for(int i = 0;i < explodesions.size(); i++){
			Explodesion e = explodesions.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i < tanks.size(); i++ ){
			Tank t = tanks.get(i);
			t.collideWithWall(w1);
			t.collideWithWall(w2);
			t.collideWithTank(tanks);
			t.draw(g);
		}
		
		
		//enemyTank.draw(g);
		myTank.draw(g); //直接调用Tank的方法
		myTank.eatBloob(b);
//		Color c = g.getColor();
//		g.setColor(Color.RED);
//		g.fillOval(x, y, 30, 30);
//		g.setColor(c);
		
		//y += 5;
		
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
		
	}
	
	//防止闪烁的双缓冲机制
	public void update(Graphics g){
	
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		
		paint(gOffScreen);
		
		g.drawImage(offScreenImage, 0, 0, null);
		
		
	}
	
	/**
	 * 方法显示主窗口
	 */
	public void lauchFrame(){
		//tanks.add(myTank);
		
		
		this.setLocation(200, 200);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.DARK_GRAY);
		this.setVisible(true);
		
		this.addKeyListener(new KeyMonitor()   );
		  
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		TankWarClient tc = new TankWarClient();
		tc.lauchFrame();
	}
	
	//线程画图内部类
	private class PaintThread implements Runnable{
		public void run(){
			while (true){
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//键盘监听内部类
	private class KeyMonitor extends KeyAdapter{
		
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e){
			//直接调用Tank的方法
			myTank.keyPressed(e);
			
//			int key = e.getKeyCode();
//			switch (key){
//			case KeyEvent.VK_LEFT :
//				x -=5;
//				break;
//			case KeyEvent.VK_UP :
//				y -=5;
//				break;
//			case KeyEvent.VK_RIGHT :
//				x += 5;
//				break;
//			case KeyEvent.VK_DOWN :
//				y += 5;
//				break;
//			}
			
		}
	}
	
	
	
	
	
	
	
}
