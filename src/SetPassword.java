import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
public class SetPassword extends JFrame{
    JButton jbtn;
    JLabel jlab,jlab1;
    JPasswordField jpswd;
    String password;
    public SetPassword(){
        JFrame jfrm=new JFrame("Digital Diary");
        jfrm.setLayout(new FlowLayout());
        jfrm.setLocation(500, 200);
        jfrm.setSize(400,220);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
        
        jbtn=new JButton("LogIn");
        jlab=new JLabel("Welcome",JLabel.CENTER); jlab.setForeground(Color.red);jlab.setFont(new Font("Serif", Font.BOLD, 60));
      
        jlab1=new JLabel("First time Set Password",JLabel.HORIZONTAL);jlab1.setFont(new Font("Courier New", Font.ITALIC, 12));
        jpswd=new JPasswordField(10);
        jpswd.setBounds(200,400,120,30);
        
        jbtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent le){
                char[] array=jpswd.getPassword();
                password=new String(array);
                DbConnect connect=new DbConnect();
                connect.PasswordUpdate(password);
                JOptionPane.showMessageDialog(null, "Password Set");
                Editor e=new Editor();
            }
       });
        
        setLayout(null);
        jfrm.add(jlab);
        jfrm.add(jlab1);
        jfrm.add(jpswd);
        jfrm.add(jbtn);
        jfrm.setVisible(true); 
    }
    
}
