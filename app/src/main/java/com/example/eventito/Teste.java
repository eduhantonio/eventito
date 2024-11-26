package com.example.eventito;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class Teste extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView imageView;  // ImageView para exibir a imagem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        // Referência para o ImageView
        imageView = findViewById(R.id.imageViewTeste);

        // Recuperar imagem do Firestore
        recuperarImagemDoFirestore();
    }

    private void recuperarImagemDoFirestore() {
        // Suponhamos que o evento tenha um ID que você quer recuperar
        String eventoId = "PnsAOcIlTTcIlj4bbDy7";  // Substitua pelo ID real do evento

        db.collection("Eventos").document(eventoId)  // Acesse o evento pelo ID
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Recupera a string Base64 armazenada no Firestore
                        String imagemBase64 = documentSnapshot.getString("imagem_evento");

                        if (imagemBase64 != null) {
                            // Converte a string Base64 de volta para um Bitmap
                            Bitmap bitmap = decodificarBase64ParaBitmap(imagemBase64);

                            // Exibe a imagem no ImageView
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(this, "Imagem não encontrada!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Documento não encontrado!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao recuperar a imagem", Toast.LENGTH_SHORT).show();
                });
    }

    // Função para decodificar a string Base64 e transformar em Bitmap
    private Bitmap decodificarBase64ParaBitmap(String imagemBase64) {
        byte[] imagemBytes = Base64.decode(imagemBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
    }
}