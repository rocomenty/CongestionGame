package Game;

import java.util.ArrayList;

public class Path {

	ArrayList<Road> roads;
	PathType type;
	
	public Path(PathType t) {
		roads = new ArrayList<Road>();
		if (t == PathType.MID) { //mid path is two roads with variable cost
			roads.add(new Road(false));
			roads.add(new Road(false));
		} else { //top & bot path is two roads with one variable and one fixed cost
			roads.add(new Road(false));
			roads.add(new Road(true));
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
