package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.CaptureActivity;

public class Scanear extends AppCompatActivity {
    private TextView txtResultado;
    private String scannedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scanear);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialize o TextView aqui
        txtResultado = findViewById(R.id.txtResultado);

        // Verifique se o resultado já está armazenado
        if (scannedResult != null) {
            txtResultado.setText("Resultado: " + scannedResult);
        } else {
            startScan();  // Inicia o scanner QR
        }
    }

    private void startScan() {
        Intent intent = new Intent(Scanear.this, CaptureActivity.class);
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifique se o resultado da leitura é OK
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("SCAN_RESULT");
            scannedResult = result;  // Armazena o resultado

            // Verifique se o TextView foi inicializado corretamente
            if (txtResultado != null) {
                txtResultado.setText("Resultado: " + scannedResult);
            } else {
                Log.e("MainActivity", "TextView não inicializado.");
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Caso o usuário pressione "voltar", reinicia o escaneamento
        super.onBackPressed();
        scannedResult = null;  // Limpa o resultado armazenado
        startScan();
    }
}