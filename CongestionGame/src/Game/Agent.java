package Game;

import java.util.*;

public class Agent {
	public AgentType type;
	public ArrayList<Path> path_history;
	public ArrayList<Double> reward_history;
	public int[] other_history;
	private double[] dist;
	private double epsilon;
	public Agent(AgentType t, CongestionGame g) {
		this.type = t;
		path_history = new ArrayList<Path>();
		reward_history = new ArrayList<Double>();
		other_history = new int[g.num_path];
		dist = new double[g.num_path];
		Arrays.fill(other_history, 0);
		Arrays.fill(dist, 0);
	}
	
	public double calculateAvgCost() {
		double total = 0;
		for (int i = 0; i < reward_history.size(); ++i) {
			total += reward_history.get(i);
		}
		return total / (double) (reward_history.size());
	}
	
	public void recordOthers(int[] path_instance) {
		for (int i = 0; i < path_instance.length; ++i) {
			other_history[i] += path_instance[i];
		}
		Path last_choice = path_history.get(path_history.size() - 1);
		if (other_history.length == 2) {
			if (last_choice.type == PathType.BOT) {
				other_history[1] -= 1;
			} else {
				other_history[0] -= 1;
			}
		} else {
			other_history[last_choice.type.ordinal()] -= 1;
		}
		
		this.normalize();
	}
	
	private void normalize() {
		int total = 0;
		for (int i = 0; i < other_history.length; ++i) {
			total += other_history[i];
		}
		//System.out.print("Distribution: ");
		for (int i = 0; i < dist.length; ++i) {
			dist[i] = (double)other_history[i] / (double)total;
			//System.out.print(dist[i] + ", ");
		}
		//System.out.println();
	}

	public Path choosePath(CongestionGame g) {
		double cost;
		int best_route;
		switch (type) {
		case FAgent:{
			double [] costs = new double[g.num_path];
			for (int i = 0; i < g.num_path; ++i) {
				costs[i] = 0;
			}
			for (int i = 0; i < costs.length; ++i) {
				int agent_num = (int)(dist[i]*g.num_agent);
				costs[i] = g.paths[i].totalCost(agent_num);
			}
//			int agent_num_top = (int)(g.path_dist[0]*g.num_agent);
//			costs[0] = g.paths[0].totalCost(agent_num_top);
//
//			int agent_num_mid = (int)(g.path_dist[1] * g.num_agent);
//			costs[1] = g.paths[1].totalCost(agent_num_mid);
//
//			int agent_num_bot = (int)(g.path_dist[2] * g.num_agent);
//			costs[2] = g.paths[2].totalCost(agent_num_bot);

			

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
//			System.out.println("round: " + g.round + " chose: " + best_route);
			return g.paths[best_route];
		}
		case EAgent:{ //epsilon value determined here
			double dice = Math.random();
			if (dice < g.epsilon) {
				return g.paths[(int)(Math.random() * g.num_path)];
			} else {
				double[] avg = new double[g.num_path];
				int[] counts = new int[g.num_path];
				for (int i = 0; i < avg.length; ++i) {
					avg[i] = 0;
					counts[i] = 0;
				}
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
						if (g.num_path > 2) {
							counts[2] ++;
							avg[2] = (avg[2] + reward_history.get(i))/2;
						} else {
							counts[1] ++;
							avg[1] = (avg[1] + reward_history.get(i))/2;
						}
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
			double[] avg = new double[g.num_path];
			int[] counts = new int[g.num_path];
			
			for (int i = 0; i < avg.length; ++i) {
				avg[i] = 0;
				counts[i] = 0;
			}

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
					if (g.num_path > 2) {
						counts[2] ++;
						//avg[2] += reward_history.get(i);
						avg[2] = (avg[2] + reward_history.get(i))/2;
					} else {
						counts[1] ++;
						//avg[2] += reward_history.get(i);
						avg[1] = (avg[1] + reward_history.get(i))/2;
					}

					break;
				}
			}

			int total_move = path_history.size();
			if(total_move == 0) {
				int rando = (int)(Math.random()*g.num_path);
				return g.paths[rando];
			}
			//			double min = avg[0] / counts[0] + Math.sqrt(2*Math.log(total_move) / counts[0]);
			double min = 0;
			if (counts[0] == 0) {
				min = 0;
			} else {
				min = avg[0] + Math.sqrt(2*Math.log(total_move) / counts[0]);
			}

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
			double [] costs = new double[g.num_path];
			double dice = Math.random();
			double[] avg = new double[g.num_path];
			int[] counts = new int[g.num_path];
			
			for (int i = 0; i < avg.length; ++i) {
				avg[i] = 0;
				counts[i] = 0;
				costs[i] = 0;
			}
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
					if (g.num_path > 2) {
						counts[2] ++;
						avg[2] = (avg[2] + reward_history.get(i))/2;
					} else {
						counts[1] ++;
						avg[1] = (avg[1] + reward_history.get(i))/2;
					}
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
