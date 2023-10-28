package dmytromk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

public class CustomClassLoader extends ClassLoader{
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) throws ClassNotFoundException {
        fileName = fileName.replace('.', File.separatorChar) + ".class";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;

        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException ignored) {
            throw new ClassNotFoundException("Class not found: " + fileName);
        }

        buffer = byteStream.toByteArray();
        return buffer;
    }
}
