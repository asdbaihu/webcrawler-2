
package webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {


    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";   
    private String url;  //Page to visit
    private ArrayList<String> links ; //List of images to save
    private ArrayList<String> heads ; //List of headings to save
    private ArrayList<String> descrs ; //List of descriptions to save
    private  ArrayList<String> pdfs;
    private  ArrayList<String> pdfnames;
    // Give it a URL and it makes an HTTP request for a web page
    public void crawl(String url)
    {
        
        
        try
        {
            //makes a connection
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            //gets the document
            Document htmlDocument = connection.get();
            //this.htmlDocument = htmlDocument;

            System.out.println("Received web page at " + url);
            
            //Finds the parent element that contains the pdfs
            Elements acc1 = htmlDocument.select(".accordion");
           
            //Finds the a[href] elements that contain pdfs
            Elements mypdfs = acc1.select("a[href]");
           
            
           
            //finds the carousel element
            Elements slider = htmlDocument.getElementsByClass("slider"); //get the elements of slider  (Should find only one)
           
            Elements slides = new Elements();                                 //slides initialize
            Elements images = new Elements();                                 //images of slider initialize
            Elements headings = new Elements();                               //headings of slider initialize
            Elements descriptions = new Elements();                           //descriptions of slider initialize
            
           
            
            
            //find slides <div> elements inside slider
            for (int i=0; i<slider.size();i++)
            {
                slides=slider.get(i).select("div.slide:not(.slick-cloned)");          
               
            }
             
            
            for (int i=0; i<slides.size();i++)
            {
               //finds image for each slide
                Element elementImg=slides.get(i).select("img[src]").first();
                //finds heading for each slide
                Element elementH3=slides.get(i).select("div.description-content h3").first();
                //finds description for each slide
                Element elementDescr=slides.get(i).select("div.description-content span").first();
                
                //adds image to arrayList
                images.add(elementImg);                           
                //adds heading to arrayList
                headings.add(i,elementH3);    
               //adds description to arrayList
                descriptions.add(i,elementDescr);      //get description element for each slide
                
                
             
            }
            
            
             
             
            
            if ( !images.isEmpty() )
            {
                for(Element link : images)
                {
                    this.links.add(link.absUrl("src"));         //get image "src" and save to class var
                }
            }
            
            if ( !headings.isEmpty() )
            {
                for(Element head : headings)
                {
                    if(head!=null)
                    this.heads.add(head.text());                //get heading "text" and save to class var
                    else this.heads.add("");
                }
            }
            
            if ( !descriptions.isEmpty() )
            {
                for(Element des : descriptions)
                {
                    if(des!=null)
                    this.descrs.add(des.text());                //get description "text" and save to class var
                    else this.descrs.add("");
                        
                }
            }
            
            
             if ( !mypdfs.isEmpty() )
            {
                
                for(Element apdf : mypdfs)
                {
                    if(apdf!=null)
                    {
                    this.pdfs.add(apdf.absUrl("href"));         //get pdf link and save to class var
                    this.pdfnames.add(apdf.text());             //get pdf names and save to class var
                    }
                    else this.pdfs.add("");                 
                }
            }
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            System.out.println("Error in out HTTP request " + ioe);
        }
        
    }
    
    //constructor
    public WebCrawler(String pageToVisit) {
        this.url = pageToVisit;
        
        links =  new ArrayList<>();
        heads =  new ArrayList<>();
        descrs =  new ArrayList<>();
        pdfs = new ArrayList<>();
        pdfnames = new ArrayList<>();
    }
    
    //MAIN FUNCTION does all 
    public static void main(String[] args) {
        //Set the path to download
        String path="C:/imgROI/";
        
       // String site="https://www.edf.fr/en/groupe-edf/producteur-industriel/carte-des-implantations/centrale-nucleaire-de-flamanville-3/views-of-flamanville-3";
        String site="http://localhost/basic.test";
        
        //Create the crawler
        WebCrawler myCrawler = new WebCrawler(site);
        
        //search the site
        myCrawler.crawl(myCrawler.url);                                     
        //create the downloader
        ImageDownloader myDownloader = new ImageDownloader(myCrawler.links, path);
        //download to path
        myDownloader.download();
        //create the PDFdownloader
        PDFdownloader myPDFdownloader = new PDFdownloader(myCrawler.pdfs,myCrawler.pdfnames, path);
        //download pdf to path
        myPDFdownloader.download();
        
        //create the PPTcreator
        PPTcreator myPPTcreator = new PPTcreator(myCrawler.heads,myCrawler.descrs,myDownloader.downloadedImages, path);
        //create the ppt
        myPPTcreator.createPPT();
    }
    
}
