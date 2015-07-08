package UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @Shaun Rain 2014
 */
public class GetFiles {

	public static List<String> getJsons(String type) {
		List<String> jsons = new ArrayList<>();

		if (type.equals("all")) {
			jsons.add("http://shaunrain.oicp.net/FileUp/upload/clear.json");
			jsons.add("http://shaunrain.oicp.net/FileUp/upload/trouble.json");
			jsons.add("http://shaunrain.oicp.net/FileUp/upload/crowd.json");
			jsons.add("http://shaunrain.oicp.net/FileUp/upload/control.json");

		} else {
			jsons.add("http://shaunrain.oicp.net/FileUp/upload/" + type
					+ ".json");
		}

		return jsons;

	}

}
