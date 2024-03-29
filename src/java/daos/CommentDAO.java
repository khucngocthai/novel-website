/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import dtos.Comment;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import utils.DBConnect;

/**
 *
 * @author chiuy
 */
public class CommentDAO {
    public LinkedList<Comment> searchCommentsByChapter(String novelID, String chapterID){
        LinkedList<Comment> lst = new LinkedList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Comment WHERE novelID=? AND chapterID=?";
        AccountDAO aDAO = new AccountDAO();
        ChapterDAO cDAO = new ChapterDAO();
        try {
            con = DBConnect.makeConnection();
            if(con != null){
                ps = con.prepareStatement(sql);
                ps.setString(1, novelID);
                ps.setString(2, chapterID);
                rs = ps.executeQuery();
                while(rs.next()){
                    String commentID = rs.getString("commentID");
                    String commentNovelID = rs.getString("novelID");
                    String commentChapterID = rs.getString("chapterID");
                    String username = rs.getString("username");
                    String context = rs.getString("context");
                    Date commentDate = rs.getDate("commentDate");
                    
                    Comment com = new Comment(commentID, cDAO.getChapterByChapterIDNovelID(commentNovelID, commentChapterID), aDAO.getAccountByUsername(username), context, (java.sql.Date) commentDate);
                    lst.add(com);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lst;
    }
    
    public LinkedList<Comment> getAllComments(){
           LinkedList<Comment> lst = new LinkedList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Comment";
        ChapterDAO cDAO = new ChapterDAO();
        AccountDAO aDAO = new AccountDAO();
        try {
            con = DBConnect.makeConnection();
            if(con != null){
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    String commentID = rs.getString("commentID");
                    String commentNovelID = rs.getString("novelID");
                    String commentChapterID = rs.getString("chapterID");
                    String username = rs.getString("username");
                    String context = rs.getString("context");
                    Date commentDate = rs.getDate("commentDate");
                    
                    Comment com = new Comment(commentID, cDAO.getChapterByChapterIDNovelID(commentNovelID, commentChapterID), aDAO.getAccountByUsername(username), context, (java.sql.Date) commentDate);
                    lst.add(com);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lst;
    }
    
    public boolean addComment(String username, String context, String chapterID, String novelID){
        Connection con = null;
        PreparedStatement ps = null;
        LinkedList<Comment> lst = this.getAllComments();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String commentID = "CM1";
        Date date = new Date();
        String commentDate = format.format(date);
        for (Comment comment : lst) {
            if(comment.getCommentID().equalsIgnoreCase(commentID)){
                commentID = "CM" + (Integer.parseInt(commentID.substring(2)) + 1);
            }
        }
        String sql = "INSERT INTO Comment(commentID, novelID, chapterID, username, context, commentDate)"
                + "VALUES(?, ?, ?, ?, ?, ?)";
        try {
            con = DBConnect.makeConnection();
            if(con != null){
                ps = con.prepareStatement(sql);
                ps.setString(1, commentID);
                ps.setString(2, novelID);
                ps.setString(3, chapterID);
                ps.setString(4, username);
                ps.setString(5, context);
                ps.setString(6, commentDate);
                
                ps.executeUpdate();
                return true;
            }
        } 
        catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(ps != null) ps.close();
                if(con != null) con.close();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    public boolean deleteComment(String commentID){
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM Comment WHERE commentID=?";
        try {
            con = DBConnect.makeConnection();
            if(con != null){
                ps = con.prepareStatement(sql);
                ps.setString(1, commentID);
                
                ps.executeUpdate();
                return true;
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(ps != null) ps.close();
                if(con != null) con.close();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
