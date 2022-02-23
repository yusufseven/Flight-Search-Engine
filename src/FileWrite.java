import java.io.*;

public class FileWrite
{
    public void createFile(String fileName)
    {
        String path = fileName;
        try
        {
            File file = new File(path);
            file.createNewFile();
        } catch (IOException e)
        {
            System.out.println("An error has been occurred while creating a file.");
            e.printStackTrace();
        }
    }

    public void write(String fileName, String line)
    {
        String path = fileName;
        try
        {
            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            byte[] byteArray = line.getBytes();
            fileOutputStream.write(byteArray);
            fileOutputStream.close();
        } catch (Exception e)
        {
            System.out.println("An error has been occurred while writing to file.");
        }
    }

    public void deleteFile(String fileName)
    {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        try
        {
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}