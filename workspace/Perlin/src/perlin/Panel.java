package perlin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



class Panel extends JPanel
{
	Perlin perlin;
	//public JButton button;
	public BufferedImage bi;
	public Component imageComponent;
	
	public int seed;
	
	public int width;
	  
	public int height;
	  
	public int initFreq = 12;
	  
	public float persistency = 1.8f;
	
	public int density = 0x70;
	
	public float cloudSharpness = 0.9875f;
	
	public int detail = 5;
	
	public int strange1 = 16;
	
	public int strange2 = 8;
	
	public Panel(Perlin perlin) {
	    this.perlin = perlin;
	}
	
    protected void paintComponent(Graphics g) {
    	
    	//System.out.println("Painting...");
    	BufferedImage image = null;

    	image = new Perlin(512, 512, initFreq, persistency, density, cloudSharpness, detail, seed).getImage(); // get the buffered image.
    	
		//perlin = new Perlin(1024, 1024, 12, 1.8fx70, 0.9875f, 7, seed); //original
		Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.drawImage(image, 0, 0, null);
        super.paintComponents(g);
    }

    public void show() {
    	
    	JFrame frame = new JFrame("Perlin Test");
    	JPanel mainPanel = new JPanel();
    	JPanel controls = new JPanel();
    	JPanel output = new JPanel();
    	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        //frame.setLayout(new GridLayout(1,1));
        
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        
        JButton button = new JButton("Generate");
        
        /*initFreq slider*/
        JSlider freqSlider = addSlider(controls, 0, 50, 12, 1, "initFreq");
        
        /*Persistence slider*/
        JSlider persistenceSlider = addSlider(controls, 0, 10, 1, 1, "persistence");
        
        /*density slider*/
        JSlider densitySlider = addSlider(controls, 0, 255, 0x70, 25, "density");
        densitySlider.setMajorTickSpacing(25);
        densitySlider.setMinorTickSpacing(1);
        densitySlider.setPaintTicks(true);
        densitySlider.setPaintLabels(true);
        /*Cloud Sharpness Slider*/
        JSlider cloudSharpnessSlider = addSlider(controls, 0, 9999, 9875, 100, "cloudSharpness");
        cloudSharpnessSlider.setMajorTickSpacing(1000);
        cloudSharpnessSlider.setMinorTickSpacing(1000);
        cloudSharpnessSlider.setPaintTicks(true);
        cloudSharpnessSlider.setPaintLabels(true);
        
        /*Detail Slider*/
        JSlider detailSlider = addSlider(controls, 0, 10, 5, 1, "detail");
        detailSlider.setMajorTickSpacing(1);
        detailSlider.setMajorTickSpacing(1);
        detailSlider.setPaintTicks(true);
        detailSlider.setPaintLabels(true);
        
        //
        Dictionary dictionary = detailSlider.getLabelTable();
        if (dictionary != null) {
          Enumeration keys = dictionary.keys();
          while (keys.hasMoreElements()) {
             JLabel thisLabel = (JLabel) dictionary.get(keys.nextElement());
             //thisLabel.setDisplayedMnemonicIndex(-1);
             //System.out.println(thisLabel.getText());
             
             //thisLabel.setForeground(Color.blue);
             // uncomment these following lines to get a background for your labels
             //int labelNumeric = ;
             if(Integer.parseInt(thisLabel.getText()) >= 7){
            	 thisLabel.setForeground(Color.red);
            	 thisLabel.setOpaque(true);
            	 //thisLabel.setBackground(Color.);
             }
             
             //thisLabel.setBackground(Color.green);
             
          }
       }
        //
        
        /* Slider ChangeListeners */
        freqSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
        
                initFreq = freqSlider.getValue();
                //initFreq = tmp / 10;
                //System.out.println("initFreq: "+initFreq);
                repaint();
            }
        });
        persistenceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                persistency = persistenceSlider.getValue();
                repaint();
            }
        });
        densitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
        
                //float tmp = density.getValue();
                //density = tmp / 10000;
            	density = densitySlider.getValue();
                repaint();
            }
        });
        cloudSharpnessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
        
                float tmp = cloudSharpnessSlider.getValue();
                cloudSharpness = tmp / 10000;
                repaint();
            }
        });
        detailSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
        
                detail = detailSlider.getValue();
                repaint();
            }
        });
        
        controls.add(button);
        
        button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	new Perlin(512, 512, initFreq, persistency, density, cloudSharpness, detail, seed).setStrange();
		    	repaint();
		    }
		});//Reads the action.
        //frame.add(this); //This line adds the image to the frame
        //updateImage(frame); //this generates a new Perlin and updates the frame
        //output.add(this);
        mainPanel.add(this);
        
        mainPanel.add(controls);
        
        frame.add(mainPanel);
        frame.setSize(1100, 512);
        
        output.setPreferredSize(new Dimension(512,512));
        controls.setPreferredSize(new Dimension(64, 256));
        //mainPanel.setPreferredSize(new Dimension(1366, 768));
        //frame.pack();
        frame.setVisible(true);
        mainPanel.setVisible(true);
        output.setVisible(true);
        controls.setVisible(true);
        
    }
    
    public JSlider addSlider(JPanel mainPanel, int min, int max, int startPos, int tickSpacing, String myLabel){ 
    	JSlider thisSlider = new JSlider(JSlider.HORIZONTAL,
                min, max, startPos);
        thisSlider.setPreferredSize(new Dimension(500, 500));
        JLabel label = new JLabel(myLabel);
        mainPanel.add(label);
        mainPanel.add(thisSlider);
        
        if(max<255 && max != 10){
	        thisSlider.setMajorTickSpacing(10);
	        thisSlider.setMinorTickSpacing(1);
	        thisSlider.setPaintTicks(true);
	        thisSlider.setPaintLabels(true);
        }
        
        //thisSlider.setForeground(Color.WHITE);
        //thisSlider.setBackground(Color.BLACK);
        Dictionary dictionary = thisSlider.getLabelTable();
//        if (dictionary != null) {
//           Enumeration keys = dictionary.keys();
//           while (keys.hasMoreElements()) {
//              JLabel thisLabel = (JLabel) dictionary.get(keys.nextElement());
//              //thisLabel.setDisplayedMnemonicIndex(-1);
//              //System.out.println(thisLabel.getText());
//              
//              thisLabel.setForeground(Color.blue);
//              // uncomment these following lines to get a background for your labels
//              //int labelNumeric = ;
//              if(Integer.parseInt(thisLabel.getText()) >= 7){
//            	  thisLabel.setForeground(Color.red);
//              }
//              thisLabel.setOpaque(true);
//              thisLabel.setBackground(Color.green);
//              
//           }
//        }
        
        return thisSlider;
    }

}