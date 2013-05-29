package game.nwn;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;

public class BinaryFileReader implements Closeable {
  public RandomAccessFile inp;

  public BinaryFileReader(File bifFile) {
    try {
      inp = new RandomAccessFile(bifFile, "r");
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
  }
  
  public long readWord() {
    long result = 0;
    int i = 0;
    try {
      result += readByte() << (i++*8);
      result += readByte() << (i++*8);
      result += readByte() << (i++*8);
      result += readByte() << (i++*8);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
    return result;
  }

  public  int readShort() {
    int result = 0;
    int i = 0;
    try {
      result += readByte() << (i++*8);
      result += readByte() << (i++*8);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
    return result;
  }

  public  int readSignedShort() {
    try {
      return inp.readShort();
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
    return -1;
  }

  public int readByte()  {
    int result = 0;
    try {
      result = inp.readUnsignedByte();
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
    return result;
  }
  
  public byte[] readBytes(int num)  {
    byte[] bytes = new byte[num];
    try {
      inp.read(bytes);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
    return bytes;
  }

  public byte[] readBytes(long offset, int num)  {
    byte[] bytes = new byte[num];
    try {
      seek(offset);
      inp.read(bytes);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
    return bytes;
  }

  public String readString(int len) {
    return new String(readBytes(len), Charsets.UTF_8);
  }

  public String readNullString(int len) {
    String result = new String(readBytes(len), Charsets.UTF_8);
    int i = result.indexOf(0);
    if ( i != -1 ) {
      return result.substring(0, i);
    } else {
      return result;
    }
  }

  @Override
  public void close() {
    try {
      inp.close();
    } catch (Exception e) {
      Throwables.propagate(e);
    }
    
  }
  
  public void seek(long pos) {
    try {
      inp.seek(pos);
    } catch (IOException e) {
      Throwables.propagate(e);
    }
  }

  public String readStringAt(long offset, int len) {
    seek(offset);
    return readString(len);
  }

  public long[] readWords(int len) {
    long[] result = new long[len];
    for(int i=0;i<len;++i) {
      result[i] = readWord();
    }
    return result;
  }

  public int[] readShorts(int len) {
    int[] result = new int[len];
    for(int i=0;i<len;++i) {
      result[i] = readShort();
    }
    return result;
  }

  public int[] readSignedShorts(int len) {
    int[] result = new int[len];
    for(int i=0;i<len;++i) {
      result[i] = readSignedShort();
    }
    return result;
  }

  public long pos() {
    try {
      return inp.getFilePointer();
    } catch(Exception e) {
      Throwables.propagate(e);
    }
    return 0;
  }

  public float readFloat() {
    try {
      return ByteBuffer.wrap(readBytes(4)).order(ByteOrder.LITTLE_ENDIAN ).getFloat();
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

}