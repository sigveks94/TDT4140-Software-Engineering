package tdt4140.gr1814.webserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tdt4140.gr1814.app.core.Patient;

//This is the servlet supposed to handle requests for patients

public class FetchPatients extends HttpServlet{
	
	//Serial Version is required. If the Servlet ever changes, the serialVersion is supposed to change as well
	private static final long serialVersionUID = 1L;
	
	//The GET-Request is supposed to get the care taker ID as paramater and will echo back all of its patients as a JSON
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//Gets the careTakerID sent as a paramter
		String careTakerParamter = req.getParameter("care_taker_id");
		
		//If the careTakerId parameter is empty the request is denied
		if(careTakerParamter == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		//Tries to convert the caretakerId into a integer, if it fails it considers the format to be wrong
		int careTakerId = 0;
		try {
			careTakerId = Integer.parseInt(careTakerParamter);
		}
		catch(NumberFormatException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		
		//Sets the content type so it expects a JSON response
		resp.setContentType("application/json");
		
		//------------------------------ Temporary ------------------------------------------
		Gson jsonParser = new Gson();
		Patient p = Patient.newPatient("Kjellaug", "Bjurgan", 'F', 12345678911l, 47673232, "geir@live.no", "id6");
		Patient x = Patient.newPatient("Gregers", "Grhezvors", 'M', 12345378911l, 98352463, "geir@live.no", "id7");
		Patient[] z = {p, x};
		String jsonP = jsonParser.toJson(z);
		
		
		PrintWriter echo = resp.getWriter();
		echo.println(jsonP);
		echo.flush();
		echo.close();
		// ------------------------- ------------------------------ -------------------------
		
	}
	

}
