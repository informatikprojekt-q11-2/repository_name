package supertolles.schachspiel.gui;

import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameOptions {
	private Dimension resolution, maxResolution;
	private boolean fullScreen;
	private String path = "./settings.conf";
	private int volume;
	private Dimension[] resolutions= new Dimension[]{new Dimension(1280, 720)
			,new Dimension(1920, 1080), new Dimension(2560, 1440), new Dimension(3840, 2160)};

	/**
	 * @author Niklas
	 * @param g
	 */
	public GameOptions(Game g) {
		readData();
		reloadFrame(g);
	}
	
	private void readData(){
		File datei = new File(path);
		BufferedReader in = null;
		if (!datei.exists()) {
		try {
			datei.createNewFile();
			setDefaultSettings();
		    } catch (IOException ex) {
			ex.printStackTrace();
		    }
		} else {
		    String zeileninhalt;
		    try {
			in = new BufferedReader(new FileReader(path));
			while ((zeileninhalt = in.readLine()) != null) {
			    parseSettings(zeileninhalt);
			}
		    } catch (Exception ex) {
			ex.printStackTrace();
		    } finally {
			if (in != null) {
			    try {
				in.close();
			    } catch (IOException e) {
				e.printStackTrace();
			    }
			}
		    }
		}
	}
	
	public void setDefaultSettings(){
		BufferedWriter output=null;
		try {
			output = new BufferedWriter(new FileWriter(path));
			output.write("?fs:"+true);
			output.newLine();
			output.write("Resdim:"+Toolkit.getDefaultToolkit().getScreenSize());
			output.newLine();
			output.write("MaxResDim:"+Toolkit.getDefaultToolkit().getScreenSize());
			output.newLine();
			output.write("FullScreen:"+false);
			output.newLine();
			output.write("Lautstärke:"+50);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (output != null) {
				try {
				    output.close();
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
			    }
		}
	}
	
	/**
	 * Durch den Methodenaufruf wird die settings.conf Datei nach Variablen untersucht,
	 * die in den Optionen gegeben sind. Diese werden im gleichen Zuge gesetzt
	 * 
	 */
	private void parseSettings(String zeileninhalt){
		if(zeileninhalt.contains("?fs")){
			String[] inh = zeileninhalt.split(":");
			try{
				boolean firstStart=Boolean.parseBoolean(inh[1]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			if(zeileninhalt.contains("Resdim")){
				String[] inh = zeileninhalt.split(":");
				inh = inh[1].split(",");
				String[] inh2 = inh[0].split("=");
				inh = inh[1].split("=");
				inh=inh[1].split("]");
				try{
					resolution= new Dimension(Integer.parseInt(inh2[1]), Integer.parseInt(inh[0]));
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				if(zeileninhalt.contains("MaxResDim")){
					String[] inh = zeileninhalt.split(":");
					inh = inh[1].split(",");
					String[] inh2 = inh[0].split("=");
					inh = inh[1].split("=");
					inh = inh[1].split("]");
					try{
						maxResolution= new Dimension(Integer.parseInt(inh2[1]), Integer.parseInt(inh[0]));
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					if(zeileninhalt.contains("FullScreen")){
						String[] inh = zeileninhalt.split(":");
						try{
							fullScreen= Boolean.parseBoolean(inh[1]);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						if(zeileninhalt.contains("Lautstärke")){
							String[] inh = zeileninhalt.split(":");
							try{
								volume= Integer.parseInt(inh[1]);
							}catch(Exception e){
								e.printStackTrace();
							}
						}else{
						}
					}
				}
			}
		}
	}
	
	/**
	 * Die gesetzten Werte der Variablen werden auf das JFrame, bzw. das Spiel angewendet
	 */
	public void reloadFrame(Game g){
		g.setSize(resolution);
		if(fullScreen){
			g.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			try{
				g.setUndecorated(true);
			}catch (IllegalComponentStateException e){
				try{
					g.setUndecorated(true);
				}catch (IllegalComponentStateException e1){
					try{
						g.setUndecorated(true);
					}catch (IllegalComponentStateException e2){
					}
				}
			}
			g.setVisible(true);
		}else{
			g.setExtendedState(JFrame.NORMAL);
			try{
				g.setUndecorated(false);
			}catch (IllegalComponentStateException e){
				try{
					g.setUndecorated(false);
				}catch (IllegalComponentStateException e1){
					try{
						g.setUndecorated(false);
					}catch (IllegalComponentStateException e2){
					}
				}
			}
			g.setVisible(true);
		}
	}
	
	public void overwriteSettings(Game g){
		String[] s=g.getOpt().getResBox().getSelectedItem().toString().split("x");
		resolution= new Dimension((int) Double.parseDouble(s[0]), (int) Double.parseDouble(s[1]));
		fullScreen= g.getOpt().getFsBox().getSelectedIndex()==0;
		volume = g.getOpt().getVolume().getValue();
		BufferedWriter output=null;
		try {
			output = new BufferedWriter(new FileWriter(path));
			output.write("?fs:"+false);
			output.newLine();
			output.write("Resdim:"+resolution);
			output.newLine();
			output.write("MaxResDim:"+maxResolution);
			output.newLine();
			output.write("FullScreen:"+fullScreen);
			output.newLine();
			output.write("Lautstärke:"+volume);
		} catch (IOException ioe) {
			setDefaultSettings();
		}finally{
			if (output != null) {
				try {
				    output.close();
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
			    }
		}
		reloadFrame(g);
		g.repaint();
		
		g.getOpt().getOptionOverlay().repaint();
	}

}
