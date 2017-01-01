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

    private String url, uname, pw, LoginQuery, StudentQuery, InsertStudentQuery, InsertClientQuery,
            InsertLoggedInPortQuery, InsertLoggedInClientQuery, GetLoggedInPortQuery, GetLoggedInClientQuery,
            DeletePort,DeleteUser;
    private Connection con, con2, con3, con4, con5, con6, con7,con8,con9;
    private PreparedStatement st, st2, st3, st4, st5, st6, st7,st8,st9;

    //   Class.forName("com.mysql.jdbc.Driver");
    public DatabaseManager() {
        url = "jdbc:mysql://localhost:3306/studentdatabase";
        uname = "root";
        pw = "";
        LoginQuery = "SELECT Password FROM `clients` WHERE UserName=?";
        StudentQuery = "SELECT * FROM `students` WHERE id=?";
        InsertStudentQuery = "INSERT INTO `students`(`id`, `name`, `department`, `semester`) VALUES (?,?,?,?)";
        InsertClientQuery = "INSERT INTO `clients`(`UserName`, `Password`) VALUES (?,?)";
        InsertLoggedInPortQuery = "INSERT INTO `pc_logged_in`(`ip_address`, `ip_port`) VALUES (?,?)";
        InsertLoggedInClientQuery = "INSERT INTO `user_logged_in`(`name`)  VALUES (?)";
        GetLoggedInPortQuery = "SELECT * FROM `pc_logged_in` WHERE ip_address=? and ip_port=?";
        GetLoggedInClientQuery = "SELECT * FROM `user_logged_in` WHERE name=?";
        DeletePort="DELETE FROM `pc_logged_in` WHERE ip_address=? and ip_port=?";
        DeleteUser="DELETE FROM `user_logged_in` WHERE name=?";
        LoadDriver();
    }

    public void LoadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public boolean LoginDB(String name, String password) throws SQLException {
  
        String msg = "";
        con = DriverManager.getConnection(url, uname, pw);
        st = con.prepareStatement(LoginQuery);
        st.setString(1, name);

        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            msg = rs.getString(1);
            System.out.println(msg);
            if (password.equals(msg)) {
                st.close();
                con.close();
                return true;
            } else {
                st.close();
                con.close();
                return false;
            }
        } else {
//            System.out.println("Empty");
        }
        st.close();
        con.close();
        return false;
    }

    public String InfoDB(int id) throws SQLException {
        con2 = DriverManager.getConnection(url, uname, pw);
        st2 = con2.prepareStatement(StudentQuery);
        st2.setInt(1, id);
        ResultSet rs1 = st2.executeQuery();
        String msg;
        if (rs1.next()) {
            String name = rs1.getString(2);
            String dept = rs1.getString(3);
            int sem = rs1.getInt(4);
            msg = name + "," + dept + ":" + sem;
//            System.out.println("Student info: " + msg);

        } else {
            msg = "Empty";
//            System.out.println(msg);
        }
        st2.close();
        con2.close();
        return msg;
    }

    public String InsertStudentDB(int id, String name, String dept, int sem) throws SQLException {
        con3 = DriverManager.getConnection(url, uname, pw);
        st3 = con3.prepareStatement(InsertStudentQuery);
        st3.setInt(1, id);
        st3.setString(2, name);
        st3.setString(3, dept);
        st3.setInt(4, sem);
        int count = st3.executeUpdate();
        st3.close();
        con3.close();
        if (count > 0) {
            System.out.println("SERVER: Successfully Added Student To Database");
            return "true";
        } else {
            System.out.println("SERVER: Couldn't Add Student To Database");
            return "false";
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

            System.out.println("SERVER: Successfully Added New Client To Database");
            return true;
        } else {

            System.out.println("SERVER: Couldn't Add Client To Database");
            return false;
        }
    }

    public boolean isLoggedInIpPort(String ip, int port) throws SQLException {
        con6 = DriverManager.getConnection(url, uname, pw);
        st6 = con6.prepareStatement(GetLoggedInPortQuery);
        st6.setString(1, ip);
        st6.setInt(2, port);
        ResultSet rs1 = st6.executeQuery();
        String msg;
        if (rs1.next()) {
            st6.close();
            con6.close();
            System.out.println("SERVER: Already Logged In With This IP: "+ip+"and port: "+port);
            return true;

        } else {
            st6.close();
            con6.close();
            System.out.println("SERVER: Not Logged In With This IP: "+ip+"and port: "+port);
            return false;
        }

    }

    public boolean isLoggedInUser(String name) throws SQLException {
        con7 = DriverManager.getConnection(url, uname, pw);
        st7 = con7.prepareStatement(GetLoggedInClientQuery);
        st7.setString(1, name);
        ResultSet rs1 = st7.executeQuery();
        String msg;
        if (rs1.next()) {
            st7.close();
            con7.close();
            System.out.println("SERVER: Already Logged In With This name: "+name);
            return true;

        } else {
            st7.close();
            con7.close();
            System.out.println("SERVER:Not Logged In With This name: "+name);
            return false;
        }

    }

    public void InsertLoggedinIP_POrt(String ip, int port) throws SQLException {
        con4 = DriverManager.getConnection(url, uname, pw);
        st4 = con4.prepareStatement(InsertLoggedInPortQuery);
        st4.setString(1, ip);
        st4.setInt(2, port);
        int count = st4.executeUpdate();
        st4.close();
        con4.close();
        if (count > 0) {
            System.out.println("SERVER: Successfully Added ip,port as Logged In ip: "+ip+"port: "+port);
        } else {
            System.out.println("SERVER: Couldn't Add ip,port To Database ip: "+ip+"port: "+port);
        }
    }

    public void InsertUserAsLoggedIn(String name) throws SQLException {
        con5 = DriverManager.getConnection(url, uname, pw);
        st5 = con5.prepareStatement(InsertLoggedInClientQuery);
        st5.setString(1, name);
        int count = st5.executeUpdate();
        st5.close();
        con5.close();
        if (count > 0) {
            System.out.println("SERVER: Successfully Added user as Logged In with name: "+name);
        } else {
            System.out.println("SERVER: Couldn't Add user as logged in Database with name: "+name);
        }
    }
    public void deleteLoggedInInfo(String ip,int port,String name) throws SQLException{
    con8 = DriverManager.getConnection(url, uname, pw);
        st8 = con8.prepareStatement(DeleteUser);
        st8.setString(1, name);
        int count = st8.executeUpdate();
        st8.close();
        con8.close();
        if (count > 0) {
            System.out.println("SERVER: Successfully Removed user with name: "+name+"ip: "+ip+"port "+port);
        } else {
            System.out.println("SERVER: Couldn't Remove user with name: "+name+"ip: "+ip+"port "+port);
        }
    con9 = DriverManager.getConnection(url, uname, pw);
        st9 = con9.prepareStatement(DeletePort);
        st9.setString(1, ip);
        st9.setInt(2, port);
        count = st9.executeUpdate();
        st9.close();
        con9.close();
        if (count > 0) {
            System.out.println("SERVER: Successfully Removed ip,port ip:"+ip+"port: "+port);
        } else {
            System.out.println("SERVER: Couldn't Remove ip,port ip:"+ip+"port: "+port);
        }
    }
}
