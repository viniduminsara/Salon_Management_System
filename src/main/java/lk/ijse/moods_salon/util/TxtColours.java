package lk.ijse.moods_salon.util;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;

public class TxtColours {
    private static final Paint focusColour = Paint.valueOf("#4059a9");
    private static final Paint unFocusColour = Paint.valueOf("#4d4d4d");

    public static void setDefaultColours(JFXTextField textField){
        textField.setFocusColor(focusColour);
        textField.setUnFocusColor(unFocusColour);
    }

    public static void setErrorColours(JFXTextField textField){
        textField.setFocusColor(Paint.valueOf("Red"));
        textField.setUnFocusColor(Paint.valueOf("Red"));
        textField.requestFocus();
    }

    public static void setErrorColours(JFXPasswordField txtpassword) {
        txtpassword.setFocusColor(Paint.valueOf("Red"));
        txtpassword.setUnFocusColor(Paint.valueOf("Red"));
        txtpassword.requestFocus();
    }
}
