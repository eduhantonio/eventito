package com.example.eventito;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventito.model.Evento;
import com.example.eventito.model.Usuario;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QrCode extends AppCompatActivity {
    Usuario usuarioatual = UsuarioManager.getUsuarioAtual();
    Evento eventoAtual = EventoManager.getEventoAtual();
    String token = usuarioatual.getIdUsuario() + "_" + eventoAtual.getNome();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrcode);
        Button buttonGenerateQR = findViewById(R.id.generateQrButton);
        ImageView imageViewqrCode = findViewById(R.id.qrCodeImageView);



        if (!token.isEmpty()) {
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(token, BarcodeFormat.QR_CODE, 250, 250);
                imageViewqrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        // Listener para o botão
        buttonGenerateQR.setOnClickListener(view -> {
            // Converta o QR Code em PDF
            Bitmap qrCodeBitmap = ((BitmapDrawable) imageViewqrCode.getDrawable()).getBitmap();
            saveQrCodeAsPdf(qrCodeBitmap);
        });
    }

    private void saveQrCodeAsPdf(Bitmap qrCodeBitmap) {
        // Crie um documento PDF
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 300, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Desenhe a imagem no PDF
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawBitmap(qrCodeBitmap, 25, 25, paint);
        pdfDocument.finishPage(page);

        // Salve o PDF na pasta Downloads (para Android 10 ou superior)
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "qrcode.pdf");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        // A partir do Android 10, use o MediaStore para acessar diretórios públicos
        try (OutputStream outputStream = getContentResolver().openOutputStream(
                getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues))) {
            if (outputStream != null) {
                pdfDocument.writeTo(outputStream);
                Toast.makeText(this, "PDF salvo na pasta Downloads", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Falha ao salvar o PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar o PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }
}
