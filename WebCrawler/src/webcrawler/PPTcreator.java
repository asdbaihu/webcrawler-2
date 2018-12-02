
package webcrawler;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;


public class PPTcreator {
    ArrayList<String> headings;
    ArrayList<String> descriptions;
    ArrayList<String> images;
    String downloadPath ;
    
    
    //constructor
    public PPTcreator (ArrayList<String> headings, ArrayList<String> descriptions, ArrayList<String> images , String path)
    {
        this.headings=headings;
        this.descriptions=descriptions;
        this.images=images;
        downloadPath =path;
    }
    
    public void createPPT () {
        //Create ppt
        XMLSlideShow ppt = new XMLSlideShow();
        
        //getting the slide master object
        XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);

        //select a layout from specified list
        XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);

        
        
        //add slides as number of images
        for (int i=0;i<images.size();i++)
        {
            //Add Slide
            XSLFSlide slide = ppt.createSlide(slidelayout);
            
            //add image for each slide
            try{
            byte[] pictureData = IOUtils.toByteArray(new FileInputStream(images.get(i)));

             XSLFPictureData pd = ppt.addPicture(pictureData, PictureData.PictureType.PNG);
             XSLFPictureShape picture = slide.createPicture(pd);
             picture.setAnchor(new Rectangle(180,280,420, 235));
             
                
            }
            catch(IOException fnf){
                System.out.println("exception " +fnf);
            }
            
            //add heading for each slide
            if(headings.get(i)!=null)
            {
            //selection of title place holder
            XSLFTextShape title = slide.getPlaceholder(0);
            title.clearText();
            XSLFTextParagraph p1 = title.addNewTextParagraph();
            XSLFTextRun r1 = p1.addNewTextRun();
            
            title.setText(headings.get(i));
            }
            
            //add description for each slide
            if(descriptions.get(i)!=null)
            {
            //selection of body place holder
            XSLFTextShape body = slide.getPlaceholder(1);

            //clear the existing text in the slide
            body.clearText();
            XSLFTextParagraph p2 = body.addNewTextParagraph();
            XSLFTextRun r2 = p2.addNewTextRun();
            
            body.setText(descriptions.get(i));
            }
           
            
        }


        
        //Save to C:/imgROI/
        try{
        FileOutputStream out = new FileOutputStream(downloadPath + "/powerpoint.pptx");
        ppt.write(out);
        out.close();
        }
        catch(IOException fnf) {
            System.out.println("exception found " + fnf);

        }
    }
}
