package com.jsp.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BalanceCheck")
public class BalanceCheck extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mobile = req.getParameter("usermob");
		String pin = req.getParameter("userpin");
		
		String url="jdbc:mysql://localhost:3306/teca39?user=root&password=12345";
		String select ="select * from bank where mobilenumber=? and pin=?";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url);
			PreparedStatement ps = connection.prepareStatement(select);
			ps.setString(1,mobile);
			ps.setString(2,pin);
			ResultSet rs = ps.executeQuery();
			PrintWriter writer = resp.getWriter();
			resp.setContentType("text/html");
			if(rs.next())
			{
				writer.println("<center><h1>Balance is : "+rs.getDouble("amount")+"</h1></center>");
			}
			else
			{
				RequestDispatcher rd= req.getRequestDispatcher("balancecheck.html");
				rd.include(req, resp);
				writer.println("<center><h1 style=color:red>Invalid details</h1></center>");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
