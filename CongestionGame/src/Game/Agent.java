package Game;

import java.util.ArrayList;

public class Agent {
	private AgentType type;
	
	public ArrayList<Path> path_history;
	public ArrayList<Double> reward_history;
	public Agent(AgentType t) {
		this.type = t;
	}
	
	public Path choosePath(CongestionGame g) {
		double cost;
		int best_route;
		switch (type) {
		case FAgent:
			double [] costs = new double[3];
			int agent_num_top = (int) g.path_dist[0] * g.num_agent;
			costs[0] = g.paths[0].totalCost(agent_num_top);
			int agent_num_mid = (int) g.path_dist[1] * g.num_agent;
			costs[1] = g.paths[1].totalCost(agent_num_mid);
			int agent_num_bot = (int) g.path_dist[2] * g.num_agent;
			costs[2] = g.paths[2].totalCost(agent_num_bot);
			cost = costs[0];
			best_route = 0;
			for (int i = 1; i < costs.length; ++i) {
				if (costs[i] < cost) {
					cost = costs[i];
					best_route = i;
				}
			}
			return g.paths[best_route];
		case EAgent: //epsilon value determined here
			double dice = Math.random();
			if (dice < 0.2) {
				return g.paths[(int)(Math.random() * 2)];
			} else {
				double[] avg = new double[3];
				int[] counts = new int[3];
				for (int i = 0; i < path_history.size(); ++i) {
					Path p = path_history.get(i);
					switch (p.type) {
					case TOP:
						counts[0] ++;
						avg[0] += reward_history.get(i);
						break;
					case MID:
						counts[1] ++;
						avg[1] += reward_history.get(i);
						break;
					case BOT:
						counts[2] ++;
						avg[2] += reward_history.get(i);
						break;
					}
				}
				cost = avg[0] / counts[0];
				best_route = 0;
				for (int i = 1; i < avg.length; ++i) {
					if ((avg[i] / counts[i] )< cost) {
						cost = avg[i] / counts[i];
						best_route = i;
					}
				}
				return g.paths[best_route];
			}
		case UAgent:
			double[] avg = new double[3];
			int[] counts = new int[3];
			for (int i = 0; i < path_history.size(); ++i) {
				Path p = path_history.get(i);
				switch (p.type) {
				case TOP:
					counts[0] ++;
					avg[0] += reward_history.get(i);
					break;
				case MID:
					counts[1] ++;
					avg[1] += reward_history.get(i);
					break;
				case BOT:
					counts[2] ++;
					avg[2] += reward_history.get(i);
					break;
				}
			}
			int total_move = path_history.size();
			double min = avg[0] / counts[0] + Math.sqrt(2*Math.log(total_move) / counts[0]);
			best_route = 0;
			double valuation;
			for (int i = 1; i < avg.length; ++i) {
				valuation = avg[i] / counts[i] + Math.sqrt(2*Math.log(total_move) / counts[i]);
				if (valuation < min) {
					min = valuation;
					best_route = i;
				}
			}
			return g.paths[best_route];
		default:
			return null;
		}
	}
	
}
