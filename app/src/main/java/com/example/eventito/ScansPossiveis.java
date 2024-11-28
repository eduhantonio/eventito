package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ScansPossiveis extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout layoutTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scans_possiveis);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        layoutTarefas = findViewById(R.id.layoutTarefas);

        // Buscar o nome do evento ou qualquer outro dado necessário
        String eventoId = getIntent().getStringExtra("eventoId"); // Supondo que você tenha o ID do evento

        if (eventoId != null) {
            carregarTarefas(eventoId);
        } else {
            Toast.makeText(this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarTarefas(String eventoId) {
        // Recupera as tarefas do Firestore
        db.collection("Eventos").document(eventoId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> tarefas = (List<String>) documentSnapshot.get("tarefas");

                        if (tarefas != null) {
                            // Cria um botão para cada tarefa
                            for (String tarefa : tarefas) {
                                Button btnTarefa = new Button(this);
                                btnTarefa.setText(tarefa);
                                btnTarefa.setOnClickListener(v -> abrirLeitorQRCode(tarefa));
                                layoutTarefas.addView(btnTarefa);
                            }
                        } else {
                            Toast.makeText(ScansPossiveis.this, "Nenhuma tarefa encontrada!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ScansPossiveis.this, "Erro ao carregar as tarefas!", Toast.LENGTH_SHORT).show();
                });
    }

    private void abrirLeitorQRCode(String tarefa) {
        // Passa a tarefa como parâmetro para o leitor de QR Code
        Intent intent = new Intent(ScansPossiveis.this, Scanear.class);
        intent.putExtra("tarefa", tarefa);
        startActivity(intent);
    }
}

