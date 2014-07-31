package pl.softech.stackoverflow.datastructure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * http://stackoverflow.com/questions/25068233/data-structure-for-keeping-frequency-count-of-pairwise-data/25069030#25069030
 * Data Structure for keeping frequency count of pairwise data? 
 */
public class NeighborListExample {

	static class Pair {

		private String feature;
		private int cnt = 1;

		Pair(String feature) {
			this.feature = feature;
		}

		void incr() {
			cnt++;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((feature == null) ? 0 : feature.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (feature == null) {
				if (other.feature != null)
					return false;
			} else if (!feature.equals(other.feature))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "(" + feature + ", " + cnt + ")";
		}

	}

	static Map<String, List<Pair>> feature2neighbors = new HashMap<>();

	private static int getId(Object[][] data, int i) {
		return ((Integer) data[i][0]).intValue();
	}

	private static String getFeature(Object[][] data, int i) {
		return data[i][1].toString();
	}

	private static void processFeatures(String[] array) {

		for (int i = 0; i < array.length; i++) {

			for (int j = 0; j < array.length; j++) {

				if (i != j) {

					List<Pair> pairs = feature2neighbors.get(array[i]);
					if (pairs == null) {
						pairs = new LinkedList<>();
						feature2neighbors.put(array[i], pairs);
					}

					Pair toAdd = new Pair(array[j]);
					int index = pairs.indexOf(toAdd);
					if (index == -1) {
						pairs.add(toAdd);
					} else {
						pairs.get(index).incr();
					}

				}

			}

		}

	}

	static void print(Map<String, List<Pair>> feature2neighbors) {

		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, List<Pair>> e : feature2neighbors.entrySet()) {

			builder.append(e.getKey()).append(" -> ");
			Iterator<Pair> it = e.getValue().iterator();
			builder.append(it.next().toString());
			while(it.hasNext()) {
				builder.append(" ").append(it.next().toString());
			}
			builder.append("\n");
			
		}
		
		System.out.println(builder.toString());

	}

	public static void main(String[] args) {

		//I assume that data is sorted
		Object[][] data = { { 5, "F1" }, //
				{ 5, "F2" }, //
				{ 6, "F1" }, //
				{ 6, "F2" }, //
				{ 7, "F3" }, //
				{ 7, "F1" }, //
				{ 7, "F2" }, //
				{ 8, "F1" }, //
				{ 9, "F1" }, //
				{ 10, "F1" }, //

		};

		List<String> features = new LinkedList<>();
		int id = getId(data, 0);
		for (int i = 0; i < data.length; i++) {

			if (id != getId(data, i)) {
				processFeatures(features.toArray(new String[0]));
				features = new LinkedList<>();
				id = getId(data, i);
			}
			features.add(getFeature(data, i));
		}
		
		print(feature2neighbors);

	}

}
