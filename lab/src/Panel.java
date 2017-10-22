import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JFrame {

  private static final long serialVersionUID = 1L;
  private JLabel seg_start_label, seg_end_label, seg_gain_label;
  private  JTextField seg_start_text, seg_end_text, seg_gain_text;
  private  JButton solve_button, next_button;
  private  JButton random_go,shortest_path,change_text;
  private JTextField pathof;
  private JPanel drawPanel;
  private int width, height;
  Graph<String> mygraph;
  HashMap<String,Vertex<String>> mapt;
  HashMap<String,Integer> vton;

  Panel(Graph<String> gr,HashMap<String,Vertex<String>> mmp) {
    initialize();
    mygraph = gr;
		mapt = mmp;
		vton = new HashMap<String,Integer>();
		for(int i = 0;i<mygraph.verticies.size();i++){
			vton.put(mygraph.verticies.get(i).getName(), i+1);
		}
		try {  
            File file = new File("word-number");  
            PrintStream ps = new PrintStream(new FileOutputStream(file));  
            ps.println(vton.toString());  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
      e.printStackTrace();  
    }  
  }

  private void initialize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    width = (int) screenSize.getWidth() - 120;
    height = (int) screenSize.getHeight() - 120;
    Data.width = width;
    Data.height = height;
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(0, 0, width, height);
    setTitle("Word Graph plot");
    setLayout(null);
    setResizable(true);

    seg_start_label = new JLabel("from node #");
    seg_start_label.setBounds(60, height - 120, 160, 50);
    seg_start_text = new JTextField();
    seg_start_text.setBounds(60, height - 60, 160, 50);

    seg_end_label = new JLabel("to node #");
    seg_end_label.setBounds(240, height - 120, 160, 50);
    seg_end_text = new JTextField();
    seg_end_text.setBounds(220, height - 60, 160, 50);

    seg_gain_label = new JLabel("bridge_word");
    seg_gain_label.setBounds(420, height - 120, 360, 50);
    seg_gain_text = new JTextField();
    seg_gain_text.setBounds(380, height - 60, 360, 50);

    drawPanel = new Draw();
    drawPanel.setBounds(0, 0, width, height - 120);
    drawPanel.setBackground(Color.WHITE);

    next_button = new JButton("plot words");
    next_button.setBounds(760, height - 120, 250, 50);
    solve_button = new JButton("check bridge");
    solve_button.setBounds(760, height - 60, 250, 50);
    
    random_go = new JButton("random_go");
    random_go.setBounds(1100,height - 60,250,50);
    shortest_path = new JButton("shortest_path");
    shortest_path.setBounds(1100,height - 120,250,50);
    change_text = new JButton("change_text");
    change_text.setBounds(1400,height - 120,250,50);
    pathof = new JTextField();
    pathof.setBounds(1400,height - 60,250,50);


    solve_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
    	  String start = seg_start_text.getText();
				String end = seg_end_text.getText();
				ArrayList<String> result = mygraph.bridgeword(mapt.get(start),mapt.get(end));
				String out  = "";
				if(result.size()!=0){
					for(int i = 0;i<result.size();i++){
						out =result.get(i) + " ";
					}
				}
				else{
					out = "NULL";
				}
				seg_gain_text.setText(out);
				
			}
		});
		next_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				   for (int i = 0;i<mygraph.edges.size();i++){
						  Edge<String> temp = mygraph.edges.get(i);
						  int n1 = vton.get(temp.getFrom().getName());
						  int n2 = vton.get(temp.getTo().getName());
							double g = temp.getCost();
							Data.segmentsGains[n1 - 1][n2 - 1] = g;
							drawPanel.repaint();
					   }
				   }
		});
		
		change_text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pathname = pathof.getText();
				 String context = "";
				 String out = "";
			     try {
				FileReader fr = new FileReader(pathname);
			     int ch = 0;
					while((ch = fr.read())!=-1){
					  if (ch == '\n' || ch==' '){
					    context += ' ';
					  }
					  else if ((ch >= 'a' && ch <='z') || (ch >='A' && ch <='Z')){
					      context += (char)ch;
					  }
					  else {
					  }
					 }
			     } catch (IOException e) {
						e.printStackTrace();
					}
				   Random rand = new Random();
				     String[] seq = context.split(" ");
				     ArrayList<String> seqlist = new ArrayList<String>();
				     for(int i =0;i<seq.length-1;i++){
				       ArrayList<String> ss = mygraph.bridgeword(mapt.get(seq[i]),mapt.get(seq[i+1]));
				       if(!ss.isEmpty()){
				         seqlist.add(seq[i]);
				         seqlist.add(ss.get(rand.nextInt(ss.size())));
				       }
				       else{
				         seqlist.add(seq[i]);
				       }
				     }
				     seqlist.add(seq[seq.length]);

				   for(int i =0;i<seqlist.size();i++){
				     out = out+seqlist.get(i)+" "; 
				   }
			        try {  
			            File file = new File("changed");  
			            PrintStream ps = new PrintStream(new FileOutputStream(file));  
			            ps.println(out);  
			        } catch (FileNotFoundException e) {  
			            // TODO Auto-generated catch block  
			            e.printStackTrace();  
			        }  
		}
			});
		
		random_go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String begin = seg_start_text.getText();
		        try {  
		            File file = new File("rand_go");  
		            PrintStream ps = new PrintStream(new FileOutputStream(file));  
		            ps.println(mygraph.rand_go(mapt.get(begin)));  
		        } catch (FileNotFoundException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
			}
		});
		
		shortest_path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String begin = seg_start_text.getText();
				String end = seg_end_text.getText();
				String out = "";
				 Shortestpath<String> ss = new Shortestpath<String>(mapt.get(begin),mygraph);
				 if(!end.equals("")){
				     LinkedList<Vertex<String>> path = ss.getpath(mapt.get(end));
				     for (int i = 0;i<path.size();i++){
				    	 out = out+path.get(i).getName()+"->";
				     }
				     seg_gain_text.setText(out);
				 }
				 else{
					 for(int i = 0;i<mygraph.verticies.size();i++){
						 LinkedList<Vertex<String>> path = ss.getpath(mapt.get(mygraph.verticies.get(i).getName()));
			            if(path != null){
							 for (int x = 0;x<path.size();x++){
						    	 out = out+path.get(x).getName()+"->";
						     }
							 out += "\n";
			            }
					 }
				        try {  
				            File file = new File("shortest_path");  
				            PrintStream ps = new PrintStream(new FileOutputStream(file));  
				            ps.println(out);  
				            seg_gain_text.setText("SEE FILE");
				        } catch (FileNotFoundException e) {  
				            // TODO Auto-generated catch block  
				            e.printStackTrace();  
				        }  
				        
				 }
			}
		});
		
		


    Font font = new Font("Serif", Font.PLAIN, 24);
    seg_start_label.setFont(font);
    seg_end_label.setFont(font);
    seg_gain_label.setFont(font);
    seg_start_text.setFont(font);
    seg_end_text.setFont(font);
    seg_gain_text.setFont(font);
    next_button.setFont(font);
    solve_button.setFont(font);
    random_go.setFont(font);
    shortest_path.setFont(font);
    change_text.setFont(font);
    pathof.setFont(font);

    getContentPane().add(seg_start_label);
    getContentPane().add(seg_end_label);
    getContentPane().add(seg_gain_label);
    getContentPane().add(seg_start_text);
    getContentPane().add(seg_end_text);
    getContentPane().add(seg_gain_text);
    getContentPane().add(drawPanel);
    getContentPane().add(solve_button);
    getContentPane().add(next_button);
    getContentPane().add(random_go);
    getContentPane().add(shortest_path);
    getContentPane().add(change_text);
    getContentPane().add(pathof);
  }
}
