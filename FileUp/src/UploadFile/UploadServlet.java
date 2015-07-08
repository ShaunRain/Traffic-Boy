package UploadFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static MongoOperate operate;

	public UploadServlet() {
		super();
		operate = new MongoOperate();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String imageURL = null;
		StringBuffer detail = new StringBuffer();
		File dir;
		File jsonFile = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			String realpath = request.getSession().getServletContext()
					.getRealPath("upload");
			dir = new File(realpath);
			if (!dir.exists())
				dir.mkdir();

			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			try {
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {

					} else {
						File file = new File(dir, item.getName());
						item.write(file);
						if (file.getName().endsWith(".jpg")) {
							imageURL = "http://shaunrain.oicp.net/FileUp/upload/"
									+ file.getName();
						} else if (file.getName().endsWith(".txt")) {

							detail = new StringBuffer();
							try (InputStreamReader reader = new InputStreamReader(
									new FileInputStream(file), "utf-8");
									BufferedReader read = new BufferedReader(
											reader);) {

								String line = null;
								while ((line = read.readLine()) != null)
									detail.append(line);

							}
						}

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			String[] details = detail.toString().split("[$]");
			Incident i = new Incident(details[0], Double.valueOf(details[1]),
					Double.valueOf(details[2]), details[3], details[4],
					details[5], imageURL);
			operate.add(i);

			Gson gson = new Gson();
			jsonFile = new File(dir, details[0] + ".json");
			if (!jsonFile.exists()) {
				jsonFile.createNewFile();
				String jsonIncident = gson.toJson(i);
				try (FileWriter fw = new FileWriter(jsonFile, true);
						BufferedWriter writer = new BufferedWriter(fw);) {
					writer.write(jsonIncident);
				}
			} else {
				String jsonIncident = gson.toJson(i);
				try (FileWriter fw = new FileWriter(jsonFile, true);
						BufferedWriter writer = new BufferedWriter(fw);) {
					writer.write(",");
					writer.write(jsonIncident);
				}
			}

		} else {
			doGet(request, response);
		}
	}

	@Override
	public void destroy() {
		operate.destroy();
		super.destroy();
	}
}
