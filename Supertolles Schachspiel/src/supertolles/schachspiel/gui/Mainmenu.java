package supertolles.schachspiel.gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Mainmenu {
	private JButton start, options, end;
	private JPanel mainmenuOverlay;
	private Cursor cursor;

	public Mainmenu(Game g) {
	}

	public void enableMainmenu(Game g){
		cursor= new Cursor(12);
		mainmenuOverlay = new JPanel();
		mainmenuOverlay.setBounds(0, 0, g.getWidth(), g.getHeight());
		mainmenuOverlay.setLayout(null);
		mainmenuOverlay.setOpaque(true);
		g.getContentPane().add(mainmenuOverlay);
		
		start = new JButton("Start game");
		start.setFocusable(false);
		start.setContentAreaFilled(false);
		start.setOpaque(false);
		start.setBorderPainted(false);
		start.setVisible(true);
		start.setRolloverEnabled(true);
		start.setCursor(cursor);
		start.setBounds((int) g.getOptions().getResolution().getWidth()/20,(int) g.getOptions().getResolution().getHeight()/8,(int) g.getOptions().getResolution().getWidth()/7,(int) g.getOptions().getResolution().getHeight()/12);
		start.setFont(new Font("Tahoma", Font.PLAIN, (int) (start.getHeight()*0.4)));
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				disableMainmenu(g);
				g.getGameOptions().enableGameOptions(g);
				g.repaint();
				g.getContentPane().repaint();
			}
		});
		mainmenuOverlay.add(start);
		
		options = new JButton("Settings");
		options.setFocusable(false);
		options.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				disableMainmenu(g);
				g.getOpt().enableOptions(g);
				g.repaint();
				g.getContentPane().repaint();
			}
		});
		options.setContentAreaFilled(false);
		options.setBorderPainted(false);
		options.setOpaque(false);
		options.setVisible(true);
		options.setFont(start.getFont());
		options.setCursor(cursor);
		options.setBounds(start.getX(),start.getY()+start.getHeight()+start.getHeight()/3,start.getWidth(),start.getHeight());
		mainmenuOverlay.add(options);
		
		end = new JButton("End game");
		end.setFocusable(false);
		end.setContentAreaFilled(false);
		end.setOpaque(false);
		end.setBorderPainted(false);
		end.setFont(start.getFont());
		end.setCursor(cursor);
		end.setVisible(true);
		end.setBounds(options.getX(),options.getY()+options.getHeight()+options.getHeight()/3,options.getWidth(),options.getHeight());
		end.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mainmenuOverlay.add(end);
	}
	
	
	
	public void disableMainmenu(Game g){
		g.remove(mainmenuOverlay);
		mainmenuOverlay.setVisible(false);
		mainmenuOverlay=null;
	}
}