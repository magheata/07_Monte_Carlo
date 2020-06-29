/* Created by andreea on 12/06/2020 */
package Infrastructure;

import java.io.*;
import java.util.ArrayList;

public class ReaderService {
    /**
     * Reads a file
     * @param path path to the file
     * @return list containing all the words from the file
     */
    public ArrayList<String> readFile(String path) {
        File file = new File(path);
        ArrayList<String> fileContent = new ArrayList<>();
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                fileContent.add(str);
            }
            in.close();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return fileContent;
    }
}
