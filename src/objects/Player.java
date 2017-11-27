package objects;

public class Player {
	private String name;
	private int points = 0;

	public Player() {

	}

	public void addPoints(int point) {
		points += point;
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public void setName(String name) {
		this.name = name;
	}
}
