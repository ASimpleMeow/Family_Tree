package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager{
	
	public String readFileToString(boolean forCompression){
		InputStream is = null;;
		BufferedReader buf = null;
		try{
			is = new FileInputStream(getFile(forCompression));
			buf = new BufferedReader(new InputStreamReader(is));
			        
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();

			while(line != null){
			   sb.append(line);
			   line = buf.readLine();
			}
			        
			String result = sb.toString();
			buf.close();
			is.close();
			return result;
		}catch(IOException e){
			System.out.println("ERROR I/O READING OF FILE\n");
			e.printStackTrace();
		}catch(NullPointerException e){
			System.out.println("File Selection Canceled\n");
		}catch(Exception e){
			System.out.println("Unexpected Error Has Occurred\n");
			e.printStackTrace();
		}finally{
			try{
		        if (buf != null) buf.close();
		        if (is != null) is.close();
		    }catch ( IOException e){}
		}
		return null;
	}
	
	public void writeFile(String huffmanCode){
		BufferedWriter writer = null;
		try{
		    writer = new BufferedWriter(new FileWriter(new File("data/output.dat")));
		    writer.write(huffmanCode);
		}catch ( IOException e){}
		finally{
		    try{
		        if ( writer != null)
		        	writer.close( );
		    }catch ( IOException e){}
		}
	}
	
	private String getFile(boolean forCompression){
		String filePath = "";
		JFrame jf = new JFrame( "Please choose a file containing family data" ); // added
        jf.setAlwaysOnTop( true ); // added
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        FileFilter filter = (forCompression)? new FileNameExtensionFilter("Text Files","txt"):
        	new FileNameExtensionFilter("Dat File","dat");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog( jf );  // changed
        try{
        	filePath = fileChooser.getSelectedFile().getPath();
        }catch(NullPointerException e){
        	jf.dispose();
        	return null;
        }
        jf.dispose(); // added
        
        return filePath;
	}
}
