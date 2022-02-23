import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileRead
{
    public String[] readFile(String fileName)
    {
        try
        {
            ArrayList<String> fileLines = new ArrayList<String>();

            String pathName =  fileName;

            Scanner scanner = new Scanner(new File(pathName));
            while (scanner.hasNextLine())
            {
                String currentLine = scanner.nextLine();
                fileLines.add(currentLine);
            }
            scanner.close();

            String[] arr = new String[fileLines.size()];
            arr = fileLines.toArray(arr);

            return arr;

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}