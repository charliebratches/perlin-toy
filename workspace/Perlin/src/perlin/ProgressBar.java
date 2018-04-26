package perlin;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class ProgressBar extends JPanel {
 
  JProgressBar pbar;
 // static final int MY_MINIMUM=0;
 //static final int MY_MAXIMUM=100;
 
  public ProgressBar(int min, int max) {
     pbar = new JProgressBar();
     pbar.setMinimum(min);
     pbar.setMaximum(max);
     pbar.setStringPainted(true);
     add(pbar);
  }
 
  public void updateBar(int newValue) {
    pbar.setValue(newValue);
  }
 
}