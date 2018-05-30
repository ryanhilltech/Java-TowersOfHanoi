package towersOfHanoi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HanoiViewImpl extends JPanel {

	//buttons set in frame
	
	private JButton startButton;//
	private JButton stopButton;//
	private JButton resetButton;//
	private JButton showSolution;
	private JPanel buttonPanel;//
	private JLabel solutionLabel;
	private JTextField solField;//
	private JComboBox ringBox;//
	private JComboBox speedBox;
	
	TowerPanel towerPanel;
    HanoiEngine solver;
    
    ArrayList<Integer> sourceTower;
    ArrayList<Integer> destTower;
    ArrayList<Integer> middleTower;
    private int numRings;
    private Thread hanoiThread;//
	private ChangeListener queensSolverListener;
    
	
	
	public HanoiViewImpl(HanoiEngine engine) {
      super();
	  this.setPreferredSize(new Dimension (HanoiFrame.FRAME_WIDTH-50, HanoiFrame.FRAME_HEIGHT-50));
	  this.solver=engine;
	  queensSolverListener = new ChangeListener()
		{ 
			public void stateChanged(ChangeEvent e){
				update();
			}
		};
		this.solver.addChangeListener(queensSolverListener);
		init();
	}
	public void init()
	{
		buttonPanel = new JPanel();
		this.setLayout(new BorderLayout());

		solutionLabel = new JLabel("Number of Solutions:");
		solField = new JTextField("0", 8);
		initComboBoxes();//
		initTowerPanel();
		initStartButton();//
		initStopButton();//
		initResetButton();//
		initHanoiSolver();//
		
		
		buttonPanel.setLayout(new GridLayout(1,8));//
		buttonPanel.add(startButton);//
		buttonPanel.add(stopButton);//
		buttonPanel.add(resetButton);//
		buttonPanel.add(ringBox);//
		buttonPanel.add(speedBox);
		buttonPanel.add(solutionLabel);//
		buttonPanel.add(solField);//
		this.add(BorderLayout.NORTH, buttonPanel);//
		this.add(BorderLayout.CENTER,  towerPanel);//
	}
	private void initComboBoxes() {
		//choose number of boxes
		String []rings = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		this.ringBox = new JComboBox(rings);
		this.ringBox.setSelectedIndex(2);
		
		//speed of solver
		String []speeds = {"Fast", "Medium", "Slow"};
		this.speedBox = new JComboBox(speeds);
		this.speedBox.setSelectedIndex(1);
		}
		
	private void initTowerPanel() {
		this.towerPanel = new TowerPanel();
		
	}
	
	//prepare solver
	private void initHanoiSolver() {
		this.solutionLabel = new JLabel("number of moves:");
		
		this.solField = new JTextField(""+solver.getNumberMoves(), 8);
		this.sourceTower = this.solver.getSourceTower();
		this.middleTower = this.solver.getMiddleTower();
		this.destTower = this.solver.getDestTower();
		int nRings = getNumberOfRings();
		this.towerPanel.initTowers(sourceTower, middleTower, destTower, nRings);
	}
	
	//reset solver
	private void initResetButton() {
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				solver.setStopFlag(true);
				int nRings = getNumberOfRings();
				solver.resetTowers(nRings);
				solField.setText(""+solver.getNumberMoves());
				
				towerPanel.repaint();
			}
		});
	}
	
	//solve solution
	private void initStopButton() {
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				solver.setStopFlag(true);
				towerPanel.repaint();
			}
		});
	}
	
	//start solution 
	private void initStartButton() {
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if(hanoiThread == null || !hanoiThread.isAlive())
				{
					hanoiThread = new Thread( new Runnable() {
						public void run() {
							int nRings = getNumberOfRings();
							solver.resetTowers(nRings);
							solver.setStopFlag(false);
							towerPanel.repaint();
							solver.solveTowers();
						}
					});
					hanoiThread.start();
				}
			}

		});
	}
	
	//method that handles a change event 
	public void update()
	{
		int index = this.speedBox.getSelectedIndex();
		int speed = (index+1) * 70;
		try {
			Thread.sleep(speed);
		}catch (InterruptedException e)
		{}
		this.solField.setText(""+ solver.getNumberMoves());
		towerPanel.repaint();
		
	}
	public int getNumberOfRings()
	{
		int nRings = Integer.parseInt((String)ringBox.getSelectedItem());
		return nRings;
	}


}
