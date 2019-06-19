package supertolles.schachspiel.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import lombok.*;

@Getter
public class Options {
	private GameOptions gOp;
	private JPanel optionOverlay;
	private JLabel res, fs, vol, head, info;
	private JComboBox<String> resBox, fsBox;
	private JSlider volume;
	private JButton accept, toDefault, mainmenu;
	private Cursor cursor;

	/**
	 * @author Niklas
	 * @param g
	 */
	public Options(Game g) {
		gOp = g.getOptions();
	}
	
	public void enableOptions(Game g){
		cursor = new Cursor(Cursor.HAND_CURSOR);
		optionOverlay = new JPanel();
		optionOverlay.setBounds(0, 0, g.getWidth(), g.getHeight());
		optionOverlay.setLayout(null);
		
		head = new JLabel("Einstellungen");
		head.setBounds(optionOverlay.getWidth()/2, optionOverlay.getHeight()/100, optionOverlay.getWidth()/6, optionOverlay.getHeight()/12);
		head.setBounds(head.getX()-head.getWidth()/2, head.getY(), head.getWidth(), head.getHeight());
		head.setFont(new Font("Tahoma", Font.BOLD, (int) (head.getHeight()*0.5)));
		head.setVisible(true);
		optionOverlay.add(head);
		
		res = new JLabel("Auflösung") ;
		res.setBounds(optionOverlay.getWidth()/12, optionOverlay.getHeight()/10, optionOverlay.getWidth()/8, optionOverlay.getHeight()/17);
		res.setFont(new Font("Tahoma", Font.PLAIN, (int) (res.getHeight()*0.4)));
		res.setVisible(true);
		optionOverlay.add(res);
		
		optionOverlay.add(createComboBox(gOp));
		
		fs = new JLabel("Anzeigemodus");
		fs.setBounds(res.getX(), res.getY()+res.getHeight(), res.getWidth(), res.getHeight());
		fs.setFont(new Font("Tahoma", Font.PLAIN, (int) (fs.getHeight()*0.4)));
		optionOverlay.add(fs);
		
		optionOverlay.add(createFsComboBox(gOp));
		
		vol = new JLabel("Lautstärke");
		vol.setBounds(fs.getX(), fs.getY()+fs.getHeight()+fs.getHeight()/2, fs.getWidth(), fs.getHeight());
		vol.setFont(new Font("Tahoma", Font.PLAIN, (int) (vol.getHeight()*0.4)));
		vol.setVisible(true);
		optionOverlay.add(vol);
		
		volume = new JSlider();
		volume.setValue(gOp.getVolume());
		volume.setBounds(vol.getX()+vol.getWidth(),vol.getY(),resBox.getWidth(),vol.getHeight());
		optionOverlay.add(volume);
		
		accept = new JButton("Übernehmen");
		accept.setBounds(volume.getX()+volume.getWidth()+volume.getWidth()/2, volume.getY()+volume.getHeight()+volume.getHeight()/2, volume.getWidth()+(int)(volume.getWidth()*0.3), volume.getHeight()+volume.getHeight()/2);
		accept.setCursor(cursor);
		accept.setFont(new Font("Tahoma", Font.PLAIN, (int) (accept.getHeight()*0.4)));
		accept.setContentAreaFilled(false);
		accept.setBorderPainted(false);
		accept.setFocusable(false);
		accept.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gOp.overwriteSettings(g);
				accept(g);
			}
		});
		optionOverlay.add(accept);
		
		toDefault = new JButton("Standardeinstellung");
		toDefault.setBounds(accept.getX(), accept.getY()+accept.getHeight()+accept.getHeight()/5, accept.getWidth(), accept.getHeight());
		toDefault.setCursor(cursor);
		toDefault.setFont(new Font("Tahoma", Font.PLAIN, (int) (toDefault.getHeight()*0.3)));
		toDefault.setContentAreaFilled(false);
		toDefault.setBorderPainted(false);
		toDefault.setFocusable(false);
		toDefault.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gOp.setDefaultSettings();
				accept(g);
			}
		});
		optionOverlay.add(toDefault);
		
		mainmenu= new JButton("Hauptmenü");
		mainmenu.setBounds(toDefault.getX(), toDefault.getY()+toDefault.getHeight()+toDefault.getHeight()/5, toDefault.getWidth(), toDefault.getHeight());
		mainmenu.setCursor(cursor);
		mainmenu.setFont(accept.getFont());
		mainmenu.setContentAreaFilled(false);
		mainmenu.setBorderPainted(false);
		mainmenu.setFocusable(false);
		mainmenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				disableOptions(g);
				g.getMenu().enableMainmenu(g);
				g.getContentPane().repaint();
				g.repaint();
			}
		});
		optionOverlay.add(mainmenu);
		
		info = new JLabel("Es manchmal notwendig das Programm erneut zu starten, sodass die Änderungen aktiv werden.");
		info.setBounds(vol.getX(), vol.getHeight()+vol.getY()+vol.getHeight()/2, head.getWidth(), head.getHeight()/5);
		info.setFont(new Font("Tahoma", Font.PLAIN, (int) (info.getHeight()*0.4)));
		optionOverlay.add(info);
		
		g.getContentPane().add(optionOverlay);
	}
	
	public void disableOptions(Game g){
		g.getContentPane().remove(optionOverlay);
		optionOverlay.setVisible(false);
		optionOverlay=null;
	}
	
	private JComboBox createComboBox(GameOptions g){
		ArrayList<String> list = new ArrayList<String>();
		if(g.getMaxResolution().equals(new Dimension(3840, 2160))){
			for(int i=0; i<g.getResolutions().length;i++){
				list.add(g.getResolutions()[i].getWidth()+"x"+g.getResolutions()[i].getHeight());
			}
		}else{
			switch((int) g.getMaxResolution().getHeight()){
			case 720:
				list.add(g.getResolutions()[0].getWidth()+"x"+g.getResolutions()[0].getHeight());
				break;
			case 1080:
				list.add(g.getResolutions()[0].getWidth()+"x"+g.getResolutions()[0].getHeight());
				list.add(g.getResolutions()[1].getWidth()+"x"+g.getResolutions()[1].getHeight());
				break;
			case 1440:
				list.add(g.getResolutions()[0].getWidth()+"x"+g.getResolutions()[0].getHeight());
				list.add(g.getResolutions()[1].getWidth()+"x"+g.getResolutions()[1].getHeight());
				list.add(g.getResolutions()[2].getWidth()+"x"+g.getResolutions()[2].getHeight());
				break;
			default:
				if(Toolkit.getDefaultToolkit().getScreenSize().getHeight()<720){
					throw new IllegalStateException();
				}else{
					if(Toolkit.getDefaultToolkit().getScreenSize().getHeight()<1080){
						list.add(g.getResolutions()[0].getWidth()+"x"+g.getResolutions()[0].getHeight());
					}else{
						if(Toolkit.getDefaultToolkit().getScreenSize().getHeight()<1440){
							list.add(g.getResolutions()[0].getWidth()+"x"+g.getResolutions()[0].getHeight());
							list.add(g.getResolutions()[1].getWidth()+"x"+g.getResolutions()[1].getHeight());
						}else{
							if(Toolkit.getDefaultToolkit().getScreenSize().getHeight()<2160){
								list.add(g.getResolutions()[0].getWidth()+"x"+g.getResolutions()[0].getHeight());
								list.add(g.getResolutions()[1].getWidth()+"x"+g.getResolutions()[1].getHeight());
								list.add(g.getResolutions()[1].getWidth()+"x"+g.getResolutions()[1].getHeight());
							}else{
								for(int i=0; i<g.getResolutions().length;i++){
									list.add(g.getResolutions()[i].getWidth()+"x"+g.getResolutions()[i].getHeight());
								}
							}
						}
					}
				}
				break;
			}
		}
		resBox= new JComboBox();
		for(int i=0; i<list.size();i++){
			resBox.addItem(list.get(i));
		}
		resBox.setBounds(res.getX()+res.getWidth(), res.getY()+res.getHeight()/5, optionOverlay.getWidth()/9, optionOverlay.getHeight()/25);
		resBox.setFont(new Font("Tahoma", Font.PLAIN, (int) (resBox.getHeight()*0.4)));
		resBox.setFocusable(false);
		if((gOp.getResolution()+"").contains("720")){
			resBox.setSelectedIndex(0);
		}else{
			if((gOp.getResolution()+"").contains("1080")){
				resBox.setSelectedIndex(1);
			}else{
				if((gOp.getResolution()+"").contains("1440")){
					resBox.setSelectedIndex(2);
				}else{
					resBox.setSelectedIndex(3);
				}
			}
		}
		return resBox;
	}
	
	private JComboBox createFsComboBox(GameOptions g){
		fsBox = new JComboBox<String>();
		fsBox.addItem("Vollbild");
		fsBox.addItem("Fenster");
		if(gOp.isFullScreen()){
			fsBox.setSelectedItem(0);
		}else{
			fsBox.setSelectedIndex(1);
		}
		fsBox.setBounds(fs.getX()+fs.getWidth(), fs.getY()+fs.getHeight()/5, optionOverlay.getWidth()/9, optionOverlay.getHeight()/25);
		fsBox.setFont(new Font("Tahoma", Font.PLAIN, (int) (fsBox.getHeight()*0.4)));
		fsBox.setFocusable(false);
		fsBox.setEnabled(false);
		return fsBox;
	}
	
	private void accept(Game g) {
		optionOverlay.setBounds(0, 0, g.getWidth(), g.getHeight());
		head.setBounds(optionOverlay.getWidth()/2, optionOverlay.getHeight()/100, optionOverlay.getWidth()/6, optionOverlay.getHeight()/12);
		head.setBounds(head.getX()-head.getWidth()/2, head.getY(), head.getWidth(), head.getHeight());
		res.setBounds(optionOverlay.getWidth()/12, optionOverlay.getHeight()/10, optionOverlay.getWidth()/8, optionOverlay.getHeight()/17);
		resBox.setBounds(res.getX()+res.getWidth(), res.getY()+res.getHeight()/5, optionOverlay.getWidth()/9, optionOverlay.getHeight()/25);
		fs.setBounds(res.getX(), res.getY()+res.getHeight(), res.getWidth(), res.getHeight());
		fsBox.setBounds(fs.getX()+fs.getWidth(), fs.getY()+fs.getHeight()/5, optionOverlay.getWidth()/9, optionOverlay.getHeight()/25);
		vol.setBounds(fs.getX(), fs.getY()+fs.getHeight()+fs.getHeight()/2, fs.getWidth(), fs.getHeight());
		volume.setBounds(vol.getX()+vol.getWidth(),vol.getY(),resBox.getWidth(),vol.getHeight());
		accept.setBounds(volume.getX()+volume.getWidth()+volume.getWidth()/2, volume.getY()+volume.getHeight()+volume.getHeight()/2, volume.getWidth()+(int)(volume.getWidth()*0.3), volume.getHeight()+volume.getHeight()/2);
		info.setBounds(vol.getX(), vol.getHeight()+vol.getY()+vol.getHeight()/2, head.getWidth(), head.getHeight()/5);
		toDefault.setBounds(accept.getX(), accept.getY()+accept.getHeight()+accept.getHeight()/5, accept.getWidth(), accept.getHeight());
		mainmenu.setBounds(toDefault.getX(), toDefault.getY()+toDefault.getHeight()+toDefault.getHeight()/5, toDefault.getWidth(), toDefault.getHeight());
		
		head.setFont(new Font("Tahoma", Font.BOLD, (int) (head.getHeight()*0.5)));
		res.setFont(new Font("Tahoma", Font.PLAIN, (int) (res.getHeight()*0.4)));
		resBox.setFont(new Font("Tahoma", Font.PLAIN, (int) (resBox.getHeight()*0.4)));
		fs.setFont(new Font("Tahoma", Font.PLAIN, (int) (fs.getHeight()*0.4)));
		fsBox.setFont(new Font("Tahoma", Font.PLAIN, (int) (fsBox.getHeight()*0.4)));
		vol.setFont(new Font("Tahoma", Font.PLAIN, (int) (vol.getHeight()*0.4)));
		accept.setFont(new Font("Tahoma", Font.PLAIN, (int) (accept.getHeight()*0.4)));
		info.setFont(new Font("Tahoma", Font.PLAIN, (int) (info.getHeight()*0.4)));
		toDefault.setFont(new Font("Tahoma", Font.PLAIN, (int) (toDefault.getHeight()*0.3)));
		mainmenu.setFont(accept.getFont());
		
		g.repaint();
		g.getContentPane().repaint();
	}

}
