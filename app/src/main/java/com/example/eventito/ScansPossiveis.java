package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ScansPossiveis extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout layoutTarefas;
    public static String nomeEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scans_possiveis);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        layoutTarefas = findViewById(R.id.layoutTarefas);

        // Buscar o nome do evento ou qualquer outro dado necessário
        if(nomeEvento == null) {
            nomeEvento = getIntent().getStringExtra("evento"); // Supõe que o nome do evento foi passado pela Intent
        }

        if (nomeEvento != null) {
            buscarEventoPorNome(nomeEvento);
        }
    }

    private void buscarEventoPorNome(String nomeEvento) {
        // Consultar Firestore para encontrar o evento pelo campo "nome_evento"
        db.collection("Eventos")
                .whereEqualTo("nome_evento", nomeEvento)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Recuperar o ID do evento ou continuar com o documento encontrado
                            //carregarTarefas(document);
                            break; // Pega apenas o primeiro documento correspondente
                        }
                    } else {
                        Toast.makeText(this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao buscar evento!", Toast.LENGTH_SHORT).show();
                });
    }

    /*private void carregarTarefas(QueryDocumentSnapshot document) {
        List<String> tarefas = (List<String>) document.get("tarefas");

        if (tarefas != null) {
            // Cria um botão para cada tarefa
            for (String tarefa : tarefas) {
                Button btnTarefa = new Button(this);
                btnTarefa.setText(tarefa);
                btnTarefa.setOnClickListener(v -> abrirLeitorQRCode(tarefa));
                layoutTarefas.addView(btnTarefa);
            }
        } else {
            Toast.makeText(this, "Nenhuma tarefa encontrada!", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void abrirLeitorQRCode(String tarefa) {
        // Passa a tarefa como parâmetro para o leitor de QR Code
        Intent intent = new Intent(ScansPossiveis.this, Scanear.class);
        intent.putExtra("tarefa", tarefa);
        startActivity(intent);
    }

    public void registrarPresenca(View view) {
        Intent intent = new Intent(ScansPossiveis.this, Scanear.class);
        intent.putExtra("evento", nomeEvento);
        startActivity(intent);
    }
}
