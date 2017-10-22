import java.awt.EventQueue;
import java.io.*;
import java.lang.String;
import java.util.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main extends JFrame {

  private static final long serialVersionUID = 1L;
  private JLabel main_label;
  private JTextField input_field;
  private JButton enter;
  
  Main() {
    initialize();
  }
  
  private void initialize() {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(400, 200, 600, 250);
    setTitle("Words Graph Of Context");
    setLayout(null);
    setResizable(false);

    main_label = new JLabel("Input the path of context");
    main_label.setBounds(50, 20, 500, 40);
    input_field = new JTextField();
    input_field.setBounds(100, 80, 400, 50);
    enter = new JButton("Enter");
    enter.setBounds(200, 180, 200, 50);

    enter.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        String path = input_field.getText();
        int n;
        try {
            FileReader fr = new FileReader(path);
            String input = new String();
            int ch = 0;
            try {
                while ((ch = fr.read()) != -1) {
                  if (ch == '\n' || ch==' '){
                    input += ' ';
                  }
                  else if ((ch >= 'a' && ch <='z') || (ch >='A' && ch <='Z')){
                  input += (char)ch;
                  }
                  else {
                  }
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            Graph<String> wordgraph = new Graph<String>();
            String[] str = input.toLowerCase().split(" ");
            ArrayList<String> get = new ArrayList<String>();
            Vector<String> count = new Vector<String>();
            HashMap<String,Vertex<String>> maptr = new HashMap<String,Vertex<String>>();
            for (int i = 0;i < str.length ; i ++) {
            if (str[i] == null || str[i].trim().equals("")) {
              }
            else {
              get.add(str[i]);
            }
          }
          Set<String> mm = new HashSet<String>();
          for(int x = 0;x<get.size();x++){
            System.out.println(get.get(x));
            if(mm.contains(get.get(x))) {
            }
            else {
              Vertex<String> t = new Vertex<String>(get.get(x));
              maptr.put(get.get(x),t);
              wordgraph.addVertex(maptr.get(get.get(x)));
              mm.add(get.get(x));
              }
            }
          for (int i = 0;i < get.size() - 1 ; i ++) {
              count.add(get.get(i)+"-"+get.get(i+1));
              }
          final HashMap<String,Integer> edge_cost = new HashMap<String,Integer>();
          while(!count.isEmpty()){
              String temp = count.remove(0);
              Set s = edge_cost.keySet();
              if(s.contains(temp)){
                  edge_cost.put(temp,edge_cost.get(temp)+1);
                }
              else{
                  edge_cost.put(temp,1);
                }
            }
          Iterator it = edge_cost.entrySet().iterator();
          while(it.hasNext()){
              HashMap.Entry pair = (HashMap.Entry)it.next();
              String temp = (String)pair.getKey();
              String[] now = temp.split("-");
              Integer cost = (Integer)pair.getValue();
              wordgraph.addEdge(maptr.get(now[0]),maptr.get(now[1]),cost);
              it.remove();
              }
          n = wordgraph.verticies.size();
          Data.numOfNodes = n;
          Data.segmentsGains = new double[n][n];
          Panel view = new Panel(wordgraph,maptr);
          view.setVisible(true);
          dispose();
          } catch (FileNotFoundException e) {
              e.printStackTrace();
              n = 0;
            }
        }
      });

    Font font = new Font("Serif", Font.PLAIN, 32);
    main_label.setFont(font);
    input_field.setFont(font);
    enter.setFont(font);

    getContentPane().add(main_label);
    getContentPane().add(input_field);
    getContentPane().add(enter);

  }
  
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Main view = new Main();
          view.setVisible(true);

            } catch (Exception e) {
              e.printStackTrace();
        }
      }
    });

  }
}