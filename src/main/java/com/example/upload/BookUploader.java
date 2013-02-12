package com.example.upload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.expert.adapter.MFBook;
import com.expert.jmfc.compiler.SyntacticAnalyzer;

public class BookUploader extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8713150470077228784L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart) {

			try {

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				@SuppressWarnings("unchecked")
				List<FileItem> items = (List<FileItem>) upload.parseRequest(req);
				for (FileItem item : items) {
					if (item.isFormField()) {
						// se for um campo normal de form
						resp.getWriter().println("<br/>Form field<br/>");
						resp.getWriter().println("<br/>Name:" + item.getFieldName());
						resp.getWriter().println("<br/>Value:" + item.getString());
					} else {
						// caso seja um campo do tipo file
						resp.getWriter().println("<br/>NOT Form field");
						resp.getWriter().println("<br/>Name:" + item.getFieldName());
						resp.getWriter().println("<br/>FileName:" + item.getName());
						resp.getWriter().println("<br/>Size:" + item.getSize());
						resp.getWriter().println("<br/>ContentType:" + item.getContentType());
						
						resp.getWriter().println("<br/><br/>EXAMPLE OF THE API FUNCTIONALITY<br/>");
						resp.getWriter().println("<br/>BOOK: ");
						resp.getWriter().println("<br/>################################:<br/>");
						resp.getWriter().println(item.getString().replace("\n", "<br />"));
						resp.getWriter().println("<br/>################################<br/>");
						
						File uploadedFile = new File("/tmp/"+new Date().getTime()+"_"+item.getName());
						item.write(uploadedFile);
						
						
						SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer();
						System.out.println("PATH: [" +uploadedFile.getAbsolutePath() + "]");
						syntacticAnalyzer.validate(uploadedFile.getAbsolutePath(), item.getName());
						System.out.println("VALIDEI!");
						out.print("<br/>BOOK INTERPRETED: <br/>");
						out.print("<div id=\"demo1\" class=\"demo jstree jstree-0 jstree-default jstree-focused\"></div>");
						String bookData = new MFBook(syntacticAnalyzer.getBook()).toXml();
						String jsFunction = "<script type='text/javascript' class='source'>" + 
								"$(function () {   " +
								"$('#demo1').jstree({" +
								"\"xml_data\" : {" +
								"\"data\" : \"" + bookData.replace("\"", "'") + "\", \"xsl\" : \"nest\"}," +
								"\"plugins\" : [ \"themes\", \"xml_data\" ]" +	
								"});" +
								"});" +
								"</script>";
						
						out.print(jsFunction);
						
					}

				}

			} catch (Exception e) {

				resp.getWriter().println(
						"ocorreu um problema ao fazer o upload: "
								+ e.getMessage());

			}

		}
		
		out.print("</body>");
		out.print("</html>");
		out.flush();
		out.close();

	}

}
