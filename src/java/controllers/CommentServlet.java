/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.CommentDAO;
import dtos.Account;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author chiuy
 */
@WebServlet(name = "CommentServlet", urlPatterns = {"/CommentServlet"})
public class CommentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("a");
        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect("LoginServlet");
        } else {
            if (action == null) {
                CommentDAO cmDAO = new CommentDAO();
                Account user = (Account) session.getAttribute("user");
                String chapterID = request.getParameter("chapterID");
                String novelID = request.getParameter("novelID");
                String context = request.getParameter("context");
                context = new String(context.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                cmDAO.addComment(user.getUsername(), context, chapterID, novelID);
                response.sendRedirect("NovelServlet?a=read&n=" + novelID + "&c=" + chapterID);
//            out.print(context);
            } else if (action.equals("delete")) {
                CommentDAO cmDAO = new CommentDAO();
                String commentID = request.getParameter("cmid");
                String novelID = request.getParameter("nid");
                String chapterID = request.getParameter("cid");
                cmDAO.deleteComment(commentID);
                response.sendRedirect("NovelServlet?a=read&n=" + novelID + "&c=" + chapterID);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
