package lk.ijse.gdse.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRGenerator {

    public static void generateQrCode(String text, int width, int height,String filepath) throws WriterException {
        QRCodeWriter qc = new QRCodeWriter();
        BitMatrix bm = qc.encode(text, BarcodeFormat.QR_CODE,width,height);
        Path path = FileSystems.getDefault().getPath(filepath);
        try {
            MatrixToImageWriter.writeToPath(bm,"PNG",path);
            new Alert(Alert.AlertType.CONFIRMATION,"Successfully saved as '"+filepath+"'.").show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
