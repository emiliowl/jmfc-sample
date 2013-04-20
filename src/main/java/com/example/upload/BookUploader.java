package com.example.upload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.expert.adapter.MFBook;
import com.expert.adapter.enumeration.FieldProperties;
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
		out.print("<script src=\"http://twitter.github.com/bootstrap/assets/js/bootstrap.js\" type=\"text/javascript\"></script>");
		out.print("<script src=\"http://twitter.github.com/bootstrap/assets/js/bootstrap-modal.js\" type=\"text/javascript\"></script>");
		out.print("<link media=\"all\" type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/themes/cupertino/jquery-ui.css\" rel=\"stylesheet\">");
		out.print("<link href=\"css/bootstrap.css\" rel=\"stylesheet\">");
		out.print("<link href=\"css/bootstrap-responsive.css\" rel=\"stylesheet\">");
		out.print("<link href=\"css/docs.css\" rel=\"stylesheet\">");
		out.print("<link href=\"js/google-code-prettify/prettify.css\" rel=\"stylesheet\">");
		out.print("</head>");
		
		out.print("<body data-spy=\"scroll\" data-target=\".subnav\" data-offset=\"50\">");                               
		out.print("	<div class=\"navbar navbar-fixed-top\">");
		out.print("		<div class=\"navbar-inner\">");
		out.print("			<div class=\"container\">");
		out.print("				<a class=\"btn btn-navbar\" data-toggle=\"collapse\" data-target=\".nav-collapse\"> <span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span>");
		out.print("				</a> <a class=\"brand\" href=\"./index2.html\">JMFConnector</a>");
		out.print("				<div class=\"nav-collapse collapse\">");
		out.print("					<ul class=\"nav\">");
		out.print("						<li class=\"\"><a href=\"./index.html\">Visão Geral</a></li>");
		out.print("						<li class=\"active\"><a href=\"./demostracao.html\">Demonstração</a></li>");
		out.print("						<li class=\"\"><a href=\"./mini_tutorial.html\">Mini Tutorial</a></li>");
		out.print("						<li class=\"\"><a href=\"#\">Download</a></li>");
		out.print("						<li class=\"divider-vertical\"></li>");
		out.print("						<li class=\"\"><a href=\"#\">Sobre Nós</a></li>");
		out.print("					</ul>");
		out.print("				</div>");
		out.print("			</div>");
		out.print("		</div>");
		out.print("</div>");
		out.print("<div style='clear:both; margin-top: 50;'");
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
						
						out.print("<p class=\"alert alert-info\">");
						resp.getWriter().println(item.getString().replace("\n", "<br />"));
						out.print("</p>");
						
						
						File uploadedFile = new File("/tmp/"+new Date().getTime()+"_"+item.getName());
						item.write(uploadedFile);
						
						
						SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer();
						System.out.println("PATH: [" +uploadedFile.getAbsolutePath() + "]");
						syntacticAnalyzer.validate(uploadedFile.getAbsolutePath(), item.getName());
						System.out.println("VALIDEI!");
						out.print("<br/>BOOK INTERPRETED: <br/>");
						out.print("<div id=\"demo1\" class=\"demo jstree jstree-0 jstree-apple jstree-focused\"></div>");
						String bookData = new MFBook(syntacticAnalyzer.getBook()).toXml();
						String jsFunction = "<script type='text/javascript' class='source'>" + 
								"$(function () {   " +
								"$('#demo1').jstree({" +
								"\"xml_data\" : {" +
								"\"data\" : \"" + bookData.replace("\"", "'") + "\", \"xsl\" : \"nest\"}," +
								"\"plugins\" : [ \"themeroller\", \"xml_data\", \"ui\" ]" +	
								"}).bind('select_node.jstree', function (event, data) { $('#' + (data.inst.get_text(data.rslt.obj)) + '_modal').modal('show');});" +
								"});" +
								"</script>";
						
						out.print(jsFunction);
						out.print(getFieldsInfoDiv(new MFBook(syntacticAnalyzer.getBook())));
					}

				}

			} catch (Exception e) {

				resp.getWriter().println(
						"ocorreu um problema ao fazer o upload: "
								+ e.getMessage());

			}

		}
		out.print("</div>");
		out.print("</body>");
		out.print("</html>");
		out.flush();
		out.close();

	}
	
	//TODO: gambiarra master, necessario extrair para classes helper e rever o modelo Servlet, um JSP faria muito mais sentido
	private String getFieldsInfoDiv(MFBook mfbook) {
		String resultString = "";
		Map<String, Map<FieldProperties, String>> fieldsInfo = mfbook.getFieldsInfo();
		Iterator<String> fieldsInfoIterator = fieldsInfo.keySet().iterator();
		while(fieldsInfoIterator.hasNext()) {
			String fieldName = fieldsInfoIterator.next();
			resultString += createDiv(fieldName, fieldsInfo.get(fieldName));
		}
		
		return resultString;
	}
	
	//TODO: gambiarra master, necessario extrair para classes helper e rever o modelo Servlet, um JSP faria muito mais sentido
	private String createDiv(String fieldId, Map<FieldProperties, String> fieldProperties) {
		String result = "<br/><div id='" + fieldId + "_modal' class='modal hide fade'>";
		
		result += "<div class='modal-header'>";
	    result += "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>";
	    result += "<h3>"+ fieldId +"</h3>";
	    result += "</div>";
		
	    result += "<div class='modal-body'>";
		for(FieldProperties fieldProperty : FieldProperties.values()) {
			result += "<ul>";
				result += "<li>";
					result += fieldProperty.toString() + "[ " + fieldProperties.get(fieldProperty) + " ]";
				result += "</li>";
			result += "</ul>";
		}
		result += "</div>";
		
		result += "</div><br/>";
		return result;
	}

}
