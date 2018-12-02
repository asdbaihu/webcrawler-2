
package webcrawler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author Roi
 */
public class PDFdownloader {
    public ArrayList<String> links= new ArrayList<>();
    public ArrayList<String> downloadedPDFs = new ArrayList<>();
    public ArrayList<String> downloadedPDFnames = new ArrayList<>();
    public String dowloadPath ;
    public String monthLimit="April 2017";
    
    //constructor
    public PDFdownloader(ArrayList<String> pdfs,ArrayList<String> names, String path) {
        this.links = pdfs;
        dowloadPath = path;
        downloadedPDFnames = names;
        
    }
    
    //removes from downloadedPDFs and downloadedPDFnames Strings before April 2017
    //HAS TO BE CALLED BEFORE DOWNLOAD
    public void limitPDFtoMonth() {
        try{
        int getI=0;
        for (int i=0; i<downloadedPDFnames.size() ;i++)
        {
            if(downloadedPDFnames.get(i).equalsIgnoreCase(monthLimit))
            getI=i;
        }
        
        for (int i=0; i<downloadedPDFnames.size() ;i++)
        {
           
            if(i>getI)
            {
              downloadedPDFnames.remove(i);
              downloadedPDFs.remove(i);
              i--;
            }
        }
        }
         catch (Exception e) {
             
         }
        
    }
    
    //downloads PFDs
    public void download() {
        
        //LIMIT ARRAYLISTS TO APRIL 2017
        limitPDFtoMonth();
        
        for (int i=0; i<links.size(); i++)
        {
            
            
            try{

               URL url =new URL(links.get(i));  //create URL from String of links of images from WebCrawler.
               
              
               System.out.println("opening connection");
                InputStream in = url.openStream();
                FileOutputStream fos = new FileOutputStream(new File(dowloadPath+"/" + downloadedPDFnames.get(i) + ".pdf"));

                System.out.println("reading from resource and writing to file...");
                int length = -1;
                byte[] buffer = new byte[1024];// buffer for portion of data from connection
                while ((length = in.read(buffer)) > -1) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                in.close();
                System.out.println("File downloaded");

               
               
                             
              
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
