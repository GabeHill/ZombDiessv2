package objects;

import java.util.ArrayList;

import enums.DieColor;

public class Cup {

	private ArrayList<ZombDie> diez = new ArrayList<ZombDie>();
	private int draws = 12;

	public Cup() {
		initArray();
	}

	public ZombDie draw() {
		if (draws >= 1) {
			int i = (int) Math.random() * 12;
			ZombDie d = diez.get(i);
			diez.remove(i);
			draws--;
			return d;
		} else {
			return null;
		}
	}

	private void initArray() {
		for (int i = 0; i < 6; i++) {
			diez.add(new ZombDie(DieColor.GREEN));
		}
		for (int i = 0; i < 4; i++) {
			diez.add(new ZombDie(DieColor.YELLOW));
		}
		for (int i = 0; i < 3; i++) {
			diez.add(new ZombDie(DieColor.RED));
		}
	}

	public void replaceDies(ArrayList<ZombDie> d) {
		diez.addAll(d);
		draws += d.size();
	}
}
