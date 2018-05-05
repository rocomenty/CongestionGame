package Game;

import java.util.*;
import java.util.Arrays;
import java.io.*;

public class CongestionGame {

	public static int round;
	public int num_agent;
	public int num_path;
	ArrayList<Agent> agents;
	Path[] paths;
	
	public int[] path_history;
	public double[] path_dist;
	
	public int[] path_instance; //record number of agents on each path for one simulation
	
	public double[] aggr_result;
	
	public int[][] agentChoice;
	ArrayList<int[][]> data;
	
	public String[] agent_types = {"FAgent", "EAgent", "UAgent", "DAgent"};
	public int weatherCount = 0;
	
	public CongestionGame(int n_f_a, int n_e_a, int n_u_a, int n_d_a,  double ratio, double cost) {
		agents = new ArrayList<Agent>();
		paths = new Path[3];
		round = 0;
		num_agent = n_f_a + n_e_a + n_u_a + n_d_a;
		num_path = 3;
		data = new ArrayList<int[][]>();
		for (int i = 0; i < n_f_a; ++i) {
			agents.add(new Agent(AgentType.FAgent));
		}
		for (int i = 0; i < n_e_a; ++i) {
			agents.add(new Agent(AgentType.EAgent));
		}
		for (int i = 0; i < n_u_a; ++i) {
			agents.add(new Agent(AgentType.UAgent));
		}
		for (int i = 0; i < n_d_a; ++i) {
			agents.add(new Agent(AgentType.DAgent));
		}
		paths[0] = new Path(PathType.TOP, ratio, cost);
		paths[1] = new Path(PathType.MID, ratio, cost);
		paths[2] = new Path(PathType.BOT, ratio, cost);
		path_history = new int[num_path];
		path_dist = new double[num_path];
		path_instance = new int[num_path];
	}
	
	private void normalize() {
		int total = (round * num_agent);
		double dubTot = (double)(1.0*(total));
		for (int i = 0; i < path_dist.length; ++i) {
			path_dist[i] = ((double)(path_history[i]))/dubTot;
		}
	}
	
	public void simulate() {
		double inclementWeather = Math.random();
		agentChoice = new int[4][3];
		round += 1;
		Arrays.fill(path_instance, 0);
		Path choice;
		
		for (Agent a : agents) {
			choice = a.choosePath(this);
			try{
				a.path_history.add(choice);
			} catch(NullPointerException e) {
				System.out.println("round:" + round);
				System.out.println("agent:" + a + "'s history: " + a.path_history);
				
			}
			path_instance[choice.type.ordinal()] += 1;
			agentChoice[a.type.ordinal()][choice.type.ordinal()] ++;
//			switch(a.type){
//				case FAgent:
//					agentChoice[a.type.ordinal()][choice.type.ordinal()] ++;
//				break;
//				case UAgent:
//				break;
//				case DAgent:
//				break;
//				case EAgent:
//				break;
//				//path 0 - 300FA, 200DA
//				//path 1
//				//path 2
//				
//			}
		}
		
		
		//calculate outcome for each path & assign to agent
		int path_index;
		double reward;
		
		double[] unforseenCircumstances = new double[3];
		unforseenCircumstances[0] = 1.0;
		unforseenCircumstances[1] = 1.0;
		unforseenCircumstances[2] = 1.0;
		if(inclementWeather <= 0.15) {
			weatherCount ++;
			
			if(inclementWeather <= 0.05) {
				System.out.println("weather on top");
				unforseenCircumstances[0] = 1.5;
			}
			else if (inclementWeather > 0.1) {
				System.out.println("weather on mid");
				unforseenCircumstances[1] = 1.5;
			} else {
				System.out.println("weather on bot");
				unforseenCircumstances[2] = 1.5;
			}
		}
		
		for (Agent a : agents) {
			choice = a.path_history.get(a.path_history.size() - 1);
			path_index = choice.type.ordinal();
			
			reward = ((paths[path_index].totalCost(path_instance[path_index]))*(unforseenCircumstances[path_index]));
			
//			if(inclementWeather <= 0.05) {
//				if(path_index == 0) {
//					System.out.println("Weather reward top: " + reward);
//				}
//			}
			
			a.reward_history.add(reward);
			path_history[choice.type.ordinal()] += 1;
		}
		normalize();
		data.add(agentChoice);
		System.out.println("Top path: " + path_instance[0]);
		System.out.println("Mid path: " + path_instance[1]);
		System.out.println("Bot path: " + path_instance[2]);
		System.out.println();
//		for(int i = 0; i < 4; i++) {
//			for(int j = 0; j < 3; j++) {
//				System.out.print(" " + agentChoice[i][j]);
//			}
//			System.out.println();
//		}
		
		
		
	}
	
	public void simulator(int runs) {
		for(int i = 0; i < runs; i++) {
			this.simulate();
		}
		System.out.println("weather count: " + weatherCount);
//		for (int r = 0; r < data.size(); r++) {
//			int [][] a = data.get(r);
//			System.out.println("Round: " + r);
//			for (int i = 0; i < a.length; ++i) {
//				System.out.println("Agent type: " + agent_types[i]);
//				for (int j = 0; j < a[i].length; ++j) {
//					System.out.println("Path: " + j + ", Num: " + agentChoice[i][j]);
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
		
	}
	
	public static void main(String[] args) {
		//CongestionGame game = new CongestionGame(FA,EA,UBA,DA,n/ratio,fixed);
		CongestionGame game = new CongestionGame(400,0,0,0,10,4.5);
		game.simulator(20);
	}
	

}
