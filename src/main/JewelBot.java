package main;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class JewelBot {

	private static final long UPDATE_TIME = 300;
	private Robot robot;
	private Rectangle screen;
	private BufferedImage frame;
	private Timer timer;
	private BejeweledModel model;
	private int originX;
	private int originY;

	public JewelBot() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void moveMouse(int x, int y) {
		robot.mouseMove(x+originX,y+originY);
	}

	public void click() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public void initializeCoordinates(int x, int y, int brX, int brY) {
		originX = x;
		originY = y;
		screen = new Rectangle(x, y, brX - x, brY - y);
	}

	public void writeFrame() {
		File outputfile = new File("frame.png");
		try {
			ImageIO.write(frame, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateFrame() {
		frame = robot.createScreenCapture(screen);
		model.setNewFrame(frame);

	}

	public static void main(String[] args) throws AWTException {
		JewelBot jb = new JewelBot();

		JewelBotUI ui = new JewelBotUI(jb);
		ui.show();

	}

	public void startGame() {
		model = new BejeweledModel(this);
		if (timer != null)
			return;

		initializeCoordinates(734, 518, 1053, 835);

		timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				updateFrame();
				System.out.println("UPDATE");
			}
		}, 0, UPDATE_TIME);
	}

	public void stopGame() {
		timer.cancel();
		model = null;
	}

}
