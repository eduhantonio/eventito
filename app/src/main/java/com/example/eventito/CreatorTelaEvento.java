package com.example.eventito;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatorTelaEvento extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Código para identificar o seletor de imagens
    private LinearLayout contentContainer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();  // Instância do Firestore
    private List<LayoutData.LayoutElement> layoutElements = new ArrayList<>();

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
                if (itemId == R.id.nav_add_title) {
                    promptForTitle();
                    return true;
                } else if (itemId == R.id.nav_add_paragraph) {
                    promptForParagraph();
                    return true;
                } else if (itemId == R.id.nav_add_image) {
                    openImagePicker();
                    return true;
                } else if(itemId == R.id.nav_pronto){
                    pronto();
                    return true;
                }
                return false;
            }
        });
    }

    // Exibe um diálogo para o usuário inserir o conteúdo do título
    private void promptForTitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Título");

        // Adiciona um campo de entrada no diálogo
        final EditText input = new EditText(this);
        input.setHint("Digite o título aqui");
        builder.setView(input);

        // Botão para adicionar o título
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleText = input.getText().toString().trim();
                if (!titleText.isEmpty()) {
                    addTitle(titleText);
                } else {
                    Toast.makeText(CreatorTelaEvento.this, "Título não pode estar vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botão para cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Exibe um diálogo para o usuário inserir o conteúdo do parágrafo
    private void promptForParagraph() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Parágrafo");

        // Adiciona um campo de entrada no diálogo
        final EditText input = new EditText(this);
        input.setHint("Digite o parágrafo aqui");
        builder.setView(input);

        // Botão para adicionar o parágrafo
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String paragraphText = input.getText().toString().trim();
                if (!paragraphText.isEmpty()) {
                    addParagraph(paragraphText);
                } else {
                    Toast.makeText(CreatorTelaEvento.this, "Parágrafo não pode estar vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botão para cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Função para adicionar um título com texto personalizado
    private void addTitle(String titleText) {
        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(24f);
        title.setPadding(0, 16, 0, 16);

        // Adiciona o título à lista de layout
        LayoutData.LayoutElement layoutElement = new LayoutData.LayoutElement("title", titleText);
        layoutElements.add(layoutElement);

        contentContainer.addView(title);
    }

    // Função para adicionar um parágrafo com texto personalizado
    private void addParagraph(String paragraphText) {
        TextView paragraph = new TextView(this);
        paragraph.setText(paragraphText);
        paragraph.setTextSize(16f);
        paragraph.setPadding(0, 8, 0, 8);

        // Adiciona o parágrafo à lista de layout
        LayoutData.LayoutElement layoutElement = new LayoutData.LayoutElement("paragraph", paragraphText);
        layoutElements.add(layoutElement);

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
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // Ajusta a imagem sem cortar

        // Converte o Bitmap para base64
        String base64Image = encodeImageToBase64(bitmap);

        // Adiciona a imagem codificada em base64 à lista de layout
        LayoutData.LayoutElement layoutElement = new LayoutData.LayoutElement("image", base64Image);
        layoutElements.add(layoutElement);

        contentContainer.addView(imageView);
    }

    // Função para converter Bitmap para base64
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // Compressão em PNG
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT); // Codifica em base64
    }

    private void pronto() {
        saveLayoutToFirebase();  // Salva o layout na coleção do Firestore
        Intent intent = new Intent(this, AdicionarEvento.class);
        intent.putExtra("layoutElements", new Gson().toJson(layoutElements)); // Passa o layout salvo para a próxima tela
        startActivity(intent);
    }

    // Exibe um diálogo para redimensionar a imagem
    private void promptResizeImage(ImageView imageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Redimensionar Imagem");

        // Layout para os controles deslizantes
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        // Controle deslizante para largura
        SeekBar widthSeekBar = new SeekBar(this);
        widthSeekBar.setMax(1000); // Define o máximo de largura (em pixels)
        widthSeekBar.setProgress(imageView.getWidth());
        layout.addView(widthSeekBar);

        // Controle deslizante para altura
        SeekBar heightSeekBar = new SeekBar(this);
        heightSeekBar.setMax(1000); // Define o máximo de altura (em pixels)
        heightSeekBar.setProgress(imageView.getHeight());
        layout.addView(heightSeekBar);

        builder.setView(layout);

        // Botão de confirmação
        builder.setPositiveButton("Redimensionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int width = widthSeekBar.getProgress();
                int height = heightSeekBar.getProgress();

                imageView.getLayoutParams().width = width;
                imageView.getLayoutParams().height = height;
                imageView.requestLayout();
            }
        });

        // Botão de cancelamento
        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    private void saveLayoutToFirebase() {
        // Salva os dados do layout no Firebase Firestore
        for (LayoutData.LayoutElement layoutElement : layoutElements) {
            DocumentReference docRef = db.collection("eventos").document();

            // Cria um mapa com os dados do layoutElement
            Map<String, Object> layoutData = new HashMap<>();
            layoutData.put("type", layoutElement.getType());
            layoutData.put("content", layoutElement.getContent());

            // Salva o mapa no Firestore
            docRef.set(layoutData)
                    .addOnSuccessListener(aVoid -> {
                        // Sucesso no envio
                        Toast.makeText(CreatorTelaEvento.this, "Layout salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Erro ao enviar
                        Toast.makeText(CreatorTelaEvento.this, "Erro ao salvar layout", Toast.LENGTH_SHORT).show();
                    });
        }
    }

}
