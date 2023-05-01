package lk.ijse.gdse.qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QrDecoder {
    public String decodeQRCode(File qrCodeImg) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImg);
        return decodeQRCode(bufferedImage);
    }

    //Credit : https://www.baeldung.com/java-generating-barcodes-qr-codes
    public static String decodeQRCode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }
}
