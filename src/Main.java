
import javax.swing.SwingUtilities;


public class Main {

    public static void main(String[] args) {
        int t;
        String password="";
        DbConnect connect=new DbConnect();
        t=connect.getData();
        if(t==0) {
        SetPassword s=new SetPassword();       
        System.out.println(password+"In main"+t);
    }
        else{
            CheckPassword c=new CheckPassword();
        }
    }
}
