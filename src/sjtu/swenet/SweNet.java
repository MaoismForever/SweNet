package sjtu.swenet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Singleton class for term similarity computing
 * @author Jiangang
 *
 */
public class SweNet {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(SweNet.getInstance().sim("java", "programming language"));
	}
	
	private Map<String, Set<String>> childFatherMap;
	private static String dbpath = "/data/softschema.txt";
	
	private static SweNet instance;
	
	public static synchronized SweNet getInstance() {
		if (instance == null) {
			instance = new SweNet(dbpath);
			return instance;
		} else {
			return instance;
		}
	}
	
	public SweNet(String dbpath){
		InputStream in;
		//in = new BufferedInputStream(new FileInputStream(path));
		in = this.getClass().getResourceAsStream(dbpath);
		loadAsTree(in);
	}
	public boolean existPath(String src, String dest){
		Set<String> fatherSet = childFatherMap.get(src);
		if(fatherSet==null)
			return false;
		if(fatherSet.contains(dest))
			return true;
		for(String str : fatherSet){
			boolean flag = existPath(str, dest);
			if(flag)
				return flag;
		}
		return false;
	}
	public void loadAsTree(InputStream in) {
		childFatherMap = new HashMap<String, Set<String>>();
		List<String> lines = Utils.readLine(in);
		Set<String> nodeSet = new HashSet<String>();
		for(String line : lines){
			String[] arr = line.split(":");
			nodeSet.add(arr[0]);
			nodeSet.add(arr[1]);
			childFatherMap = Utils.addToMap(childFatherMap, arr[0], arr[1]);
		}
		for(String node : nodeSet){
			if(!childFatherMap.containsKey(node)){
				Set<String> father = new HashSet<String>();
				father.add("-1");
				childFatherMap.put(node, father);
			}
		}
	}

	public double sim(String a, String b) {
		if (a == null || b == null)
			return 0d;
		//replace spaces with hyphens
		a = a.replaceAll("\\s+", "-").toLowerCase();
		b = b.replaceAll("\\s+", "-").toLowerCase();
		
		if (a.equals(b))
			return 1d;

		List<Subsumer> paths = getAllPaths(a, b);
		if (paths == null || paths.size() == 0)
			return 0d;
		Subsumer subsumer = paths.get(0);
		int dep = subsumer.lsubsumerHeight;
		int depth1 = subsumer.lhypernymSize;
		int depth2 = subsumer.rhypernymSize;

		double score = 0;
		if (depth1 > 0 && depth2 > 0) {
			score = (double) (2 * dep) / (double) (depth1 + depth2);
		}
		return score;
	}

	public List<List<String>> getHypernymTrees(String synset) {
		//System.out.println(synset);
		List<List<String>> returnList = new ArrayList<List<String>>();
		if (childFatherMap.get(synset) == null) {
			List<String> newList = new ArrayList<String>();
			newList.add(synset);
			returnList.add(newList);
			return returnList;
		}
		List<String> synlinks = new ArrayList<String>(
				childFatherMap.get(synset));

		for (String hypernym : synlinks) {
			List<List<String>> hypernymTrees = getHypernymTrees(hypernym);
			if (hypernymTrees != null) {
				for (List<String> hypernymTree : hypernymTrees) {
					hypernymTree.add(synset);
					returnList.add(hypernymTree);
				}
			}
			if (returnList.size() == 0) {
				List<String> newList = new ArrayList<String>();
				newList.add(synset);
				newList.add(0, "0");
				returnList.add(newList);
			}
		}
		return returnList;
	}

	public List<Subsumer> getAllPaths(String synset1, String synset2) {
		List<Subsumer> paths = new ArrayList<Subsumer>();
		List<List<String>> lTrees = getHypernymTrees(synset1);
		List<List<String>> rTrees = getHypernymTrees(synset2);

		if (lTrees == null || rTrees == null)
			return null;
		int max = 0;
		for (List<String> lTree : lTrees) {
			for (List<String> rTree : rTrees) {
				String subsumer = getSubsumerFromTrees(lTree, rTree);

				if (subsumer == null)
					continue;

				int lCount = 0;
				List<String> lpath = new ArrayList<String>(lTree.size());
				List<String> reversedLTree = Utils.reverse(lTree);

				for (String synset : reversedLTree) {
					lCount++;
					if (synset.equals(subsumer))
						break;
					lpath.add(synset);
				}

				int rCount = 0;
				List<String> rpath = new ArrayList<String>(rTree.size());
				List<String> reversedRTree = Utils.reverse(rTree);
				for (String synset : reversedRTree) {
					rCount++;
					if (synset.equals(subsumer))
						break;
					rpath.add(synset);
				}

				Subsumer sub = new Subsumer();
				sub.subsumer = subsumer;
				sub.length = rCount + lCount - 1;
				sub.lpath = lpath;
				sub.rpath = rpath;
				sub.lhypernymSize = lTree.size();
				sub.rhypernymSize = rTree.size();
				sub.lsubsumerHeight = lTree.size() - lCount + 1;
				sub.rsubsumerHeight = rTree.size() - rCount + 1;
				if (sub.lsubsumerHeight == sub.rsubsumerHeight) {
					if (sub.lsubsumerHeight >= max) {
						paths.add(sub);
						max = sub.lsubsumerHeight;
					}
				}
			}
		}

		// asc sort by length
		Collections.sort(paths, new Comparator<Subsumer>() {
			public int compare(Subsumer s1, Subsumer s2) {
				if (s1.length > s2.length) {
					return 1;
				} else if (s1.length < s2.length) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		return paths;
	}

	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return synset of the subsumer
	 */
	private static String getSubsumerFromTrees(List<String> list1,
			List<String> list2) {
		List<String> tree1 = Utils.reverse(list1);
		List<String> tree2 = Utils.reverse(list2);

		String tree1Joined = " " + Utils.join(" ", tree1) + " ";
		for (String synset2 : tree2) {
			if (tree1Joined.indexOf(synset2) != -1) {
				return synset2;
			}
		}

		return null;
	}

	public static class Subsumer {
		public String subsumer;
		public int length;
		public double ic;
		public List<String> lpath;
		public List<String> rpath;
		public int lhypernymSize;
		public int rhypernymSize;
		public int lsubsumerHeight;
		public int rsubsumerHeight;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{ ");
			sb.append("\"subsumer\" : \"" + subsumer + "\", ");
			sb.append("\"length\" : \"" + length + "\", ");
			sb.append("\"ic\" : \"" + ic + "\", ");
			sb.append("\"lpath\" : \"" + lpath + "\", ");
			sb.append("\"rpath\" : \"" + rpath + "\"");
			sb.append(" }");
			return sb.toString();
		}
	}
}
