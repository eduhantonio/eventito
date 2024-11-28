package com.example.eventito;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventito.model.Evento;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdicionarEvento extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout layoutImagens;
    private LinearLayout layoutTasks;
    private TextView txtTotalPontos;
    private int totalPontos = 0;
    EditText edtNomeEvento;
    private Bitmap imagemSelecionadaBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adicionar_evento);

        String layoutJson = getIntent().getStringExtra("layoutElements");
        if (layoutJson != null) {
            // Deserializa os dados do layout
            Type listType = new TypeToken<List<LayoutData.LayoutElement>>() {}.getType();
            List<LayoutData.LayoutElement> layoutElements = new Gson().fromJson(layoutJson, listType);
            // Agora você tem acesso aos dados do layout
            // Faça o que for necessário com layoutData
        }
        // Referências aos elementos do layout
        edtNomeEvento = findViewById(R.id.edtNomeEvento);
        EditText edtDescricaoEvento = findViewById(R.id.edtDescricaoEvento);
        Button btnUploadImagem = findViewById(R.id.btnUploadImagem);
        layoutImagens = findViewById(R.id.layoutImagens);
        layoutTasks = findViewById(R.id.layoutTasks);
        txtTotalPontos = findViewById(R.id.txtTotalPontos);

        // Lógica para upload de imagem
        btnUploadImagem.setOnClickListener(v -> openImagePicker());

        // Lógica para adicionar tarefa
        Button btnAdicionarTask = findViewById(R.id.btnAdicionarTask);
        EditText edtNomeTask = findViewById(R.id.edtNomeTask);
        EditText edtPtsText = findViewById(R.id.edtPtsText);

        btnAdicionarTask.setOnClickListener(v -> {
            String nomeTask = edtNomeTask.getText().toString();
            String pontos = edtPtsText.getText().toString();

            if (!nomeTask.isEmpty() && !pontos.isEmpty()) {
                int pontosInt = Integer.parseInt(pontos);

                // Criar novo TextView para a tarefa
                TextView taskView = new TextView(this);
                taskView.setText(nomeTask + " - " + pontos + " pontos");
                layoutTasks.addView(taskView);

                // Atualizar total de pontos
                totalPontos += pontosInt;
                txtTotalPontos.setText("Total de Pontos: " + totalPontos);
            }
        });

        // Lógica para salvar evento no Firebase
        Button btnSalvarEvento = findViewById(R.id.btnCriarEvento);
        btnSalvarEvento.setOnClickListener(v -> salvarEvento(edtNomeEvento, edtDescricaoEvento));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imagemSelecionadaUri = data.getData();
            if (imagemSelecionadaUri != null) {
                try {
                    imagemSelecionadaBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagemSelecionadaUri);

                    // Criar um novo ImageView e adicionar ao layout
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(imagemSelecionadaBitmap);
                    imageView.setPadding(0, 10, 0, 10);
                    layoutImagens.addView(imageView);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void IrParaEditorDeTelas(View view) {
        Intent intent = new Intent(AdicionarEvento.this, CreatorTelaEvento.class);
        startActivity(intent);
        finish();
    }

    public void IrParaAdicionarColaborador(View view){
        String nomeEvento = edtNomeEvento.getText().toString().trim();
        if (nomeEvento.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome do evento!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Exibe o nome do evento em um Toast
        Toast.makeText(this, "Nome do evento: " + nomeEvento, Toast.LENGTH_SHORT).show();

        Log.d("AdicionarEvento", "Nome do evento enviado: " + nomeEvento);

        Intent intent = new Intent(AdicionarEvento.this, AdicionarColaborador.class);
        // Passa o nome do evento para a próxima tela
        intent.putExtra("nomeEvento", nomeEvento);
        startActivity(intent);
    }

    private void salvarEvento(EditText edtNomeEvento, EditText edtDescricaoEvento) {
        String nomeEvento = edtNomeEvento.getText().toString().trim();
        String descricaoEvento = edtDescricaoEvento.getText().toString().trim();

        if (nomeEvento.isEmpty() || descricaoEvento.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recupera o layout (supondo que você tenha recebido o layout como JSON)
        String layoutJson = getIntent().getStringExtra("layoutElements");
        // Converter imagem para Base64
        String imagemBase64 = null;
        if (imagemSelecionadaBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagemSelecionadaBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imagemBytes = byteArrayOutputStream.toByteArray();
            imagemBase64 = Base64.encodeToString(imagemBytes, Base64.DEFAULT);

        }

        // Criar evento e incluir layout
        Map<String, Object> evento = new HashMap<>();
        evento.put("nome_evento", nomeEvento);
        evento.put("descricao_evento", descricaoEvento);
        evento.put("total_pontos", totalPontos);
        evento.put("imagem_evento", imagemBase64);
        evento.put("layout_evento", layoutJson); // Incluindo o layout do evento no campo "layout_evento"



        // Adicionar tarefas ao evento
        ArrayList<String> tarefas = new ArrayList<>();
        for (int i = 0; i < layoutTasks.getChildCount(); i++) {
            TextView taskView = (TextView) layoutTasks.getChildAt(i);
            tarefas.add(taskView.getText().toString());
        }
        evento.put("tarefas", tarefas);

        db.collection("Eventos")
                .add(evento)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Evento salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    // Redireciona para a tela principal
                    Intent intent = new Intent(AdicionarEvento.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao salvar o evento", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Erro ao salvar o evento", e);
                });
    }

}
