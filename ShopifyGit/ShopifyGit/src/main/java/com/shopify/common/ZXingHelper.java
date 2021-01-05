package com.shopify.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.krysalis.barcode4j.BarcodeClassResolver;
import org.krysalis.barcode4j.DefaultBarcodeClassResolver;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ZXingHelper {
	
	/*
	 * qr코드를 이미지로 생성
	 */
    public static byte[] getQRCodeImage(String code, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
    
    /*
     * bar코드를 이미지로 생성
     */
    public static void getBarCodeImage(String code, String cour, int width, int height, HttpServletResponse res) {
        try {
            MultiFormatWriter zxingWriter  = new MultiFormatWriter();
            BitMatrix bitMatrix = zxingWriter.encode(code, BarcodeFormat.CODE_39, width, height);
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            
            MatrixToImageConfig conf         = new MatrixToImageConfig(0xFF000000, 0x00FFFFFF);      
            BufferedImage barcodeImage    = MatrixToImageWriter.toBufferedImage(bitMatrix, conf);
            BufferedImage combined          = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
            Graphics addImg                  = (Graphics) combined.getGraphics(); 
            addImg.drawImage(barcodeImage, 0, -(height/4), width+50, height, null);
            addImg.setColor(new Color(0xFF000000));
            addImg.setFont(new Font("고딕", Font.ITALIC, 15));
            int deltaWidth  = barcodeImage.getWidth() - addImg.getFontMetrics().stringWidth(code);
            if(deltaWidth > 150) {
            	deltaWidth = 5;
            } else {
            	deltaWidth = (int) Math.round(deltaWidth / 4);
            }
            addImg.drawString(code+" ("+cour+")",  deltaWidth, barcodeImage.getHeight()-2);
            //addImg.drawString(code+" ("+cour+")", Integer.parseInt((width / 1.5)+""), barcodeImage.getHeight()-2);
            ImageIO.write(combined, "png", res.getOutputStream()); 
            res.getOutputStream().flush();
            res.getOutputStream().close();
        } catch (Exception e) {
            
        }
    }
    
    /**
     * 바코드 생성
     * @param barcodeType
     * @param barcodeData
     * @param dpi
     */
    public static void getBarCodeImg(String barcodeData, String barcodeTxt, String barcodeType, String fileFormat, boolean isAntiAliasing, int dpi, String outputFile, int width, int height, HttpServletResponse res){
    	try {
        	AbstractBarcodeBean bean = null;

        	BarcodeClassResolver resolver = new DefaultBarcodeClassResolver();
        	Class clazz = resolver.resolveBean(barcodeType);
        	bean = (AbstractBarcodeBean)clazz.newInstance();
            bean.doQuietZone(true);
            
            //Open output file
            OutputStream out = new FileOutputStream(new File(outputFile));
            try {
                //Set up the canvas provider for monochrome JPEG output 
            	String mimeType = MimeTypes.expandFormat(fileFormat);
            	int imageType   = BufferedImage.TYPE_BYTE_BINARY;
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                        out, mimeType, dpi, imageType, isAntiAliasing, 0);
                
                //Generate the barcode
                bean.generateBarcode(canvas, barcodeData);
                BufferedImage barcodeImage = canvas.getBufferedImage();
                canvas.finish();
               
                
				/*Graphics addImg                  = (Graphics) barcodeImage.getGraphics(); 
				addImg.setColor(new Color(0xFF000000));
				addImg.setFont(new Font("고딕", Font.ITALIC, 10));
				//int deltaWidth  = barcodeImage.getWidth() - addImg.getFontMetrics().stringWidth(barcodeData);
				int deltaWidth = 245;
				
				addImg.drawString(" "+barcodeTxt,  deltaWidth, barcodeImage.getHeight());
				*/
                ImageIO.write(barcodeImage, "png", res.getOutputStream()); 
                res.getOutputStream().flush();
                res.getOutputStream().close();
                System.out.println("create image success.");
            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}