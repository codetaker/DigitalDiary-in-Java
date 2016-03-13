package swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;


public class Reminder implements ActionListener{
    JLabel jlab;
    JTextField jtf;
    double start,end;
    Timer timer;
    JFrame jfrm;
  
    Reminder(){
        jfrm=new JFrame("Reminder");
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(250,120);//Set container Size
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jtf=new JTextField(2);
        jtf.setActionCommand("Stat");
        jtf.addActionListener(this);
        jfrm.add(jtf);
        
        JButton fir=new JButton("Start");
        fir.addActionListener(this);
        jfrm.add(fir);
        
        jlab=new JLabel("Enter time");
        jlab.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        jlab.setBorder(BorderFactory.createEtchedBorder(Color.blue, Color.green));
        jfrm.add(jlab);
        jfrm.setVisible(true);
    }

    class RemindTask extends TimerTask {
        public void run() {
            jlab.setText("Time's Up");
            jlab.setBorder(BorderFactory.createLineBorder(Color.RED));
            timer.cancel(); 
        }
    }
    
     public void Remind(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds*1000);
    }
    
    public void actionPerformed(ActionEvent ae){
       Calendar cal=Calendar.getInstance();
        if(ae.getActionCommand().equals("Start")){ 
            String t;
            t=jtf.getText();
            int k=Integer.parseInt(t);
            Remind(k);
           
        }    
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
        public void run(){
            new Reminder();
            
        }
    });
 }
}
