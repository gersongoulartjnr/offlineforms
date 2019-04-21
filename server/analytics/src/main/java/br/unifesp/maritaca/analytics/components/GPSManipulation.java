package br.unifesp.maritaca.analytics.components;

import java.util.ArrayList;

import br.unifesp.maritaca.analytics.util.ConstantsPipeline;
import br.unifesp.maritaca.analytics.util.MongoDriver;

import com.mongodb.BasicDBObject;

public class GPSManipulation {

	public static BasicDBObject getGPSDistance(MongoDriver driver,
			String identifier, Double maxDistance, ArrayList<Double> actual) {
		/* Este campo sera usado para indexar a pesquisa */
		driver.getCollection().ensureIndex(new BasicDBObject(identifier, "2d"));
		BasicDBObject query = new BasicDBObject(ConstantsPipeline.NEAR, actual);
		query.put(ConstantsPipeline.DISTANCE_FIELD, "real_distance");
		query.put(ConstantsPipeline.MAX_DISTANCE, maxDistance/111.12);
		BasicDBObject geoNear = new BasicDBObject(ConstantsPipeline.GEONEAR,
				query);
		return geoNear;
	}
}