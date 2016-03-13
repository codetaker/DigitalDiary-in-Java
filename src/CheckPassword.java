import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
public class CheckPassword extends JFrame{
    JButton jbtn;
    JLabel jlab,jlab1;
    JPasswordField jpswd;
    String password;
    Boolean status;
    public CheckPassword(){
        JFrame jfrm=new JFrame("Digital Diary");
        jfrm.setLayout(new FlowLayout());
        jfrm.setLocation(500, 200);
        jfrm.setSize(400,220);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
        
        jbtn=new JButton("LogIn");
        jlab=new JLabel("Welcome",JLabel.CENTER); jlab.setForeground(Color.red);jlab.setFont(new Font("Serif", Font.BOLD, 60));
        jlab1=new JLabel("Enter Password",JLabel.HORIZONTAL);jlab1.setFont(new Font("Courier New", Font.ITALIC, 20));
        jpswd=new JPasswordField(10);
        jpswd.setBounds(200,400,120,30);
        
        jbtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent le){
                char[] array=jpswd.getPassword();
                password=new String(array);
                DbConnect connect=new DbConnect();
                status=connect.PasswordCheck(password);
                if(status==true) {
                    JOptionPane.showMessageDialog(null, "Successfully LogIn");
                    jfrm.setVisible(false);
                    Editor e=new Editor();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong Password");
                    jpswd.setText("");
                }
            }
       });
        
        setLayout(null);
        //getContentPane().setBackground(Color.decode("#bdb67b"));
        jfrm.add(jlab);
        jfrm.add(jlab1);
        jfrm.add(jpswd);
        jfrm.add(jbtn);
        jfrm.setVisible(true); 
    }
   
    
   
}
