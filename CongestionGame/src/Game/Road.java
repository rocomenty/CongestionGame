package Game;

public class Road {

	double cost;
	double ratio;
	boolean fixed;
	
	
	
	public Road(boolean f, double ratio, double cost) {
		this.fixed = f;
		this.cost = cost;
		this.ratio = ratio;
	}

	double calculateCost(int agent_num) {
		if (fixed) {
			return this.cost;
		} else {
			double dubAgent = (double)(1.0*(agent_num));
			return dubAgent /(this.ratio);
		}
	}
}


/// 10 + n/10
/// n/10 + 10
/// n/10 + n/10

/// 1 + 1
///10 + 1

