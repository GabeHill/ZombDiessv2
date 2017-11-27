package objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.DieColor;

public class ZombDie {

	private List<String> side = new ArrayList<String>(
			Arrays.asList("Shotgun blast", "Brain", "Footprint", "Footprint"));
	private DieColor color;

	public ZombDie(DieColor color) {
		setDif(color);
	}

	public DieColor getColor() {
		return color;
	}

	public String getRoll() {
		int f = (int) (Math.random() * 6);
		return side.get(f);
	}

	private void setDif(DieColor color) {

		if (color == DieColor.GREEN) {
			side.add("Brain");

			side.add("Brain");
			this.color = DieColor.GREEN;
		} else if (color == DieColor.YELLOW) {
			side.add("Brain");
			side.add("Shotgun blast");
			this.color = DieColor.YELLOW;
		} else {
			side.add("Shotgun blast");
			side.add("Shotgun blast");
			this.color = DieColor.RED;
		}
	}
}
