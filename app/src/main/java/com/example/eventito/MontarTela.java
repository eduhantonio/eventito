package com.example.eventito;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventito.LayoutData;
import com.example.eventito.R;
import com.example.eventito.model.Evento;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MontarTela extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout layoutDinamico;
    Evento eventoAtual = EventoManager.getEventoAtual();
    Button buttonParticipar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_evento);

        db = FirebaseFirestore.getInstance();
        layoutDinamico = findViewById(R.id.layoutDinamico1);
        buttonParticipar = findViewById(R.id.buttonParticipar);

        // Obtemos o nome do evento
        String nomeDoEvento = eventoAtual.getNome();
        if (nomeDoEvento != null) {
            buscarElementosNoFirebase(nomeDoEvento);
        } else {
            Toast.makeText(this, "Nome do evento não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarElementosNoFirebase(String nomeDoEvento) {
        db.collection("modificarEvento")
                .document(nomeDoEvento)
                .collection("elementos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            LayoutData.LayoutElement layoutElement = document.toObject(LayoutData.LayoutElement.class);
                            criarComponenteDinamico(layoutElement);
                        }
                    } else {
                        Toast.makeText(MontarTela.this, "Nenhum elemento encontrado!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MontarTela.this, "Erro ao buscar elementos", Toast.LENGTH_SHORT).show();
                });
    }

    private void criarComponenteDinamico(LayoutData.LayoutElement layoutElement) {
        if (layoutElement != null) {
            switch (layoutElement.getType()) {  // Verifica o tipo do componente
                case "title":
                    // Criar um TextView para título
                    TextView titleTextView = new TextView(this);
                    titleTextView.setText(layoutElement.getContent());
                    titleTextView.setTextSize(24);
                    titleTextView.setTextColor(Color.BLACK);
                    layoutDinamico.addView(titleTextView);
                    break;

                case "paragraph":
                    // Criar um TextView para parágrafo
                    TextView paragraphTextView = new TextView(this);
                    paragraphTextView.setText(layoutElement.getContent());
                    paragraphTextView.setTextSize(16);
                    paragraphTextView.setTextColor(Color.DKGRAY);
                    layoutDinamico.addView(paragraphTextView);
                    break;

                case "image":
                    // Criar um ImageView para imagem
                    ImageView imageView = new ImageView(this);

                    // Decodificar a imagem de Base64
                    String base64Image = layoutElement.getContent();
                    if (base64Image != null && !base64Image.isEmpty()) {
                        Bitmap bitmap = decodeBase64ToBitmap(base64Image);
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);  // Define a imagem no ImageView
                        }
                    }
                    layoutDinamico.addView(imageView);
                    break;

                default:
                    break;
            }
        }
    }

    // Função para decodificar uma string Base64 e retornar um Bitmap
    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;  // Retorna null caso a string Base64 seja inválida
        }
    }

    public void gerarQrcode(View view){
        buttonParticipar.setText("Ver QrCode");
        Intent intent = new Intent(MontarTela.this, QrCode.class);
        startActivity(intent);
    }
}
