package Game;

import java.util.ArrayList;
import java.util.Arrays;

public class CongestionGame {

	public static int round;
	public int num_agent;
	public int num_path;
	ArrayList<Agent> agents;
	Path[] paths;
	
	public int[] path_history;
	public double[] path_dist;
	
	public int[] path_instance; //record number of agents on each path for one simulation
	
	public CongestionGame(int n_f_a, int n_e_a, int n_u_a, int n_t_a) {
		round = 0;
		num_agent = n_f_a + n_e_a + n_u_a + n_t_a;
		num_path = 3;
		
		for (int i = 0; i < n_f_a; ++i) {
			agents.add(new Agent(AgentType.FAgent));
		}
		for (int i = 0; i < n_e_a; ++i) {
			agents.add(new Agent(AgentType.EAgent));
		}
		for (int i = 0; i < n_u_a; ++i) {
			agents.add(new Agent(AgentType.UAgent));
		}
		for (int i = 0; i < n_t_a; ++i) {
			agents.add(new Agent(AgentType.TAgent));
		}
		paths[0] = new Path(PathType.TOP);
		paths[1] = new Path(PathType.MID);
		paths[2] = new Path(PathType.BOT);
		path_history = new int[num_path];
		path_dist = new double[num_path];
		path_instance = new int[num_path];
	}
	
	private void normalize() {
		int total = round * num_agent;
		for (int i = 0; i < path_dist.length; ++i) {
			path_dist[i] = (double) path_history[i] / total;
		}
	}
	
	public void simulate() {
		round += 1;
		Arrays.fill(path_instance, 0);
		Path choice;
		for (Agent a : agents) {
			choice = a.choosePath(this);
			a.path_history.add(choice);
			path_history[choice.type.ordinal()] += 1;
			path_instance[choice.type.ordinal()] += 1;
		}
		normalize();
		
		//calculate outcome for each path & assign to agent
		int path_index;
		double reward;
		for (Agent a : agents) {
			choice = a.path_history.get(a.path_history.size() - 1);
			path_index = choice.type.ordinal();
			reward = paths[path_index].totalCost(path_instance[path_index]);
			a.reward_history.add(reward);
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
