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
	
	public int[] path_instance; //record number of agents on each path for one simulation
	
	public double[] aggr_result;
	
	public int[][] agentChoice;
	ArrayList<int[][]> data;
	
	public String[] agent_types = {"FAgent", "EAgent", "UAgent", "DAgent"};
	public int weatherCount = 0;
	
	public CongestionGame(int n_f_a, int n_e_a, int n_u_a, int n_d_a,  double ratio, double cost, boolean has_super) {
		agents = new ArrayList<Agent>();
		if (has_super) {
			paths = new Path[3];
			paths[0] = new Path(PathType.TOP, ratio, cost);
			paths[1] = new Path(PathType.MID, ratio, cost);
			paths[2] = new Path(PathType.BOT, ratio, cost);
			num_path = 3;
		} else {
			paths = new Path[2];
			paths[0] = new Path(PathType.TOP, ratio, cost);
			paths[1] = new Path(PathType.BOT, ratio, cost);
			num_path = 2;
		}
		round = 0;
		num_agent = n_f_a + n_e_a + n_u_a + n_d_a;
		data = new ArrayList<int[][]>();
		for (int i = 0; i < n_f_a; ++i) {
			agents.add(new Agent(AgentType.FAgent, this));
		}
		for (int i = 0; i < n_e_a; ++i) {
			agents.add(new Agent(AgentType.EAgent, this));
		}
		for (int i = 0; i < n_u_a; ++i) {
			agents.add(new Agent(AgentType.UAgent, this));
		}
		for (int i = 0; i < n_d_a; ++i) {
			agents.add(new Agent(AgentType.DAgent, this));
		}
		path_history = new int[num_path];
		path_instance = new int[num_path];
	}
	
	
	public void simulate() {
		double inclementWeather = Math.random();
		agentChoice = new int[4][num_path];
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
			if (num_path == 2) {
				if (choice.type == PathType.BOT) {
					path_instance[1] += 1;
					agentChoice[a.type.ordinal()][1] ++;
				} else {
					path_instance[0] += 1;
					agentChoice[a.type.ordinal()][0] ++;
				}
			} else {
				path_instance[choice.type.ordinal()] += 1;
				agentChoice[a.type.ordinal()][choice.type.ordinal()] ++;
			}
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
//		if(inclementWeather <= 0.15) {
//			weatherCount ++;
//			
//			if(inclementWeather <= 0.05) {
//				System.out.println("weather on top");
//				unforseenCircumstances[0] = 1.5;
//			}
//			else if (inclementWeather > 0.1) {
//				System.out.println("weather on mid");
//				unforseenCircumstances[1] = 1.5;
//			} else {
//				System.out.println("weather on bot");
//				unforseenCircumstances[2] = 1.5;
//			}
//		}
		
		for (Agent a : agents) {
			choice = a.path_history.get(a.path_history.size() - 1);
			if (num_path == 2) {
				if (choice.type == PathType.BOT) {
					path_index = 1;
				} else {
					path_index = 0;
				}
			} else {
				path_index = choice.type.ordinal();
			}
			
			if (a.type == AgentType.FAgent) {
				a.recordOthers(path_instance);
			}
			
			reward = ((paths[path_index].totalCost(path_instance[path_index]))*(unforseenCircumstances[path_index]));
			
//			if(inclementWeather <= 0.05) {
//				if(path_index == 0) {
//					System.out.println("Weather reward top: " + reward);
//				}
//			}
			
			a.reward_history.add(reward);
			if (num_path == 2) {
				if (choice.type == PathType.BOT) {
					path_history[1] += 1;
				} else {
					path_history[0] += 1;
				}
			} else {
				path_history[choice.type.ordinal()] += 1;
			}
		}
		data.add(agentChoice);
		System.out.println("Round: " + round);
		for (int i = 0; i < 4; ++i) {
			System.out.println("Agent: " + i + " choices: ");
			for (int j = 0; j < num_path; ++j) {
				System.out.print(agentChoice[i][j] + ", ");
			}
			System.out.println();
		}
		System.out.println();
//		System.out.println("Top path: " + path_instance[0]);
//		System.out.println("Mid path: " + path_instance[1]);
//		System.out.println("Bot path: " + path_instance[2]);
//		System.out.println();
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
//		System.out.println("weather count: " + weatherCount);
//		for (int r = 0; r < data.size(); r++) {
//			int [][] a = data.get(r);
//			System.out.println("Round: " + r);
//			for (int i = 0; i < 4; ++i) {
//				System.out.println("Agent type: " + agent_types[i]);
//				for (int j = 0; j < num_path; ++j) {
//					System.out.println("Path: " + j + ", Num: " + agentChoice[i][j]);
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
		
		double[] costs = new double[4];
		double[] counts = new double[4];
		
		for (int i = 0; i < agents.size(); ++i) {
			Agent a = agents.get(i);
			switch (a.type) {
			case FAgent:
				costs[0] += a.calculateAvgCost();
				counts[0] += 1;
				break;
				
			case EAgent:
				costs[1] += a.calculateAvgCost();
				counts[1] += 1;
				break;
				
			case UAgent:
				costs[2] += a.calculateAvgCost();
				counts[2] += 1;
				break;
				
			case DAgent:
				costs[3] += a.calculateAvgCost();
				counts[3] += 1;
				break;
			}
		}
		
		for (int i = 0; i < costs.length; i++) {
			if (counts[i] != 0) {
				costs[i] /= counts[i];
			}
		}
		
		System.out.println("FAgent: " + costs[0] + "; EAgent: " + costs[1] + "; UAgent: " + costs[2] + "; DAgent: " + costs[3]);
		
//		double cost = 0;
//		double count = 0;
//		for (int i = 0; i < agents.size(); ++i) {
//			if (agents.get(i).type == AgentType.EAgent) {
//				cost += agents.get(i).calculateAvgCost();
//				count ++;
//			}
//		}
//		cost = cost / count;
//		System.out.println("EAgent avg: " + cost);
		
	}
	
	public static void main(String[] args) {
		//CongestionGame game = new CongestionGame(FA,EA,UBA,DA,n/ratio,fixed);
		CongestionGame game = new CongestionGame(40,0,0,0,1,45, true);
		game.simulator(5);
	}
	

}
