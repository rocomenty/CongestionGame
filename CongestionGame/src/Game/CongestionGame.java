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
	
	public double[] aggr_result;
	
	public CongestionGame(int n_f_a, int n_e_a, int n_u_a, int n_t_a) {
		agents = new ArrayList<Agent>();
		paths = new Path[3];
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
		int counter = 0;
		for (Agent a : agents) {
			counter++;
			System.out.print("How many agents added: " + counter + " of " + agents.size());
			System.out.println("hello: " + a + " ");
			choice = a.choosePath(this);
			System.out.print("Agent: " + a + " ");
			System.out.println("choice:" + choice);
			
			try{
				System.out.println("choice type:" + choice.type);
			} catch(NullPointerException e) {
				System.out.println("round:" + round);
				System.out.println("agent:" + a + "'s history: " + a.path_history);
				
			}
			System.out.println("choice ordinal:" + choice.type.ordinal());
			

			a.path_history.add(choice);

			
			path_history[choice.type.ordinal()] += 1;
			System.out.println("history " + a + " ");
			path_instance[choice.type.ordinal()] += 1;
			System.out.println("instance " + a + " ");
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
			System.out.println("hello");
		}
		
		
		
	}
	
	public static void main(String[] args) {
		CongestionGame game = new CongestionGame(2,2,2,2);
		game.simulate();
	}

}
