package perlin;

import java.util.Random;

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
	    new Panel(new Perlin(1024, 1024, 4, 2.5f, 0x70, 0.9875f, 6, seed)).show();
			//new Panel().show();
        int x=0;
        int y=0;
        int z=0;
        //ImprovedNoise
       // new ImprovedNoise().show();
	    }
}
