package executable.sentimentbuilder;

/**
 * This is an awefull quickfix to using String as Map keys
 * @author ceichler
 *
 */

public class StringWrapper {
	
	String s;

	public StringWrapper(String s) {
		this.s = s;
	}

	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@Override
	public String toString() {
		return s.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringWrapper other = (StringWrapper) obj;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.contentEquals(other.s))
			{
			System.out.println(s);
			System.out.println(other.s);
			return false;
			}
			
		return true;
	}
	
	

}
