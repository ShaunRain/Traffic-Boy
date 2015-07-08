package UploadFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @Shaun Rain 2014
 */
public class MongoOperate {

	private MongoClient client = null;
	private DB db;
	private DBCollection incidents;

	@SuppressWarnings("deprecation")
	public MongoOperate() {
		client = new MongoClient("localhost", 27017);
		db = client.getDB("server");
		incidents = db.getCollection("incidents");
	}

	public void destroy() {
		if (client != null)
			client.close();
		client = null;
		db = null;
		incidents = null;
		System.gc();
	}

	public List<Incident> queryAll() throws IOException, ClassNotFoundException {

		List<Incident> incs = new ArrayList<>();
		DBCursor cursor = incidents.find();
		while (cursor.hasNext()) {
			byte[] bytes = (byte[]) cursor.next().get("inc");
			InputStream is = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(is);
			incs.add((Incident) ois.readObject());
		}
		return incs;
	}

	public void add(Incident incident) throws IOException {
		DBObject inc = new BasicDBObject();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(incident);

		inc.put("inc", bos.toByteArray());
		inc.put("image", incident.getImage());
		inc.put("type", incident.getType());
		incidents.insert(inc);

		bos.close();
		oos.close();
	}

	public void remove(String image) {
		incidents.remove(new BasicDBObject("image", image));
	}

	public List<Incident> query(String type) throws ClassNotFoundException,
			IOException {
		if (type.equals("all"))
			return queryAll();

		List<Incident> incs = new ArrayList<>();
		DBCursor cursor = incidents.find(new BasicDBObject("type", type));
		while (cursor.hasNext()) {
			byte[] bytes = (byte[]) cursor.next().get("inc");
			InputStream is = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(is);
			incs.add((Incident) ois.readObject());
		}
		return incs;
	}

}
