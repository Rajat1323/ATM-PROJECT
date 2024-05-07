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

@WebServlet("/WithdrawAmount")
public class WithdrawAmount extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		double amount = Double.parseDouble(req.getParameter("amount"));
		PrintWriter writer = resp.getWriter();
		resp.setContentType("text/html");
		if(amount>0)
		{
			HttpSession session = req.getSession();
			String mobile = (String)session.getAttribute("mobile");
			String url="jdbc:mysql://localhost:3306/teca39?user=root&password=12345";
			String select="select * from bank where mobilenumber=?";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url);
				PreparedStatement ps1 = connection.prepareStatement(select);
				ps1.setString(1,mobile);
				ResultSet rs = ps1.executeQuery();
				rs.next();
				double balance =rs.getDouble("amount");
				if(amount<=balance)
				{
						String insert="update bank set amount=? where mobilenumber=?";
						PreparedStatement ps = connection.prepareStatement(insert);
						ps.setDouble(1, balance-amount);
						ps.setString(2, mobile);
						int num = ps.executeUpdate();
						if(num>0)
						{
							writer.println("<center><h1 style=color:green>Withdraw Successful!!!!!!</h1></center>");
						}
						else
						{
							RequestDispatcher rd= req.getRequestDispatcher("withdrawAmount.html");
							rd.include(req, resp);
							writer.println("<center><h1 style=color:red>404 error</h1></center>");
						}
				}
				else
				{
					RequestDispatcher rd= req.getRequestDispatcher("withdrawAmount.html");
					rd.include(req, resp);
					writer.println("<center><h1 style=color:red>Insufficient balance</h1></center>");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			RequestDispatcher rd= req.getRequestDispatcher("withdrawAmount.html");
			rd.include(req, resp);
			writer.println("<center><h1 style=color:red>Invalid amount</h1></center>");
		}
	}
}
