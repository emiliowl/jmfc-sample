package com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expert.adapter.MFBook;
import com.expert.jmfc.compiler.SyntacticAnalyzer;
import com.expert.jmfc.util.Conversor;

public class HelloServlet extends HttpServlet {

	/**
	 * Auto-generated by eclipse
	 */
	private static final long serialVersionUID = 4781779936081278961L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		out.print("<html>");
		out.print("<head>");
		out.print("<script src=\"/js/jquery.js\" type=\"text/javascript\"></script>");
		out.print("<script src=\"/js/jquery.cookie.js\" type=\"text/javascript\"></script>");
		out.print("<script src=\"/js/jquery.hotkeys.js\" type=\"text/javascript\"></script>");
		out.print("<script src=\"/js/jquery.jstree.js\" type=\"text/javascript\"></script>");
		out.print("<link type='text/css' rel='stylesheet' href=\"/css/!style.css\" />");
		out.print("</head>");

		SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer();
		String bookText = "";
		
		try {

			syntacticAnalyzer.validate("target" + File.separator
					+ "classes" + File.separator
					+ "SyntacticAnalyzerTesterBook.txt", "Book");
			
			 bookText = Conversor.fileToString("target" + File.separator
					+ "classes" + File.separator
					+ "SyntacticAnalyzerTesterBook.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out.print("<body>");
		out.print("<p class='note'>");
		out.print("EXAMPLE OF THE API FUNCTIONALITY");
		out.print("\nBOOK: ");
		out.print("\n################################:\n");
		out.print(bookText);
		out.print("\n################################");
		out.print("<p/>");
		
		out.print("<p class='note'>");
		out.print("\n\n\nFIELDS PARSED: ");
		out.print("\n################################:\n");
		out.print(syntacticAnalyzer.getBook().toString());
		out.print("\n################################");
		out.print("<p/>");
		
		out.print("<div id=\"demo1\" class=\"demo jstree jstree-0 jstree-default jstree-focused\"></div>");
		String bookData = new MFBook(syntacticAnalyzer.getBook()).toXml();
		String jsFunction = "<script type='text/javascript' class='source'>" + 
				"$(function () {   " +
				"$('#demo1').jstree({" +
				"\"xml_data\" : {" +
				"\"data\" : \"" + bookData + "\", \"xsl\" : \"nest\"}," +
				"\"plugins\" : [ \"themes\", \"xml_data\" ]" +	
				"});" +
				"});" +
				"</script>";
		
		out.print(jsFunction);
		out.print("</body>");
		out.print("</html>");
		out.flush();
		out.close();
	}
}
