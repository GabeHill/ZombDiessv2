package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import lib.ConsoleIO;
import objects.Cup;
import objects.Player;
import objects.ZombDie;

//this class was used to test situations and fix things wile keeping the original class intact in case this
//made it worse. the code of this class was then copied to the main Game class for use.
public class GameDEBUG {
	private static Cup c;
	private static String[] storage;
	private static ArrayList<Player> players;
	private static boolean fail = false, done = false;
	private static ArrayList<ZombDie> z, footprint, brain, store;
	private static int l = 0, brains = 0;

	private static void finalScore() {

		Collections.sort(players, new SortByPoints());

		System.out.println(players.get(players.size() - 1).getName() + " wins with "
				+ players.get(players.size() - 1).getPoints() + " brains.");
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
		if (z.get(0) == null) {
			c.replaceDies(z);
			z.add(0, c.draw());
			z.add(0, c.draw());
			z.add(0, c.draw());
		}
		storage = new String[3];
		for (int i = 0; i < storage.length; i++) {
			storage[i] = z.get(i).getRoll();
		}
		for (String s : storage) {
			System.out.print(s + ", ");
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
		if (z.get(0) == null) {
			c.replaceDies(z);
			z.add(0, c.draw());
			z.add(0, c.draw());
			z.add(0, c.draw());
		}
		for (int i = 0; i < storage.length; i++) {
			storage[i] = z.get(i).getRoll();
		}
		for (String s : storage) {
			System.out.print(s + ", ");
		}
		System.out.println();
		if (isShot()) {
			fail = true;
		}
		score();
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
			if (z.get(i) == null) {
				c.replaceDies(z);
			} else {
				storage[i] = z.get(i).getRoll();
				i++;
			}
		}
		for (String s : storage) {
			System.out.print(s + ", ");

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
				if (win() && (l >= players.size())) {
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
	}

	private static void setup() {
		c = new Cup();
		players = new ArrayList<Player>();
		z = new ArrayList<ZombDie>();
		store = new ArrayList<ZombDie>();
		footprint = new ArrayList<ZombDie>();
		brain = new ArrayList<ZombDie>();
		storage = new String[3];

		int num = ConsoleIO.promptForInt("How many players? Min 2, Max 4:", 2, 4);
		String temp;
		for (int i = 0; i < num; i++) {
			players.add(new Player());
			temp = ConsoleIO.promptForInput("Enter player " + (i + 1) + "'s name:", true).trim();
			if ((temp == null) || temp.equals("")) {
				temp = "Player" + (i + 1);
			}
			players.get(i).setName(temp);
		}

	}

	private static void turn() {

		for (int i = 0; i < players.size(); i++) {
			boolean b;
			boolean b1 = false;
			boolean b2 = false;
			brain.removeAll(brain);
			footprint.removeAll(footprint);
			store.removeAll(store);
			brains = 0;
			fail = false;
			b = players.get(i).getPoints() >= 13;
			if (b) {
				l++;
				break;
			} else if ((l < players.size()) && (l < (i + 1))) {

				System.out.println("\n" + players.get(i).getName() + ": " + players.get(i).getPoints() + " brains.");

				printRes();

				if (store.size() >= 3) {
					fail = true;
				} else {
					fail = false;
				}
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
		for (Player p : players) {
			win = p.getPoints() >= 13;
			if (win && !done) {
				done = true;
				System.out.println("One more turn until it's decided!");
				break;
			}
		}

		return win;
	}
}

class SortByPointsDEBUG implements Comparator<Player> {
	@Override
	public int compare(Player a, Player b) {
		return a.getPoints() - b.getPoints();
	}
}
