package com.example.eventito;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class CreatorTelaEvento extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Código para identificar o seletor de imagens
    private LinearLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criador_tela_evento);

        // Inicializa o container onde os elementos serão adicionados
        contentContainer = findViewById(R.id.content_container);

        // Configura a barra de navegação inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                //POR ALGUMA RAZÃO SWITCH NÃO FUNCIONA
                if (itemId == R.id.nav_add_title) {
                    addTitle();
                    return true;
                } else if (itemId == R.id.nav_add_paragraph) {
                    addParagraph();
                    return true;
                } else if (itemId == R.id.nav_add_image) {
                    openImagePicker();
                    return true;
                }
                return false;
            }
        });

    }

    // Função para adicionar um título
    private void addTitle() {
        TextView title = new TextView(this);
        title.setText("Novo Título");
        title.setTextSize(24f);
        title.setPadding(0, 16, 0, 16);

        contentContainer.addView(title);
    }

    // Função para adicionar um parágrafo
    private void addParagraph() {
        TextView paragraph = new TextView(this);
        paragraph.setText("Este é um novo parágrafo. Você pode personalizá-lo conforme necessário.");
        paragraph.setTextSize(16f);
        paragraph.setPadding(0, 8, 0, 8);

        contentContainer.addView(paragraph);
    }

    // Abre o seletor de imagens para o usuário escolher uma imagem
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData(); // URI da imagem selecionada

            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    addImage(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    // Adiciona a imagem escolhida ao layout
    private void addImage(Bitmap bitmap) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 16);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bitmap); // Define o Bitmap como a imagem da ImageView
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        contentContainer.addView(imageView);
    }
}
