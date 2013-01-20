package com.example;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		ServletOutputStream out = resp.getOutputStream();

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
		
		
		//Imprimindo o book "original"
		System.out.println(bookText);
		
		//Imprimindo todos os fields
		System.out.println("######### In�cio do print dos fields ######### \n");
		System.out.println(syntacticAnalyzer.getBook().toString());		
		System.out.println("\n######### T�rmino do print dos fields ######### \n\n");

		out.write("EXAMPLE OF THE API FUNCTIONALITY".getBytes());
		out.write("\nBOOK: ".getBytes());
		out.write("\n################################:\n".getBytes());
		out.write(bookText.getBytes());
		out.write("\n################################".getBytes());
		
		out.write("\n\n\nFIELDS PARSED: ".getBytes());
		out.write("\n################################:\n".getBytes());
		out.write(syntacticAnalyzer.getBook().toString().getBytes());
		out.write("\n################################".getBytes());
		
		out.flush();
		out.close();
	}
}
