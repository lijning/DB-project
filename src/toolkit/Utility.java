package toolkit;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import control.DBConnection;
import model.Course;
import model.SC;

/**
 * A bunch of tools.
 * Created on 2017/12/3.
 */

public class Utility {
    private Utility(){}
    public static void setWindowAtCenter(Window window){
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Dimension screenSize=toolkit.getScreenSize();
        window.setLocation((screenSize.width-window.getWidth())/2,(screenSize.height-window.getHeight())/2);
    }
    public static void reportErrorEmptyTable(){
        JOptionPane.showMessageDialog(null, "没有符合条件的记录，请检查查询条件","wrong",JOptionPane.ERROR_MESSAGE);;
    }

    /**
     * @param table the table to select from.
     * @param attr the attribute to select.
     * @return A vector of string.
     */
    public static Vector simpleUniqueQuery(String table, String attr){
        Vector vector=new Vector<String>();
        ResultSet resultSet=null;
        String sql="select distinct "+ attr+" from "+table;
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                vector.add(resultSet.getString(1));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error!simpleUniqueQuery!");
            e.printStackTrace();
        }finally {
            vector.add(null);
            return vector;
        }
    }
    public static JScrollPane getJSPfromResultSet(ResultSet rs){
        JScrollPane jsp=null;
        try {
            if(rs==null){
                System.out.println("Result set is null.");
                return null;
            }
            Table t=new Table(rs);
            jsp = new JScrollPane();
            jsp.setViewportView(t.jt);
            rs.close();
            return jsp;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("sql error in get JSP");
        }finally {
            return jsp;
        }
    }
    public static JScrollPane jspFromSQL(String sql){
        ResultSet rs=null;
        JScrollPane jsp=null;
        try{
            Statement statement = DBConnection.getConnection().createStatement();
            rs=statement.executeQuery(sql);
            jsp=getJSPfromResultSet(rs);
            rs.last();
            if(rs.getRow()==0)Utility.reportErrorEmptyTable();
            rs.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            jsp=new JScrollPane();
        }
        return jsp;
    }

    /**
     * @param s
     * @return 's'
     */
    public static String quote(String s){
        return "'"+s+"'";
    }
}
