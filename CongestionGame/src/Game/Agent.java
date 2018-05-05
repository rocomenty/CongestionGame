package Game;

import java.util.*;

public class Agent {
	public AgentType type;

	public ArrayList<Path> path_history;
	public ArrayList<Double> reward_history;
	public Agent(AgentType t) {
		this.type = t;
		path_history = new ArrayList<Path>();
		reward_history = new ArrayList<Double>();
	}

	public Path choosePath(CongestionGame g) {
		double cost;
		int best_route;
		switch (type) {
		case FAgent:{
			double [] costs = new double[3];
			costs[0] = 0;
			costs[1] = 0;
			costs[2] = 0;

			int agent_num_top = (int)(g.path_dist[0]*g.num_agent);
			costs[0] = g.paths[0].totalCost(agent_num_top);


			int agent_num_mid = (int)(g.path_dist[1] * g.num_agent);
			costs[1] = g.paths[1].totalCost(agent_num_mid);

			int agent_num_bot = (int)(g.path_dist[2] * g.num_agent);
			costs[2] = g.paths[2].totalCost(agent_num_bot);

			

			ArrayList<Integer> mins = new ArrayList<Integer>();
			cost = costs[0];
			best_route = 0;
			mins.add(0);
			for (int i = 1; i < costs.length; ++i) {

				if (costs[i] < cost) {
					mins.clear();
					mins.add(i);
					cost = costs[i];
					best_route = i;
				} 
				else if (costs[i] == cost){
					mins.add(i);
				}
			}
			if(mins.size() > 1) {
				int rando = (int)(Math.random()*mins.size());
				best_route = mins.get(rando);
			} 
			return g.paths[best_route];
		}
		case EAgent:{ //epsilon value determined here
			double dice = Math.random();
			if (dice < 0.2) {
				return g.paths[(int)(Math.random() * 3)];
			} else {
				double[] avg = new double[3];
				int[] counts = new int[3];
				for (int i = 0; i < path_history.size(); ++i) {
					Path p = path_history.get(i);
					switch (p.type) {
					case TOP:
						counts[0] ++;
						avg[0] = (avg[0] + reward_history.get(i))/2;
						break;
					case MID:
						counts[1] ++;
						avg[1] = (avg[1] + reward_history.get(i))/2;
						break;
					case BOT:
						counts[2] ++;
						avg[2] = (avg[2] + reward_history.get(i))/2;
						break;
					}
				}
				ArrayList<Integer> mins = new ArrayList<Integer>();
				mins.add(0);
				cost = avg[0];
				best_route = 0;
				for (int i = 1; i < avg.length; ++i) {
					if ((avg[i])< cost) {
						mins.clear();
						mins.add(i);
						cost = avg[i];
						best_route = i;
					}
					else if(avg[i] == cost) {
						mins.add(i);
					}
				}
				if(mins.size() > 1) {
					int rando = (int)(Math.random()*mins.size());
					best_route = mins.get(rando);
				} 
				return g.paths[best_route];
			}}
		case UAgent:{
			double[] avg = new double[3];
			int[] counts = new int[3];
			avg[0] = 0;
			avg[1] = 0;
			avg[2] = 0;
			counts[0] = 0;
			counts[1] = 0;
			counts[2] = 0;

			for (int i = 0; i < path_history.size(); ++i) {
				Path p = path_history.get(i);
				switch (p.type) {
				case TOP:
					counts[0] ++;
					//avg[0] += reward_history.get(i);
					avg[0] = (avg[0] + reward_history.get(i))/2;
					break;
				case MID:
					counts[1] ++;
					//avg[1] += reward_history.get(i);
					avg[1] = (avg[1] + reward_history.get(i))/2;
					break;
				case BOT:
					counts[2] ++;
					//avg[2] += reward_history.get(i);
					avg[2] = (avg[2] + reward_history.get(i))/2;
					break;
				}
			}

			int total_move = path_history.size();
			if(total_move == 0) {
				int rando = (int)(Math.random()*3);
				return g.paths[rando];
			}
			//			double min = avg[0] / counts[0] + Math.sqrt(2*Math.log(total_move) / counts[0]);
			double min = avg[0] + Math.sqrt(2*Math.log(total_move) / counts[0]);

			best_route = 0;
			double valuation;
			ArrayList<Integer> mins = new ArrayList<Integer>();
			mins.add(0);

			for (int i = 1; i < avg.length; ++i) {
				//valuation = avg[i] / counts[i] + Math.sqrt(2*Math.log(total_move) / counts[i]);
				if(counts[i] != 0) {
					valuation = avg[i] + Math.sqrt(2*Math.log(total_move) / counts[i]);
				} else {
					valuation = 0.0;
				}

				if (valuation < min) {
					mins.clear();
					mins.add(i);
					min = valuation;
					best_route = i;
				} 
				else if(valuation == min) {
					mins.add(i);
				}
			}
			if(mins.size() > 1) {
				int rando = (int)(Math.random()*mins.size());
				best_route = mins.get(rando);
			} 
			return g.paths[best_route];
		}
		case DAgent:{
			double [] costs = new double[3];
			double dice = Math.random();
			double[] avg = new double[3];
			int[] counts = new int[3];
			counts[0] = 0;
			counts[1] = 0;
			counts[2] = 0;
			for (int i = 0; i < path_history.size(); ++i) {
				Path p = path_history.get(i);
				switch (p.type) {
				case TOP:
					counts[0] ++;
					avg[0] = (avg[0] + reward_history.get(i))/2;
					break;
				case MID:
					counts[1] ++;
					avg[1] = (avg[1] + reward_history.get(i))/2;
					break;
				case BOT:
					counts[2] ++;
					avg[2] = (avg[2] + reward_history.get(i))/2;
					break;
				}
			}
			if (dice < 0.357) {
				//It's a sad day (or driving to work!)
				ArrayList<Integer> maxes = new ArrayList<Integer>();
				cost = costs[0];
				best_route = 0;
				maxes.add(0);
				for (int i = 1; i < costs.length; ++i) {
					if (costs[i] > cost) {
						maxes.clear();
						maxes.add(i);
						cost = costs[i];
						best_route = i;
					} 
					else if (costs[i] == cost){
						maxes.add(i);
					}
				}
				if(maxes.size() > 1) {
					int rando = (int)(Math.random()*maxes.size());
					best_route = maxes.get(rando);
				}
				return g.paths[best_route];
			} else {
				ArrayList<Integer> mins = new ArrayList<Integer>();
				mins.add(0);
				cost = avg[0];
				best_route = 0;
				for (int i = 1; i < avg.length; ++i) {
					if ((avg[i])< cost) {
						mins.clear();
						mins.add(i);
						cost = avg[i];
						best_route = i;
					}
					else if(avg[i] == cost) {
						mins.add(i);
					}
				}
				if(mins.size() > 1) {
					int rando = (int)(Math.random()*mins.size());
					best_route = mins.get(rando);
				} 
			}
			return g.paths[best_route];
		}
		default:
			return null;
		}
	}

}
