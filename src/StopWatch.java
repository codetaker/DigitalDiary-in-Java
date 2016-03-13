package swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
class StopWatch implements ActionListener{
    JLabel jlab;
    long start;
    StopWatch(){
        JFrame jfrm=new JFrame("Stop Watch");
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(275,100);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JButton fir=new JButton("Start");
        JButton sec=new JButton("Stop");
        
        fir.addActionListener(this);
        sec.addActionListener(this);
        
        jfrm.add(fir);
        jfrm.add(sec);
        
        jlab=new JLabel("Press Start to begin timing");
        jfrm.add(jlab);
        jfrm.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        Calendar cal=Calendar.getInstance();
        if(ae.getActionCommand().equals("Start")){
            start=cal.getTimeInMillis();
            jlab.setText("StopWatch is Running....");
        }
        else
            jlab.setText("Elapsed time is"+(double)(cal.getTimeInMillis()-start)/1000);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
        public void run(){
            new StopWatch();
        }
    });
 }
}
