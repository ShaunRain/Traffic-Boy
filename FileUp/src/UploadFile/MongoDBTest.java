package UploadFile;

/**
 * @Shaun Rain 2014
 */
public class MongoDBTest {
	public static void main(String[] args) {

		try {

			MongoOperate operate = new MongoOperate();
			
			for (Incident i : operate.query("all"))
				System.out.println(i);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}
}
