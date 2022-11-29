package utils;

public class Pair<T>{
		T first;
		T second;
		
		public Pair(T t1, T t2){
			this.first = t1;
			this.second = t2;
		}
		
		public T getFirst() {
			return first;
		}
		
		public T getSecond() {
			return second;
		}

}
