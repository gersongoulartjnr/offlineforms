package br.unifesp.maritaca.ws.api.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Adapter extends XmlAdapter<HashMapType, HashMap>  {

	@Override
	public HashMapType marshal(HashMap v) throws Exception {
		Set<String> keys = v.keySet();
		List<MapEntry> results = new ArrayList<MapEntry>(v.size());
		for (String key : keys) {
			results.add(new MapEntry(key, (String) v.get(key)));
		}
		HashMapType hmt = new HashMapType();
		hmt.setEntry(results);
		return hmt;
	}

	@Override
	public HashMap unmarshal(HashMapType v) throws Exception {
		HashMap map = new HashMap(v.getEntry().size());
		for (MapEntry entry : v.getEntry()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
}
