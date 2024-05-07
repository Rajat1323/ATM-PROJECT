package com.jsp.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/Transfer")
public class Transfer extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String receiver = req.getParameter("usermob");
		double amount=Double.parseDouble(req.getParameter("amount"));
		HttpSession session =req.getSession();
		String sender = (String)session.getAttribute("mobile");
		String url="jdbc:mysql://localhost:3306/teca39?user=root&password=12345";
		String check="select * from bank where mobilenumber=?";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url);
			PreparedStatement ps = connection.prepareStatement(check);
			ps.setString(1, receiver);
			//checking receiver address
			ResultSet rs = ps.executeQuery();
			PrintWriter writer = resp.getWriter();
			resp.setContentType("text/html");
			if(rs.next())
			{
				if(amount>0)
				{
					PreparedStatement ps1 = connection.prepareStatement(check);
					ps1.setString(1, sender);
					ResultSet rs1 = ps1.executeQuery();
					rs1.next();
					double balance = rs1.getDouble("amount");
					if(amount<=balance)
					{
						String send="update bank set amount=? where mobilenumber=?";
						String receive="update bank set amount=? where mobilenumber=?";
						PreparedStatement ps2 = connection.prepareStatement(send);
						ps2.setDouble(1, balance-amount);
						ps2.setString(2, sender);
						int num = ps2.executeUpdate();
						PreparedStatement ps3 = connection.prepareStatement(receive);
						ps3.setDouble(1,rs.getDouble("amount")+amount);
						ps3.setString(2, receiver);
						int num1 = ps3.executeUpdate();
						if(num>0 && num1>0)
						{
							writer.println("<center><h1 style=color:green>Mobile Transfer Successful!!!!!!</h1></center>");
						}
						else
						{
							RequestDispatcher rd= req.getRequestDispatcher("transfer.html");
							rd.include(req, resp);
							writer.println("<center><h1 style=color:red>Transation error</h1></center>");
						}
					}
					else
					{
						RequestDispatcher rd= req.getRequestDispatcher("transfer.html");
						rd.include(req, resp);
						writer.println("<center><h1 style=color:red>Transation failed</h1></center>");
					}
				}
				else
				{
					RequestDispatcher rd= req.getRequestDispatcher("transfer.html");
					rd.include(req, resp);
					writer.println("<center><h1 style=color:red>Invalid amount</h1></center>");
				}
			}
			else
			{
				RequestDispatcher rd= req.getRequestDispatcher("transfer.html");
				rd.include(req, resp);
				writer.println("<center><h1 style=color:red>Invalid Receiver Mobile Number</h1></center>");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
