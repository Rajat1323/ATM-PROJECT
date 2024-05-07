package com.jsp.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/NewUser")
public class NewUser extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("username");
		String mobilenumber=req.getParameter("usermob");
		String pin=req.getParameter("userpin");
		double amount=Double.parseDouble(req.getParameter("amount"));
		
		String url="jdbc:mysql://localhost:3306/teca39?user=root&password=12345";
		String insert ="insert into bank(name, mobilenumber, pin, amount) values(?,?,?,?)";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url);
			PreparedStatement ps = connection.prepareStatement(insert);
			ps.setString(1,name);
			ps.setString(2,mobilenumber);
			ps.setString(3, pin);
			ps.setDouble(4, amount);
			PrintWriter writer = resp.getWriter();
			resp.setContentType("text/html");
			if(amount>=0)
			{
				int num = ps.executeUpdate();
				if(num>0)
				{
					writer.println("<center><h1 style=color:green>Account created!!!!!!</h1></center>");
				}
				else
				{
					RequestDispatcher rd= req.getRequestDispatcher("newuser.html");
					rd.include(req, resp);
					writer.println("<center><h1 style=color:red>404 error</h1></center>");
				}
			}
			else
			{
				RequestDispatcher rd= req.getRequestDispatcher("newuser.html");
				rd.include(req, resp);
				writer.println("<center><h1 style=color:red>Invalid amount</h1></center>");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
