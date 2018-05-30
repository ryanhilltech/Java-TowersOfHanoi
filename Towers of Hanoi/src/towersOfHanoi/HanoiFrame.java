package towersOfHanoi;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

// build the graphical presentation for tower

public class HanoiFrame 
{
	//window size
	public static final int FRAME_WIDTH=900;
	public static final int FRAME_HEIGHT=800;
	
	
	
	public static void main(String[]args)
	{
		
	HanoiEngine solver = new HanoiEngineImpl();
	
	HanoiViewImpl controller = new HanoiViewImpl(solver);
	
	JFrame frame = new JFrame("The Towers of Hanoi Solver");
	
	frame.setLayout(new FlowLayout());
	
	frame.addWindowListener( new WindowAdapter() {
		//close system
		public void windowClosing(WindowEvent windowEvent)
		{
			System.exit(0);
		}
	});
	frame.getContentPane().add(controller);
	frame.setSize(HanoiFrame.FRAME_WIDTH,HanoiFrame.FRAME_HEIGHT);
	frame.setVisible(true);
   }
}