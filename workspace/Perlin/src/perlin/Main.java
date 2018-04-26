package perlin;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//import paint.MyPanel;

public class Main {
	
	public static void main(String[] args){
		/*Original Perlin:
		 * (1024, 1024, 4, 2.5f, 0x70, 0.9875f, 6)
		 */
		int max = 255;
		int min = 0;
		int seed = 128;
		Random rand = new Random(seed); //generate the seed once;
		seed = rand.nextInt((max-min)+1)+min;
		
        System.out.println(rand);
	    //new Panel(new Perlin(1024, 1024, 4, 2.5f, 0x70, 0.9875f, 6, seed)).show();
        
        ///////////////////////
        new Display().display();
        ///////////////////////
        // long startTime = System.nanoTime();
        
        //METHOD 1: NO GUI
        //ImprovedNoise noise = new ImprovedNoise();
        //ImagePainter ip = new ImagePainter();
		//BufferedImage image = ip.createImage(3, "LINES", 512, 512, 512, noise, 3, 1, 64, 4, 0, 16, 128, 128);
		//END METHOD 1
  
        ///////////////////////////////////////////
		//colorShiftAmount Options
		//17 = dark red
		//9 = electric yellow
		
		//System.out.println("Finished Execution");
//		long difference = System.nanoTime() - startTime;
//		System.out.println("Total execution time: " +
//                String.format("%d min, %d sec",
//                        TimeUnit.NANOSECONDS.toHours(difference),
//                        TimeUnit.NANOSECONDS.toSeconds(difference) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(difference))));

        int x=0;
        int y=0;
        int z=0;
        //ImprovedNoise
       // new ImprovedNoise().show(3, "LINES");
	    }
	
}
