package ch.zhaw.it.pm2.receiptsplitter.service;

import net.sourceforge.tess4j.Tesseract;
import java.io.File;

public class ImageExtractor {
    Tesseract tesseract = new Tesseract();

    public  ImageExtractor() {}

    public String extractOCR(File file) {
        return "OCR";
    }
}
