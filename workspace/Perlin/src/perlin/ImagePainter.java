package perlin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImagePainter {
	
	public String mode;
	public int dimension;
	public int colorShift;
	//public int octaves = 2;
	//public double persistence = 0.8;
	//public ImprovedNoise perlin;
	public BufferedImage image;
	public boolean outputImageFile = true;
	public int gifSpeed = 5;
	
	public BufferedImage createImage(int dimension, String mode, int width, int height, int depth, ImprovedNoise perlin, 
			int octaves, double persistence, int size, int offsetFactor, int colorShiftAmount, int red, int green, int blue){
			
			long startTime = System.nanoTime();
		
			int arr2D[][] = new int[width][height];
			int arr3D[][][] = new int [width][height][depth];
			
			
			//Progress Bar
			final ProgressBar progressBar = new ProgressBar(0, depth);
			JFrame frame = new JFrame("Generating 3D Perlin Noise");
			if( dimension == 3) {
			
				progressBar.setSize(new Dimension(300, 64));
			   
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			    frame.setPreferredSize(new Dimension(300, 64));
			    frame.setContentPane(progressBar);
			    frame.pack();
			    //Set JFRAME in center of screen
			    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			    frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
			    frame.setVisible(true);
			}
			//Initialize our main image
			
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
				
			if(mode.equals("NORMAL")) {
				//System.out.println("Creating image in NORMAL mode.");
				//black & white
			}
			if(mode == "LINES") {
				//System.out.println("Creating image in LINES mode.");
				//lines
			}
			BufferedImage[] bi = new BufferedImage[depth];
			BufferedImage f = null;
			BufferedImage l = null;
			switch (dimension ) {
			///////////////////////////////////////////////////////////////////////////
			//case 2
			
			case 2://For a 2D perlin object
			int[] all = new int[width*height];
			int ctr=0;
			for (int i = 0; i<width; i++){
				for (int j = 0; j<height; j++){
					//double x = (double)i/(size*2);
					double x = (double)i/(size);
					double y = (double)j/size;
					
					
					int tmp = (int) (perlin.noise(x, y, 0.0, octaves, persistence)*(size-1));
					//if (tmp < 0){ tmp = 0; }
					all[ctr]=tmp;
					//tmp=tmp+107;
					arr2D[i][j] = tmp;
					
					//System.out.print(arr2D[i][j]+" ");
					ctr++;
				}
				//System.out.println("");
			}
			//System.out.println("Successfully generated 2D perlin object.");
			List b = Arrays.asList(all);
			System.out.println("max: "+getMaxValue(all));
			System.out.println("min: "+getMinValue(all));
			int range=-getMinValue(all)+getMaxValue(all);
			System.out.println("range: "+range);
			//Fill each pixel in the image
			int re=0;
			int gr=0;
			int bl=0;
			Color color = new Color(re, gr, bl);
			if(mode.equals("NORMAL")){
				//System.out.println("NORMAL MODE");
				int alpha = Color.BLACK.getRGB();
				for (int i = 0; i<width; i++){
					for (int j = 0; j<height; j++){
						
						int val = arr2D[i][j];
						//color = new Color(re, gr+(Math.abs(val))*2, bl-(Math.abs(val))*2);
						//color = new Color(Math.abs(val), Math.abs(val), Math.abs(val));
						//val = Math.abs(val);
						//val = val+Math.abs(getMinValue(all));
						
//						//invert
//						if(val>=0){
//							val=val-val;
//						}else if(val<0){
//							val=Math.abs(val)*2;
//						}
//						////////
//						val=Math.abs(val);
//						color = new Color(val+110,val+150,val+170);
//						///

						//Color currentColor = colorize(val, red, green, blue);
						image.setRGB(i, j, colorize(val, red, green, blue).getRGB() << colorShiftAmount);
					}
				}
			}else if(mode.equals("LINES")){
				//System.out.println("LINES MODE");
				int alpha = Color.BLACK.getRGB();
				for (int i = 0; i<width; i++){
					for (int j = 0; j<height; j++){
						int val = arr2D[i][j];
						int offset=val/offsetFactor;
						if((j%4)==0){
							if(j>0 && j<490){

									//image.setRGB(i, Math.abs(j+offset), paintClouds(val, Color.BLACK).getRGB() << colorShiftAmount);
									image.setRGB(i, Math.abs(j+offset), colorize(val, red, green, blue).getRGB() << colorShiftAmount);
									
									
									
									//image.setRGB(i, Math.abs(j+offset), (val << 1) + (val << 79) + val);//LAVA LAMP
								//image.setRGB(i, j, (val << 30) + (val << 1) + val);
							}
						}
					}
				}
			}else
				
			g.drawImage(image, null, 0, 0 );
			
			
			//OUTPUT 2D IMAGE!
			if( outputImageFile == true){
				try
				{
					ImageIO.write(image, "png", new File("test.png"));
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//	
			
			//////////////////////////////////////////////////////////////////////////
			System.out.println("Complete!");
			//////////////////////////////////////////////////////////////////////////
			
			break;
			///////////////////////////////////////////////////////////////////////////
			//case 3
			case 3://For a 3d perlin object
			
			int val = 0;
			for (int i = 0; i<width; i++){
				for (int j = 0; j<height; j++){
					for (int k = 0; k<depth; k++){
						//double x = (double)i/(size*2);
						double x = (double)i/(size);
						double y = (double)j/size;
						double z = (double)k/size;
						int tmp = (int) (perlin.noise(x, y, z, octaves, persistence)*size);
						//if (tmp < 0){ tmp = 0; }
						//tmp=tmp+255;
						arr3D[i][j][k] = tmp;

					}
				}

			}
			if(mode.equals("NORMAL")){
				color = new Color(0,0,0);
				BufferedImage blackImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				for (int i = 0; i<width; i++){
					for (int j = 0; j<height; j++){			
						blackImg.setRGB(i, j, Color.BLACK.getRGB());
					}
				}
				//System.out.println("NORMAL MODE");
				for (int k = 0; k<depth; k++){
					BufferedImage copy=copyImage(blackImg);
					for (int i = 0; i<width; i++){
						for (int j = 0; j<height; j++){			
							val = arr3D[i][j][k];	
							//image.setRGB(i, j, (val << 16) + (val << 8) + val);	
							//image.setRGB(i, j, paintClouds(val, color).getRGB());
							copy.setRGB(i, j, colorize(val, red, green, blue).getRGB() << colorShiftAmount);
							image = copy;
						}
					}				
					g.drawImage(image, null, 0, 0 );
							
					bi[k] = copyImage(image);
				
				}//SCOPE END FOR LOOPS
			}else if(mode.equals("LINES")){
				//CREATE BLACK IMAGE ONCE///////////
				BufferedImage blackImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				for (int i = 0; i<width; i++){
					for (int j = 0; j<height; j++){			
						blackImg.setRGB(i, j, Color.BLACK.getRGB());
					}
				}
				////////////////////////////////////
				//System.out.println("LINES MODE");
				for (int k = 0; k<depth; k++){
					//image = blackImg;
					BufferedImage copy=copyImage(blackImg);
					for (int i = 0; i<width; i++){
						for (int j = 0; j<height; j++){			
							val = arr3D[i][j][k];
							int offset=val/offsetFactor;
							if((j%2)==0){
								if(j>0 && j<(height-16)){	
									copy.setRGB(i, Math.abs(j+offset), colorize(val, red, green, blue).getRGB() << colorShiftAmount);
									//copy.setRGB(i, Math.abs(j+offset), paintClouds(val, Color.BLACK).getRGB() << colorShiftAmount);
									//copy.setRGB(i, Math.abs(j+offset), Color.GRAY.getRGB() << colorShiftAmount);
									image=copy;
									
								}
							}
						}
					}				
					g.drawImage(image, null, 0, 0 );
							
					bi[k] = copyImage(image);
					//UPDATE PROGRESS BAR
					int percent = k;
					SwingUtilities.invokeLater(new Runnable() {
			              public void run() {
			                progressBar.updateBar(percent);
			              }
			        });
					
				}//SCOPE END FOR LOOPS
				
			}
			
			//////////////////////////////////////////////////////////////////////////
			//CREATE GIF
			// grab the output image type from the first image in the sequence
			//GIF PROGRESS BAR
			//Progress Bar
//			final ProgressBar gifProgressBar = new ProgressBar(0, bi.length);
//			gifProgressBar.setSize(new Dimension(300, 64));
//		    JFrame frame2 = new JFrame("Generating Gif");
//		    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
//		    frame2.setPreferredSize(new Dimension(300, 64));
//		    frame2.setContentPane(gifProgressBar);
//		    frame2.pack();
//		    //Set JFRAME in center of screen
//		    frame2.setLocation(dim.width/2-frame2.getSize().width/2, dim.height/2-frame2.getSize().height/2);
//		    frame2.setVisible(true);
			//////////////////
		    BufferedImage firstImage = bi[0];

		    // create a new BufferedOutputStream with the last argument
			ImageOutputStream output;
			try {
				output = new FileImageOutputStream(new File("output.gif"));
			    // create a gif sequence with the type of the first image, 1 second
			    // between frames, which loops continuously
			    GifSequenceWriter writer = 
			      new GifSequenceWriter(output, firstImage.getType(), gifSpeed, true);
			
			    // write out the first image to our sequence...
			    writer.writeToSequence(firstImage);
			    for(int i=1; i<bi.length-1; i++) {
			    	if((i%1)==0){
				      BufferedImage nextImage = bi[i];
				      writer.writeToSequence(nextImage);
			    	}
//			      int percent=i;
//			      SwingUtilities.invokeLater(new Runnable() {
//		              public void run() {
//		            	  gifProgressBar.updateBar(percent);
//		              }
//			      });
			    }
			
			    writer.close();
			    output.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//////////////////////////////////////////////////////////////////////////
			System.out.println("Complete!");
			
			long difference = System.nanoTime() - startTime;
			System.out.println("Total execution time: " +
	                String.format("%d min, %d sec",
	                        TimeUnit.NANOSECONDS.toMinutes(difference),
	                        TimeUnit.NANOSECONDS.toSeconds(difference) -
	                                TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(difference))));
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));//Close progress bar
			//////////////////////////////////////////////////////////////////////////
			break;
			default: System.out.println("Invalid Dimension");
			break;
			}
			
			return image;
			
	}// end method
	public static int getMaxValue(int[] array) {
	    int maxValue = array[0];
	    for (int i = 1; i < array.length; i++) {
	        if (array[i] > maxValue) {
	            maxValue = array[i];
	        }
	    }
	    return maxValue;
	}
	public static int getMinValue(int[] array) {
	    int minValue = array[0];
	    for (int i = 1; i < array.length; i++) {
	        if (array[i] < minValue) {
	            minValue = array[i];
	        }
	    }
	    return minValue;
	}
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public static int paintLines(){
		return 1;
	}
	
	public static int paintGradient(){
		
		return 1;
	}
	
	public static Color paintClouds(int val, Color color){
		//invert
		if(val>0){
			val=val-val;
		}else if(val<=0){
			val=Math.abs(val)*2;
		}
		////////
		int red = val+60;
		if(red>255){
			red=255;
		}
		int green = val+120;
		if(green>255){
			green=255;
		}
		int blue = val+130;
		if(blue>255){
			blue=255;
		}
		////////
		val=Math.abs(val);
		color = new Color(val+60,val+120,val+130);
		///
		return color;
	}
	
	public static Color colorize(int val, int red, int green, int blue){
		//invert
		float brightness = 0.8f;
		int alpha = 50;
		red=(int) (red*brightness);
		green=(int) (green*brightness);
		blue=(int) (blue*brightness);
		if(val>0){
			val=val-val;
		}else if(val<=0){
			val=Math.abs(val)*2;
		}
		////////
		val=Math.abs(val);
		
		Color color = new Color(val+red, val+green, val+blue, alpha);
		///
		return color;
	}
	
}
