package Game;

public class Road {

	double cost;
	boolean fixed;
	
	public Road(boolean f) {
		this.fixed = f;
	}
	
	double calculateCost(int agent_num) {
		if (fixed) {
			return 45;
		} else {
			return agent_num / 100.0;
		}
	}
}
