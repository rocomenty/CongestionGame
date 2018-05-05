package Game;

import java.util.ArrayList;

public class Path {

	ArrayList<Road> roads;
	PathType type;
	
	public Path(PathType t, double ratio, double cost) {
		roads = new ArrayList<Road>();
		type = t;
		if (t == PathType.MID) { //mid path is two roads with variable cost
			roads.add(new Road(false, ratio, cost));
			roads.add(new Road(false, ratio, cost));
		} else { //top & bot path is two roads with one variable and one fixed cost
			roads.add(new Road(false, ratio, cost));
			roads.add(new Road(true, ratio, cost));
		}
	}
	
	public void addRoad(Road r) {
		roads.add(r);
	}
	
	public double totalCost(int agent_num) {
		double total = 0;
		for (Road r : roads) {
			total += r.calculateCost(agent_num);
		}
		return total;
	}
	
	
}
