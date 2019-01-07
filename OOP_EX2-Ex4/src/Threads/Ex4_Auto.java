package Threads;

import java.util.ArrayList;
import java.util.Iterator;

import Algorithms.ConnectedGraph;
import Algorithms.DijkstraAlgo;
import GIS.Game;
import GIS.Node;
import GUI.GuiEx4;
import Geom.Point3D;
import Robot.Play;

public class Ex4_Auto extends Thread{
	GuiEx4 gui;
	boolean play=true;
	DijkstraAlgo D;
	ConnectedGraph C;
	public Ex4_Auto(GuiEx4 guiex4) {
		this.gui= guiex4;
		}
		public void run() {
			while(play) {
			// 1) Create a "play" from a file (attached to Ex4)
			Play play1 = gui.GamePlayer;
			
			// 2) Set your ID's - of all the group members
			play1.setIDs(1111,2222,3333);
			
			// 3)Get the GPS coordinates of the "arena"
			String map_data = play1.getBoundingBox();
//			System.out.println("Bounding Box info: "+map_data);
			
			// 4) get the game-board data
			ArrayList<String> board_data = play1.getBoard();
			for(int i=0;i<board_data.size();i++) {
				System.out.println(board_data.get(i));
			}
			System.out.println();
//			System.out.println("Init Player Location should be set using the bounding box info");
			
			// 5) Set the "player" init location - should be a valid location
//			play1.setInitLocation(32.1040,35.2061);
			
			// 6) Start the "server"
			play1.start(); // default max time is 100 seconds (1000*100 ms).
			
			// 7) "Play" as long as there are "fruits" and time
//			for(int i=0;i<10;i++) {
			int i=0;
				while(play1.isRuning()) {
					try {
					ConnectedGraph  C = new ConnectedGraph(gui.game);
					D=new DijkstraAlgo(gui.game,C);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					i++;
			// 7.1) this is the main command to the player (on the server side)	
				System.out.println("***** "+i+"******");
				rotate_null(); 
			// 7.2) get the current score of the game
				String info = play1.getStatistics();
//				System.out.println(info);
			// 7.3) get the game-board current state
				board_data = play1.getBoard();
				gui.game=new Game(board_data);
//				D= new DijkstraAlgo(gui.game);
				try {
				rotate(); 
				}
				catch (Exception e) {
//					rotate_null();
					System.out.println("error!!!");
					
				}
				gui.repaint();
				try {
				sleep(100);
				}
				catch (Exception e) {
				}
//				for(int a=0;a<board_data.size();a++) {
//					System.out.println(board_data.get(a));
//				}
				System.out.println();
			}
			// 8) stop the server - not needed in the real implementation.
			//play1.stop();
//			System.out.println("**** Done Game (user stop) ****");
			
			// 9) print the data & save to the course DB
			String info = play1.getStatistics();
			System.out.println(info);
			play=false;
			}
		}
		
		private void rotate() {
			String vertex="";
			for(String s:D.path) {
				System.out.print(s+", ");
			}
			if(D.path.size()>=2) {
			vertex=D.path.get(1);
			System.out.println(D);
			Node n=null;
			Iterator<Node>vertexes=C.vertexes.iterator();
			while(vertexes.hasNext()) {
				Node n0=vertexes.next();
				if(vertex.equals(n0._name)) {
					n=n0;
				}	
			}
			gui.rotate((int)(n.inPixel.x()),(int)(n.inPixel.y()));
//			Point3D p1=gui.player.get_p();
//			if(n._point.x()==p1.x()&&n._point.y()==p1.x()) {
//				D.path.remove(1);
//			}
			}
			else {
				try {
				Point3D p=D.fruit.get_p();
				System.out.println(p.x()+","+p.y());
				int arr[]=gui.map.gpsToPixel(p.x(),p.y());
				System.out.println(arr[0]+","+arr[1]);
				gui.rotate(arr[0],arr[1]);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			double degree= gui.azimute;
			gui.GamePlayer.rotate(degree);
			}
		
		private void rotate_null() {
			double degree= gui.azimute;
			gui.GamePlayer.rotate(degree);
		}
		}