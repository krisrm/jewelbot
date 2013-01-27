package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JewelBotUI {

	private JFrame mainframe;
	private JPanel panel = new JPanel();
	private JewelBot jb;
	
	public JewelBotUI(JewelBot j){
		this.jb = j;
		this.mainframe = new JFrame("JewelBot");
		mainframe.setSize(200, 100);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setContentPane(panel);
		mainframe.setLocationRelativeTo(null);
		
		
		JButton start = new JButton("Start Game");
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jb.startGame();
			}
		});
		
		JButton test = new JButton("Write FrameBuffer");
		test.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jb.writeFrame();
			}
		});
		
		
		JButton stop = new JButton("Stop Game");
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jb.stopGame();
			}
		});
		
		panel.add(start);
		//panel.add(test);
		panel.add(stop);
	}
	
	public void show(){
		mainframe.setVisible(true);
	}
	
	
}
