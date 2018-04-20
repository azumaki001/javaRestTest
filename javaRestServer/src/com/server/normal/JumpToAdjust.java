package com.server.normal;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JumpToAdjust extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet (HttpServletRequest req, HttpServletResponse res) {

		RequestDispatcher dispatcher = req.getRequestDispatcher("/adjust.html");
		try {
			dispatcher .forward(req, res);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
