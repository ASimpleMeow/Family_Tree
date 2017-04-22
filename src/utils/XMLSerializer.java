
package utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Serailizes data to an XML file
 * 
 * @author Oleksandr Kononov
 *
 */
public class XMLSerializer
{

  @SuppressWarnings("rawtypes")
private Stack stack = new Stack();
  private File file;

  /**
   * Constructor for the class
   * @param file
   */
  public XMLSerializer(File file)
  {
    this.file = file;
  }

  /**
   * Pushs an object onto the stack
   */
  @SuppressWarnings("unchecked")
  public void push(Object o)
  {
    stack.push(o);
  }

  /**
   * Pops an object off the stack and returns it
   */
  public Object pop()
  {
    return stack.pop(); 
  }

  /**
   * Reads in a stack from an XML file which is decompressed by GZIP stream
   */
  @SuppressWarnings("unchecked")
  public void read() throws Exception
  {
    ObjectInputStream is = null;

    try
    {
      XStream xstream = new XStream(new DomDriver());
      is = xstream.createObjectInputStream(new FileReader(file));
      stack = (Stack) is.readObject();
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }
  }

  /**
   * Write the stack to an XML file and compresses it with GZIP stream
   */
  public void write() throws Exception
  {
    ObjectOutputStream os = null;

    try
    {
      XStream xstream = new XStream(new DomDriver());
      os = xstream.createObjectOutputStream(new FileWriter(file));
      os.writeObject(stack);
    }
    finally
    {
      if (os != null)
      {
        os.close();
      }
    }
  }
}