package main;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

public class BejeweledModel {

	private static final int SQUARES = 8;
	private static final int SQUARE_SIZE = 40;
	private static final int START_X = 25;
	private static final int START_Y = 25;
	private JewelBot bot;
	private JewelColor[][] jewels = new JewelColor[8][8];
	private Stack<JewelColor> unassigned = new Stack<JewelColor>();

	private HashMap<Integer, JewelColor> colorMap = new HashMap<Integer, JewelColor>();

	public BejeweledModel(JewelBot bot) {
		this.bot = bot;
		unassigned.addAll(Arrays.asList(JewelColor.values()));
		unassigned.remove(JewelColor.UNKNOWN);
		Toolkit.getDefaultToolkit().getScreenSize();
	}

	public void setNewFrame(BufferedImage frame) {
		int j_x = 0;
		int j_y = 0;
		for (int x = START_X; x < SQUARES * SQUARE_SIZE + START_X; x += SQUARE_SIZE) {
			for (int y = START_Y; y < SQUARES * SQUARE_SIZE + START_Y; y += SQUARE_SIZE) {
//				for (int w = 0; w < 20; w++){
//					for (int h = 0; h < 20; h++){
//						frame.getRGB(x+w, y+h)
//					}
//					
//				}
				
				int rgb = frame.getRGB(x, y); // TODO make this an average of
												// the square rather than
												// (25,25)'s color
				JewelColor color = colorMap.get(rgb);
				if (color == null) {
					color = assignColor(rgb);
					colorMap.put(rgb, color);
				}
				jewels[j_x][j_y] = color;
				j_y++;
			}
			j_x++;
			j_y = 0;
		}
		System.out.println(jewelsString());
		if (frameIsEmpty()) {
			return;
		}

		doMoves(computeMoves());

	}

	private boolean frameIsEmpty() {
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 8; x++) {
				if (!jewels[x][y].equals(JewelColor.UNKNOWN))
					return false;
			}
		}

		return true;
	}

	public int resolveX(int x) {
		return x * SQUARE_SIZE + START_X;
	}

	public int resolveY(int y) {
		return y * SQUARE_SIZE + START_Y;

	}

	public void doMoves(ArrayList<Move> moves) {
		for (Move m : moves) {
			bot.moveMouse(resolveX(m.startJewelX), resolveY(m.startJewelY));
			bot.click();
			bot.moveMouse(resolveX(m.endJewelX), resolveY(m.endJewelY));
			bot.click();
		}
	}

	public ArrayList<Move> computeMoves() {
		ArrayList<Move> m = new ArrayList<Move>();

		/*
		 * X OX X
		 */
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 8; x++) {
				JewelColor o = jewels[x][y];
				if (o.equals(JewelColor.UNKNOWN))
					continue;
				if (jewels[x][y + 2].equals(o)) {
					if (x > 0 && jewels[x - 1][y + 1].equals(o)) {
						m.add(new Move(x - 1, y + 1, x, y + 1));
					} else if (x < 7 && jewels[x + 1][y + 1].equals(o)) {
						m.add(new Move(x + 1, y + 1, x, y + 1));
					}
				}
			}
		}

		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 6; x++) {
				JewelColor o = jewels[x][y];
				if (o.equals(JewelColor.UNKNOWN))
					continue;
				if (jewels[x + 2][y].equals(o)) {
					if (y > 0 && jewels[x + 1][y - 1].equals(o)) {
						m.add(new Move(x + 1, y - 1, x + 1, y));
					} else if (y < 7 && jewels[x + 1][y + 1].equals(o)) {
						m.add(new Move(x + 1, y + 1, x + 1, y));
					}
				}
			}
		}

		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				JewelColor o = jewels[x][y];
				if (o.equals(JewelColor.UNKNOWN))
					continue;
				if (x > 2 && jewels[x - 1][y].equals(o)) {
					if (y > 0 && jewels[x - 2][y - 1].equals(o)) {
						m.add(new Move(x - 2, y - 1, x - 2, y));
					} else if (y < 7&& jewels[x - 2][y + 1].equals(o)) {
						m.add(new Move(x - 2, y + 1, x - 2, y));
					} else if (x > 3 && jewels[x - 3][y].equals(o)) {
						m.add(new Move(x - 3, y, x - 2, y));
					}
				}
				if (x < 6 && jewels[x + 1][y].equals(o)) {
					if (y > 0 && jewels[x + 2][y - 1].equals(o)) {
						m.add(new Move(x + 2, y - 1, x + 2, y));
					} else if (y < 7 && jewels[x + 2][y + 1].equals(o)) {
						m.add(new Move(x + 2, y + 1, x + 2, y));
					} else if (x < 5 && jewels[x + 3][y].equals(o)) {
						m.add(new Move(x + 3, y, x + 2, y));
					}
				}
				if (y > 1 && jewels[x][y - 1].equals(o)) {
					if (x > 0 && jewels[x - 1][y - 2].equals(o)) {
						m.add(new Move(x - 1, y - 2, x , y - 2));
					} else if (x < 7&& jewels[x + 1][y - 2].equals(o)) {
						m.add(new Move(x + 1, y - 2, x , y - 2));
					} else if (y > 3 && jewels[x][y - 3].equals(o)) {
						m.add(new Move(x, y - 3, x, y -2));
					}
				}
				
				if (y < 6 && jewels[x][y + 1].equals(o)) {
					if (x > 0 && jewels[x -1][y +2].equals(o)) {
						m.add(new Move(x -1, y + 2, x, y+2));
					} else if (x < 7 && jewels[x + 1][y + 2].equals(o)) {
						m.add(new Move(x + 1, y + 2, x , y+2));
					} else if (y < 5 && jewels[x][y+3].equals(o)) {
						m.add(new Move(x , y+3, x, y+2));
					}
				}

			}
		}

		// m.clear();
		// m.add(new Move(0,0,1,0));

		return m;
	}

	public String jewelsString() {
		String r = "";
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				r += jewels[x][y].toString().substring(0, 1) + " ";
			}
			r += "\n";
		}

		return r;
	}

	public JewelColor assignColor(int rgb) {
		// TODO a more complex implementation with color averages would be nice
		if (unassigned.size() > 0) {
			return unassigned.pop();
		}

		return JewelColor.UNKNOWN;
	}

}
