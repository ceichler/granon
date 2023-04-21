package progFileReader;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProgReader {
	private String pathToFile;
	private BufferedReader reader;
	public ProgReader() {
		try {
			reader = new BufferedReader(new FileReader("src/progFileReader/command.txt"));
			pathToFile = new String("src/progFileReader/command.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ProgReader(String pathToFile) {
		try {
			reader = new BufferedReader(new FileReader(pathToFile));
			pathToFile = new String(pathToFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFileRead(String pathToFile) {
		try {
			reader = new BufferedReader(new FileReader(pathToFile));
			pathToFile = new String(pathToFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayFilePath() {
		System.out.println(pathToFile);
	}
	
	public void displayFileContent() {       
        try {
	        String line = reader.readLine();
	        while (line != null) {
	            if (!line.trim().isEmpty()) {
	                System.out.println(line);
	            }
	            line = reader.readLine();
        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (reader != null) {
	                reader.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public List<String> getFileContent() {
		List<String> content = new ArrayList<String>();
		try {
	        String line = reader.readLine();
	        while (line != null) {
	            if (!line.trim().isEmpty()) {
	            	content.add(line);
	            }
	            line = reader.readLine();	            
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (reader != null) {
	                reader.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		return content;
	}
	
}
