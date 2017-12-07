package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import lib.ConsoleIO;
import objects.Cup;
import objects.Player;
import objects.ZombDie;

public class Game2Player {
	private static Cup c;
	private static String[] storage;
	private static ArrayList<Player> players;
	private static boolean fail = false, done = false;
	private static ArrayList<ZombDie> z, footprint, brain, store;
	private static int brains = 0, first = -1, dieCounter = 13;

	private static void finalScore() {
		Collections.sort(players, new SortByPoints2());
		Player winner = players.get(players.size() - 1);
		System.out.println(winner.getName() + " wins with " + winner.getPoints() + " brains.");
	}

	private static boolean getFootprints() {
		boolean t = false;
		for (String element : storage) {
			t = element.equalsIgnoreCase("Footprint");
			if (t) {
				break;
			}

		}

		return t;
	}

	private static boolean getReroll() {
		return ConsoleIO.promptForBool("Do you want to reroll all footprints? Yes or No:", "yes", "no");

	}

	private static boolean isShot() {
		for (String s : storage) {
			if (s.equalsIgnoreCase("Shotgun blast")) {
			}
		}
		return store.size() >= 3;

	}

	private static void printReRes() {

		z.add(0, c.draw());
		z.add(0, c.draw());
		z.add(0, c.draw());
		refill();
		storage = new String[3];
		for (int i = 0; i < storage.length; i++) {
			storage[i] = z.get(i).getRoll();
			dieCounter--;
			System.out.print(z.get(i).getColor() + " " + storage[i] + ", ");
		}
		System.out.println();
		if (isShot()) {
			fail = true;
		}
		score();
	}

	private static void printRes() {
		z = new ArrayList<ZombDie>();
		z.add(0, c.draw());
		z.add(0, c.draw());
		z.add(0, c.draw());
		refill();
		storage = new String[3];

		for (int i = 0; i < storage.length; i++) {
			storage[i] = z.get(i).getRoll();
			dieCounter--;
			System.out.print(z.get(i).getColor() + " " + storage[i] + ", ");
		}
		System.out.println();
		if (isShot()) {
			fail = true;
		}
		score();
	}

	private static void refill() {
		if (dieCounter <= 2) {
			c.replaceDies(brain);
			brain.remove(brains);
		}
	}

	private static boolean reroll() {
		return ConsoleIO.promptForBool("Would you like to roll again? Yes or No:", "yes", "no");
	}

	private static void rollFoot() {
		int i;
		for (i = 0; i < (footprint.size() - 1); i++) {
			storage[i] = footprint.get(i).getRoll();
		}
		while (i < 2) {
			z.add(i, c.draw());
			refill();
			dieCounter--;
			if (z.get(i) == null) {
				c.replaceDies(z);
			} else {
				storage[i] = z.get(i).getRoll();

				i++;
			}
		}
		for (i = 0; i < storage.length; i++) {

			System.out.print(z.get(i).getColor() + " " + storage[i] + ", ");

		}

		footprint.removeAll(footprint);
		System.out.println();
		score();
	}

	public static void run() {
		boolean play = true;
		boolean again;
		do {
			setup();
			do {
				turn();
				if (win()) {
					turn();
					finalScore();
					play = false;
				}
			} while (play);
			again = ConsoleIO.promptForBool("Would you like to play again? Yes or No: ", "yes", "no");

		} while (again);
		System.out.println("Thanks for playing!");
	}

	private static void score() {

		for (int i = 0; i < storage.length; i++) {

			if (storage[i].equalsIgnoreCase("brain")) {
				brains++;
				brain.add(z.get(i));
			} else if (storage[i].equalsIgnoreCase("shotgun Blast")) {
				// shotgun++;
				store.add(z.get(i));
			} else if (storage[i].equalsIgnoreCase("footprint")) {
				footprint.add(0, z.get(i));
			}
		}
		if (isShot()) {
			brains = 0;
			brain.removeAll(brain);
			fail = true;
		}
		System.out.println("Shots fired: " + store.size() + ", Survivors cornered: " + brains);

	}

	private static void setup() {
		c = new Cup();
		players = new ArrayList<Player>();
		z = new ArrayList<ZombDie>();
		store = new ArrayList<ZombDie>();
		footprint = new ArrayList<ZombDie>();
		brain = new ArrayList<ZombDie>();
		storage = new String[3];

		String temp;
		for (int i = 0; i < 2; i++) {
			players.add(new Player());
			temp = ConsoleIO.promptForInput("Enter player " + (i + 1) + "'s name:", true).trim();
			if ((temp == null) || temp.equals("")) {
				temp = "Player" + (i + 1);
			}
			players.get(i).setName(temp);
		}

	}

	private static void turn() {

		for (int i = 0; (i < players.size()) && (i != first); i++) {
			dieCounter = 13;
			boolean b = players.get(i).getPoints() >= 13;
			boolean b1 = false;
			boolean b2 = false;
			brain.removeAll(brain);
			footprint.removeAll(footprint);
			store.removeAll(store);
			brains = 0;
			fail = false;
			if (b) {
				break;
			} else {
				// if ((l < players.size()) && (l < (i)))
				System.out.println("\n" + players.get(i).getName() + ": " + players.get(i).getPoints() + " brains.");

				printRes();

				fail = store.size() >= 3;

				while (!fail) {
					b1 = false;
					b2 = false;

					if (getFootprints()) {
						b1 = getReroll();
					} else {
						b2 = reroll();
					}

					if (b1 && !fail) {
						rollFoot();
					} else if (b2 && !fail) {
						printReRes();
					} else {
						break;
					}
				}

				fail = false;
				players.get(i).addPoints(brains);
				c.replaceDies(z);

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("Oops! Something broke. Sorry.");
					e.printStackTrace();
				}
			}

		}
	}

	private static boolean win() {
		boolean win = false;
		for (int i = 0; i < players.size(); i++) {
			win = players.get(i).getPoints() >= 13;
			if (win && !done) {
				first = i;
				done = true;
				System.out.println("One more turn until it's decided!");

				break;
			}
		}
		return win;
	}
}

class SortByPoints2 implements Comparator<Player> {
	@Override
	public int compare(Player a, Player b) {
		return a.getPoints() - b.getPoints();
	}
}
