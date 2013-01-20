package com.example.upload;

import java.io.File;
import java.io.IOException;
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

import com.expert.jmfc.compiler.SyntacticAnalyzer;

public class BookUploader extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8713150470077228784L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
						resp.getWriter().println("Form field");
						resp.getWriter().println("Name:" + item.getFieldName());
						resp.getWriter().println("Value:" + item.getString());
					} else {
						// caso seja um campo do tipo file
						resp.getWriter().println("NOT Form field");
						resp.getWriter().println("Name:" + item.getFieldName());
						resp.getWriter().println("FileName:" + item.getName());
						resp.getWriter().println("Size:" + item.getSize());
						resp.getWriter().println("ContentType:" + item.getContentType());
						
						resp.getWriter().println("EXAMPLE OF THE API FUNCTIONALITY");
						resp.getWriter().println("\nBOOK: ");
						resp.getWriter().println("\n################################:\n");
						resp.getWriter().println(item.getString());
						resp.getWriter().println("\n################################");
						
						File uploadedFile = new File("/tmp/"+new Date().getTime()+"_"+item.getName());
						item.write(uploadedFile);
						
						
						SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer();
						System.out.println("PATH: [" +uploadedFile.getAbsolutePath() + "]");
						syntacticAnalyzer.validate(uploadedFile.getAbsolutePath(), item.getName());
						System.out.println("VALIDEI!");
						
						resp.getWriter().println("\n\n\nFIELDS PARSED: ");
						resp.getWriter().println("\n################################:\n");
						resp.getWriter().println(syntacticAnalyzer.getBook().toString());
						resp.getWriter().println("\n################################");
						
					}

				}

			} catch (Exception e) {

				resp.getWriter().println(
						"ocorreu um problema ao fazer o upload: "
								+ e.getMessage());

			}

		}

	}

}
