package perlin;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Perlin extends JPanel
{
	public static int newSeed=new Random().nextInt(255);	static int counter = 1;
	private BufferedImage image;
	private int section;
	private BufferedImage[] imageSections;
	private char[] remap;
	public int strange1;
	public int strange2;
	public String mode = "TERRAIN";
	public static boolean seedBool = true;
	//public static int seed;
	
	public void setStrange(){
		strange1 = 8;
		strange2 = 16;
		ImageAdjuster imadj = new ImageAdjuster(this, strange1, strange2);
	}
	
	public int getStrange1(){
		return strange1;
	}
	
	public int getStrange2(){
		return strange2;
	}

	/*Output to .png file*/
	public void savePerlin()
	{	
//		try
//		{
//			ImageIO.write(this.image, "png", new File("test.png"));
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public BufferedImage getImage(){
		return this.image;
	}
	/**
	 * 
	 * Builds a Cloud texture map with Perlin noise
	 *
	 * 
	 * 
	 * @param width
	 * 
	 * @param height
	 * 
	 * @param initFreq
	 * 
	 * @param persistency
	 * 
	 * @param decay
	 * 
	 * @param detail
	 * 
	 */
	public Perlin(int width, int height, int initFreq, float persistency,
			int density, float cloudSharpness, int detail, int seed)
	{
		long start = System.currentTimeMillis();
		//System.out.println("Unique Perlin #"+counter++);
		//System.out.println("Generating lookup table");
		// generate a re-mapping lookup table
		if (density < 0)
		{
			density = 0;
		}
		else if (density > 255)
		{
			density = 255;
		}
		if (cloudSharpness < 0.0f)
		{
			cloudSharpness = 0.0f;
		}
		else if (cloudSharpness > 1.0f)
		{
			cloudSharpness = 1.0f;
		}
		remap = new char[255];
		for (int i = 0; i < 255; i++)
		{
			remap[i] = (char) (density - i);
			if (remap[i] < 0 || remap[i] > 255)
			{
				remap[i] = (char) 0;
			}
			remap[i] = (char) (255 - (Math.pow(cloudSharpness, remap[i]) * 255));
		}
		//System.out.println(System.currentTimeMillis() - start);
		//System.out.println("Generating noise maps and combining...");
		start = System.currentTimeMillis();
		// time to generate the 2D noise functions
		Noise2D[] noiseMaps = new Noise2D[detail];
		float amplitude = 1.0f;
		for (int i = 0; i < detail; i++)
		{
			noiseMaps[i] = new Noise2D(width, height, initFreq, initFreq,
					amplitude);
			noiseMaps[i].start();
			amplitude /= persistency;
			initFreq *= 2;
		}
		// initialize our main image
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
		// as thread finish, blend them in
		boolean keepRunning = true;
		while (keepRunning)
		{
			keepRunning = false;
			for (int i = 0; i < noiseMaps.length; i++)
			{
				if (noiseMaps[i] != null)
				{
					// check to see if we can extract data
					if (!noiseMaps[i].isAlive())
					{
						// we can get the data
						g.drawImage(noiseMaps[i].image, null, 0, 0);
						// allow Java to garbage collect the thread
						noiseMaps[i] = null;
					}
					else
					{
						keepRunning = true;
					}
				}
			}
//			try
//			{
//				Thread.sleep(15);
//			}
//			catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		g.dispose();
		//System.out.println(System.currentTimeMillis() - start);
		//System.out.println("Adjusting image");
		start = System.currentTimeMillis();
		// split the image up into sections and re-map the image
		adjustImage(Runtime.getRuntime().availableProcessors(), image
				.getWidth()
				* image.getHeight() / 0x40000);
		//System.out.println(System.currentTimeMillis() - start);
	}
	/**
	 * 
	 * Adjusts the image using the LUT
	 *
	 * 
	 * 
	 * @param threadCount
	 * 
	 * @param sectionsCount
	 * 
	 */
	public void adjustImage(int threadCount, int sectionsCount)
	{
		if (sectionsCount == 0)
		{
			// need at least one section
			sectionsCount = 1;
		}
		if (sectionsCount < threadCount)
		{
			// split up into more sections
			sectionsCount = threadCount;
		}
		ImageAdjuster[] threads = new ImageAdjuster[threadCount];
		imageSections = new BufferedImage[sectionsCount];
		for (int i = 0; i < sectionsCount; i++)
		{
			int sectionStart = i * image.getHeight() / sectionsCount;
			if (i + 1 == sectionsCount)
			{
				// last section
				imageSections[i] = image.getSubimage(0, sectionStart, image
						.getWidth(), image.getHeight() - sectionStart);
			}
			else
			{
				imageSections[i] = image.getSubimage(0, sectionStart, image
						.getWidth(), image.getHeight() / sectionsCount);
			}
		}
		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new ImageAdjuster(this, getStrange1(), getStrange2());
			threads[i].start();
		}
		// sleep this thread until done re-mapping image
		boolean keepRunning = true;
		while (keepRunning)
		{
			keepRunning = false;
			for (int i = 0; i < threads.length; i++)
			{
				if (threads[i].isAlive())
				{
					keepRunning = true;
				}
			}
//			try
//			{
//				Thread.sleep(30);
//			}
//			catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	/**
	 * 
	 * helper method to give ImageAdjuster threads new image sections to adjust
	 *
	 * 
	 * 
	 * @param thread
	 * 
	 * @return
	 * 
	 */
	private synchronized BufferedImage requestNextSection(ImageAdjuster thread)
	{
		// divide the image up into 64 scan-lines
		if (section == imageSections.length)
		{
			return null;
		}
		BufferedImage toReturn = imageSections[section];
		imageSections[section] = null;
		section++;
		return toReturn;
	}
	/**
	 * 
	 * Internal class used by Perlin to adjust the image using the remap LUT
	 *
	 * 
	 * 
	 * 
	 */
	public class ImageAdjuster extends Thread
	{
		public BufferedImage image;
		public BufferedImage nextImage;
		private Perlin owner;
		public int strange1;
		public int strange2;
		public ImageAdjuster(Perlin owner, int strange1, int strange2)
		{
			this.owner = owner;
			this.strange1 = strange1;
			this.strange2 = strange2;
		}

		@Override
		public void run()
		{
			// get an initial image
			image = owner.requestNextSection(this);
			//nextImage = owner.requestNextSection(this);
			
			//System.out.println("Image Width: "+image.getWidth());
			//System.out.println("Image Height"+image.getHeight());
			//colors
			Color oceanBlue = new Color(35, 45, 100); // Color oceans
			Color pebble = new Color(255, 230, 163); // Color sand
			Color snow = new Color(230, 250, 255); //color snow
			int rgb_white=(Color.WHITE).getRGB();
			int rgb_gray=(Color.GRAY).getRGB();
			int rgb_red=(Color.RED).getRGB();
			int rgb_green=(Color.GREEN).getRGB();
			//image.setRGB(i, j, 0); //NORMAL
			
			int rgb = oceanBlue.getRGB();
			int lineCounter = 0;
			while (image != null)
			{
				// work 1 (NORMAL)
				for (int i = 0; i < image.getWidth(); i++)
				{
					for (int j = 0; j < image.getHeight(); j++)
					{
						
						char val = (char) (image.getRGB(i, j) & 0xFF); //NORMAL
						Color color = new Color(image.getRGB(i, j));
						int alpha = color.getRed();
						//char val = (char) (image.getRGB(i, j) & 230); //This looks like topographic lines
						//System.out.println("Value for "+i+", "+j+": "+val);
						
						//int thisPix = (val << 16 )+(val << 8)+val;
						//System.out.println(thisPix);
						val = owner.remap[val];
						
						if(mode.equals("NORMAL")){
							image.setRGB(i, j, (val << 16) + (val << 8) + val);
						}else 
						if(mode.equals("TERRAIN")){
//							
//							//char val = (char) (image.getRGB(i, j) & 230); //This looks like topographic lines
//							//System.out.println("Value for "+i+", "+j+": "+val);
//							
//							int thisPix = (val << 16 )+(val << 8)+val;
//							//System.out.println(thisPix);
//							val = owner.remap[val];
//							image.setRGB(i, j, (val << 16) + (0xFF00) + val); //NORMAL
//							
//							if(((val << 16 )+(val << 8)+val)<2000000){
//								//image.setRGB(i, j, oceanBlue.getRGB()/2);
//								image.setRGB(i, j, pebble.getRGB());
//							}
//							if(((val << 16 )+(val << 8)+val)<128){
//								//image.setRGB(i, j, oceanBlue.getRGB()/2);
//								image.setRGB(i, j, oceanBlue.getRGB());
//								//image.setRGB(i, j, (val << 16) + (0xFF) + val); //NORMAL
//							}		
						}else
						if(mode.equals("LINES")){
							image.setRGB(i, j, 0);//make it black
							if((j%2)==0){
								if(j <= 128 && j > 0){
									int offset = (alpha+1)/2;
									if(offset < 128){
										image.setRGB(i, offset, rgb_red);
									}
									else if(offset >= 128){
										int newOffset=offset-128;
										//System.out.println("Mapping white pixel to "+i+", "+newOffset+"");
										image.setRGB(i, newOffset,  rgb_white);
									}
								}
							}
						}			
						//lineCounter=lineCounter+5;
						//image.setRGB(i, j, (val << 16) + (val << 8) + val); //NORMAL
						
						//image.setRGB(i, j, (val << 9) + (val << 17) + val);//SANDY AND BLUE
						//image.setRGB(i, j, (val << 11) + (val << 9)+val);//GREEN AND BLUE
						
						//System.out.println(strange1+" "+strange2);
						//image.setRGB(i, j, val+190); //This line generates OCEANS (kinda)
					}
				}
				// work 2 (COLORS)
				if(mode.equals("TERRAIN")){
					for (int i = 0; i < image.getWidth(); i++)
					{
						for (int j = 0; j < image.getHeight(); j++)
						{
							char val = (char) (image.getRGB(i, j) & 0xFF); //NORMAL
							Color color = new Color(image.getRGB(i, j));
							//char val = (char) (image.getRGB(i, j) & 230); //This looks like topographic lines
							//System.out.println("Value for "+i+", "+j+": "+val);
							
							int thisPix = (val << 16 )+(val << 8)+val;
							//System.out.println(thisPix);
							val = owner.remap[val];
							image.setRGB(i, j, (val << 16) + (0x008000) + val); //NORMAL
							
							if(((val << 16 )+(val << 8)+val)<2000000){
								//image.setRGB(i, j, oceanBlue.getRGB()/2);
								image.setRGB(i, j, pebble.getRGB());
							}
							if(((val << 16 )+(val << 8)+val)<128){
								//image.setRGB(i, j, oceanBlue.getRGB()/2);
								image.setRGB(i, j, oceanBlue.getRGB());
								//image.setRGB(i, j, (val << 16) + (0xFF) + val); //NORMAL
							}
							
							
							//image.setRGB(i, j, (val << 9) + (val << 17) + val);//SANDY AND BLUE
							//image.setRGB(i, j, (val << 11) + (val << 9)+val);//GREEN AND BLUE
							
							//System.out.println(strange1+" "+strange2);
							//image.setRGB(i, j, val+190); //This line generates OCEANS (kinda)
						}
					}
				}
				
//				if(mode=="LINES"){
//					image=nextImage;
//				}else
				image = owner.requestNextSection(this);
			}
			//////////////////////////////////////////////////////////////////////////////////
			//COLORIZE
//			
//			{
//				// work
//				for (int i = 0; i < image.getWidth(); i++)
//				{
//					for (int j = 0; j < image.getHeight(); j++)
//					{
//						char val = (char) (image.getRGB(i, j) & 0xFF); //NORMAL
//						
//						val = owner.remap[val];
//						System.out.println("Value for "+i+", "+j+": "+val);
//						if(val < 255){
//							image.setRGB(i,j, rgb); //NORMAL
//						}
//
//					}
//				}
//			image = owner.requestNextSection(this);
//			}
			//////////////////////////////////////////////////////////////////////////////////
		}
	}
	private static class Noise2D extends Thread
	{
		public int newSeed = 7777; //BREAKTHROUGH! if i iterate this by one, I can create the illusion of movement
		//public int newSeed=new Random().nextInt(255);
		BufferedImage image;
		private int width;
		private int height;
		private int freqX;
		private float alpha;
		private int freqY;
		public Noise2D(int width, int height, int freqX, int freqY, float alpha)
		{
			this.width = width;
			this.height = height;
			this.freqX = freqX;
			this.freqY = freqY;
			this.alpha = alpha;
		}
		@Override
		public void run()
		{
			BufferedImage temp = new BufferedImage(freqX, freqY,
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = temp.createGraphics();
			// generate a low-res random image
			int count=0;
			int max = 255;
			int min = 0;
			
			Random rand = new Random(newSeed); //generate the seed once;
			newSeed = rand.nextInt((max-min)+1)+min;
			
			
			///////////////////////
//			if(seedBool==true){
//				System.out.println("SEED:"+newSeed);
//			}
			for (int i = 0; i < freqX; i++)
			{
				for (int j = 0; j < freqY; j++)
				{
					///////////////////////
					int val = 0;
					if(seedBool==false){
						val = new Random().nextInt(255);
					}else
					if(seedBool==true){
						val = new Random(newSeed+count).nextInt(255);
					}
					//System.out.println(seed);
					g.setColor(new Color(val, val, val, (int) (alpha * 0xFF)));
					//g.setColor(Color.GREEN);
					

					g.fillRect(i, j, 1, 1);
					count++;
				}
			}
			g.dispose();
			// re-scale the image up using interpolation (in this case, linear)
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_4BYTE_ABGR);
			g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(temp, 0, 0, width, height, 0, 0, freqX, freqY, null);
			g.dispose();
		}
	}
}