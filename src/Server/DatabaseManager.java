package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author :al Created At:Dec 11, 2016
 */
public class DatabaseManager {

    private String url, uname, pw, LoginQuery, StudentQuery, InsertStudentQuery,InsertClientQuery;
    private Connection con;
    private PreparedStatement st;

    //   Class.forName("com.mysql.jdbc.Driver");
    public DatabaseManager() {
        url = "jdbc:mysql://localhost:3306/studentdatabase";
        uname = "root";
        pw = "";
        LoginQuery = "SELECT Password FROM `clients` WHERE UserName=?";
        StudentQuery = "SELECT * FROM `students` WHERE id=?";
        InsertStudentQuery = "INSERT INTO `students`(`id`, `name`, `department`, `semester`) VALUES (?,?,?,?)";
        InsertClientQuery="INSERT INTO `clients`(`UserName`, `Password`) VALUES (?,?)";
        LoadDriver();
    }

    public void LoadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public boolean LoginDB(String name,String password) throws SQLException {
   //     String givenName=name;
    //     System.out.print(name+" Length :"+name.length()+givenName+" Length: "+givenName.length() );
        String msg="";
        con = DriverManager.getConnection(url, uname, pw);
        st = con.prepareStatement(LoginQuery);
        st.setString(1, name);
       
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            msg= rs.getString(1);
            System.out.println(msg);
            if(password.equals(msg)){
                return  true;   
            }
            else{
                 return  false;
            }
                        
        } 
        else{System.out.println("Empty"); }
        st.close();
        con.close();
        return  false;
    }

       public ResultSet InfoDB(int id) throws SQLException {
        con = DriverManager.getConnection(url, uname, pw);
        st = con.prepareStatement(StudentQuery);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        st.close();
        con.close();
        return  rs;
    }
    public void InsertStudentDB(int id, String name, String dept, int sem) throws SQLException {
        con = DriverManager.getConnection(url, uname, pw);
        st = con.prepareStatement(InsertStudentQuery);
        st.setInt(1, id);
        st.setString(2, name);
        st.setString(3, dept);
        st.setInt(4, sem);
        int count = st.executeUpdate();
        st.close();
        con.close();
        if (count > 0) {
            System.out.println("Successfully Added Student To Database");
        } else {
            System.out.println("Couldn't Add Student To Database");
        }
    }
    public boolean InsertClientDB(String name, String password) throws SQLException {
        con = DriverManager.getConnection(url, uname, pw);
        st = con.prepareStatement(InsertClientQuery);
        st.setString(1, name);
        st.setString(2, password);
        int count = st.executeUpdate();
        st.close();
        con.close();
        if (count > 0) {
           
            System.out.println("Successfully Added New Client To Database");
             return true;
        } else {
            
            System.out.println("Couldn't Add Client To Database");
            return false;
        }
    }
}
