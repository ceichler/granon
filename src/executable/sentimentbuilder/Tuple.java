package executable.sentimentbuilder;
import java.util.HashMap;

public class Tuple {
	
	private HashMap<String, String> contents;
	private static String[] attributes;
	private static int s;
	
	public void put(String _a, String _v) {
		this.contents.put(_a, _v);
	}
	
	public void put(int _i, String _v) {
		this.contents.put(Tuple.attributes[_i], _v);
	}
	
	public String get(String _a) {
		return this.contents.get(_a);
	}
		
	public String get(int _i) {
		return this.contents.get(Tuple.attributes[_i]);
	}
	

	public String[] getTuple() {
		String[] ret = new String[Tuple.attributes.length];
		for(int i=0;i<Tuple.attributes.length;i++) {
			ret[i]=this.get(Tuple.attributes[i]);
		}
		return ret;
	}
	
	public static int size() {
		return Tuple.s;
	}
	
	Tuple(String[] _attributes){
		Tuple.attributes = new String[_attributes.length];
		int i=0;
		for(String a : _attributes){
			Tuple.attributes[i++] = a;
		}
		this.contents = new HashMap<String, String>();
		Tuple.s = _attributes.length;
	}
	

}