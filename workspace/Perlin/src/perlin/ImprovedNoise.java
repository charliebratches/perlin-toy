package perlin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



//JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.

public final class ImprovedNoise {
public BufferedImage image;
static public double noise(double x, double y, double z) {
   int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
       Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
       Z = (int)Math.floor(z) & 255;
   x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
   y -= Math.floor(y);                                // OF POINT IN CUBE.
   z -= Math.floor(z);
   double u = fade(x),                                // COMPUTE FADE CURVES
          v = fade(y),                                // FOR EACH OF X,Y,Z.
          w = fade(z);
   int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
       B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

   return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
                                  grad(p[BA  ], x-1, y  , z   )), // BLENDED
                          lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
                                  grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
                  lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
                                  grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
                          lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                  grad(p[BB+1], x-1, y-1, z-1 ))));
}
static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
static double lerp(double t, double a, double b) { 
	//System.out.println(a + t * (b - a));
	return a + t * (b - a); 
	}
static double grad(int hash, double x, double y, double z) {
   int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
   double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
          v = h<4 ? y : h==12||h==14 ? x : z;
   return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
}
static final int p[] = new int[512], permutation[] = { 151,160,137,91,90,15,
131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
};
static { for (int i=0; i < 256 ; i++){ p[256+i] = p[i] = permutation[i];}}

public void show(int dimension, String mode){
	
	int width = 256;
	int height = 256;
	int depth = 256;
	int arr2D[][] = new int[width][height];
	int arr3D[][][] = new int [width][height][depth];
	
	//Initialize our main image
	
	image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	Graphics2D g = image.createGraphics();
	g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
	if(mode=="NORMAL") {
		System.out.println("Creating image in NORMAL mode.");
		//black & white
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
			double x = (double)i/255;
			double y = (double)j/255;
			int tmp = (int) (noise(x, y, 0.0)*255);
			//if (tmp < 0){ tmp = 0; }
			all[ctr]=tmp;
			//tmp=tmp+107;
			arr2D[i][j] = tmp;
			
			//System.out.print(arr2D[i][j]+" ");
			ctr++;
		}
		System.out.println("");
	}
	System.out.println("Successfully generated 2D perlin object.");
	List b = Arrays.asList(all);
	System.out.println("max: "+getMaxValue(all));
	System.out.println("min: "+getMinValue(all));
	int range=-getMinValue(all)+getMaxValue(all);
	System.out.println("range: "+range);
	//Fill each pixel in the image
	int alpha = Color.BLACK.getRGB();
	for (int i = 0; i<width; i++){
		for (int j = 0; j<height; j++){
			int val = arr2D[i][j];
			image.setRGB(i, j, (val << 16) + (val << 8) + val);
		}
	}
	break;
	///////////////////////////////////////////////////////////////////////////
	//case 3
	case 3://For a 3d perlin object
	
	int val = 0;
	for (int i = 0; i<width; i++){
		for (int j = 0; j<height; j++){
			for (int k = 0; k<depth; k++){
				double x = (double)i/255;
				double y = (double)j/255;
				double z = (double)k/255;
				int tmp = (int) (noise(x, y, z)*255);
				if (tmp < 0){ tmp = 0; }
				//tmp=tmp+255;
				arr3D[i][j][k] = tmp;

			}
		}

	}
	
	for (int k = 0; k<depth; k++){
		for (int i = 0; i<width; i++){
			for (int j = 0; j<height; j++){			
				val = arr3D[i][j][k];
				image.setRGB(i, j, (val << 16) + (val << 8) + val);		
			}
		}				
		g.drawImage(image, null, 0, 0 );
				
		bi[k] = copyImage(image);
		
	}//SCOPE END FOR LOOPS
	
	//////////////////////////////////////////////////////////////////////////
	//CREATE GIF
	// grab the output image type from the first image in the sequence
    BufferedImage firstImage = bi[0];

    // create a new BufferedOutputStream with the last argument
	ImageOutputStream output;
	try {
		output = new FileImageOutputStream(new File("output.gif"));
	    // create a gif sequence with the type of the first image, 1 second
	    // between frames, which loops continuously
	    GifSequenceWriter writer = 
	      new GifSequenceWriter(output, firstImage.getType(), 1, true);
	
	    // write out the first image to our sequence...
	    writer.writeToSequence(firstImage);
	    for(int i=1; i<bi.length-1; i++) {
	      BufferedImage nextImage = bi[i];
	      writer.writeToSequence(nextImage);
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
	break;
	default: System.out.println("Invalid Dimension");
	break;
	}
	
//	for (int i = 0; i<bi.length; i++) {
//		try { ImageIO.write(bi[i], "png", new File("gif_testing/giftest"+i+".png")); }
//		catch (IOException e) { e.printStackTrace(); }
//	}
	
	

	
	
	g.drawImage(image, null, 0, 0 );
	
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
public BufferedImage getImage(){
	return this.image;
}
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
}