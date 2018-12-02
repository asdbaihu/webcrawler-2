
package webcrawler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
 
public class ImageDownloader
{   
    public ArrayList<String> links= new ArrayList<>();
    public ArrayList<String> downloadedImages = new ArrayList<>();
    public String dowloadPath ;
    
    //constructor
    public ImageDownloader(ArrayList<String> images, String path)
    {
        this.links = images;
        dowloadPath = path;
    }
    
    //downloads
    public void download() {
        
        for (int i=0; i<links.size(); i++)
        {
            
            BufferedImage image = null;
            try{

               URL url =new URL(links.get(i));  //create URL from String of links of images from WebCrawler.
               
               image = ImageIO.read(url);    // read the url


               
               new File(dowloadPath).mkdirs();    //create folder
               if(image!=null)
               {
               ImageIO.write(image, "png",new File(dowloadPath + "/image"+i+".png"));  //download to folder
               downloadedImages.add(dowloadPath+ "/" + "image"+i+".png");
               }

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
