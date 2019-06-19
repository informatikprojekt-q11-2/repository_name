package supertolles.schachspiel.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import supertolles.schachspiel.Constants;
import supertolles.schachspiel.GameOverlay;

public class Game extends JFrame {
	private JPanel contentPane;
	private Mainmenu mainmenu;
	private GameOptions options;
	private supertolles.schachspiel.GameOptions setup;
	private Options opt;

	/**
	 * @author Niklas
	 * Create the frame.
	 */
	public Game() {
		options = new GameOptions(this);
		opt = new Options(this);
		mainmenu = new Mainmenu(this);
		setup = new supertolles.schachspiel.GameOptions(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Chess");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		mainmenu.enableMainmenu(this);
		repaint();
		setIconImage(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackQueen)).getImage());
		contentPane.repaint();
	}
	
	public GameOptions getOptions(){
		return options;
	}
	
	public Mainmenu getMenu(){
		return mainmenu;
	}
	
	public Options getOpt(){
		return opt;
	}
	
	public supertolles.schachspiel.GameOptions getGameOptions(){
		return setup;
	}

}
