
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class DbConnect {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    String password;
    Boolean status;
    public DbConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","");
            st=con.createStatement();//Create statement
            
        }catch(ClassNotFoundException | SQLException ex){
            System.out.println("Error: "+ex);
        }
    }
    
    public int getData(){
        try{
            
            String query ="select * from Project";
            rs =st.executeQuery(query);
            System.out.println("Records from database");
            while(rs.next()){
                 password=rs.getString("Password");
                //System.out.println("Password: "+password);
                
            }
            
        }catch(Exception ex){
            System.out.println("Error: "+ex);
        }
        if(password.length()>1) return 1;
        else return 0;
    }
    
    public String PasswordUpdate(String s){
        try{
            System.out.println(s);
            String query="UPDATE Project SET Password= ?";
            PreparedStatement ps = con.prepareStatement(query);
              ps.setString(1, s); 
            ps.executeUpdate();
        }catch(Exception ex){
            System.out.println("Error: "+ex);
        }
        return "";
    }
    
    public Boolean PasswordCheck(String s){
        System.out.println(status+password);
        try{
            String query ="select * from Project";
            rs =st.executeQuery(query);
            while(rs.next()){
                 password=rs.getString("Password");
            }
        }catch(Exception ex){
            System.out.println("Error: "+ex);
        }
            status=password.equals(s);
        return status;
    }
    
}
