package com.zaidbinihtesham.seproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class generate_QR_Code extends AppCompatActivity {

    ImageView qr_code;
    Button btn_genertor;
    String sText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        btn_genertor = findViewById(R.id.btn_generate_qr);
        qr_code = findViewById(R.id.qr_code);

        btn_genertor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sText = RecyclerViewSpecificItem.qr_title;
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    //initialize bitmatrix
                    BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE,350,350);
                    //initialize barcode encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    //initialize bitmap
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    //set bitmap on imageView
                    qr_code.setImageBitmap(bitmap);
                    //initialize input manager
                    InputMethodManager manager = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE
                    );
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
