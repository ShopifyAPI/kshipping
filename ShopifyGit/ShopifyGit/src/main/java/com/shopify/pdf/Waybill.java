package com.shopify.pdf;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import io.micrometer.core.instrument.util.StringUtils;

public class Waybill {
	

	public static int computeNext(int distance, int delta) {
		int value = distance - delta;
		return -1 * value;
	}


	public static int blockText(List<String> lines, PDPageContentStream contentStream, PDFont pdfFont, float fontSize, float startX, float startY) throws IOException {
		
		int count = 0;
		float leading = (int)1.0f * fontSize;
		
        contentStream.setFont(pdfFont, fontSize);
        contentStream.newLineAtOffset(startX, startY);
        
		for (String line : lines) {
			contentStream.showText(line);
			contentStream.newLineAtOffset(0, -leading);
			count++;
		}
		
		return (int)(count * leading);
	}

	public static int wrapText(String text, PDPageContentStream contentStream, PDFont pdfFont, float fontSize, float width, int max) throws IOException {
		
		int count = 0;
		float leading = 1.0f * fontSize;
		List<String> lines = new ArrayList<String>();
		int lastSpace = -1;
		while (text.length() > 0) {
			int spaceIndex = text.indexOf(' ', lastSpace + 1);
			if (spaceIndex < 0)
				spaceIndex = text.length();
			String subString = text.substring(0, spaceIndex);
			float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
//			System.out.printf("'%s' - %f of %f\n", subString, size, width);
			if (size > width) {
				if (lastSpace < 0)
					lastSpace = spaceIndex;
				subString = text.substring(0, lastSpace);
				lines.add(subString);
				text = text.substring(lastSpace).trim();
//				System.out.printf("'%s' is line\n", subString);
				lastSpace = -1;
			} else if (spaceIndex == text.length()) {
				lines.add(text);
//				System.out.printf("'%s' is Last line\n", text);
				text = "";
			} else {
				lastSpace = spaceIndex;
			}
		}

		if ( lines.size() > max ) {
			List<String> sub = new ArrayList<>();
			sub = lines.subList(0, max - 1);
			String last = lines.get(max - 1);
			for( int i = max; i < lines.size(); i++ ) {
				last += " " + lines.get(i);
			}
			sub.add(last);
			lines = sub;
		}
		
		contentStream.setFont(pdfFont, fontSize);
		for (String line : lines) {
			contentStream.showText(line);
			contentStream.newLineAtOffset(0, -leading);
			count++;
		}
		
//		System.out.println("return (int)(count =" + count + ", leading = " + leading);
		return (int)(count * leading);
	}
	

	public static void printBarcode39(PDDocument document, PDPage page, String text, float x, float y, float w, float h) {
		try {

			PDPageContentStream contentStream = new PDPageContentStream(document, page,
					PDPageContentStream.AppendMode.APPEND, true);

			int dpi = 300;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			Code39Bean code39Bean = new Code39Bean();
			code39Bean.generateBarcode(canvas, text.trim());
			canvas.finish();
			BufferedImage bImage = canvas.getBufferedImage();

			PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
			contentStream.drawImage(image, x, y, w, h);
			contentStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printBarcode25(PDDocument document, PDPage page, String text, float x, float y, float w, float h) {
		if ( StringUtils.isBlank(text)) {
			return;
		}
		
		try {
			
			PDPageContentStream contentStream = new PDPageContentStream(document, page,
					PDPageContentStream.AppendMode.APPEND, true);
			
			int dpi = 300;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			Interleaved2Of5Bean itf = new Interleaved2Of5Bean();
			itf.generateBarcode(canvas, text.trim());
			canvas.finish();
			BufferedImage bImage = canvas.getBufferedImage();
			
			PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
			contentStream.drawImage(image, x, y, w, h);
			contentStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}