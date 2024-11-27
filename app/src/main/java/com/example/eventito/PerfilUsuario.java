package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventito.model.Conquista;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PerfilUsuario extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Conquista> conquistas = new ArrayList<>();
    ConquistaAdapter adapter;
    Map<String, Long> pontosUsuario = new HashMap<>();
    String idUsuario;
    String idConquista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        idUsuario = getIntent().getStringExtra("Id");

        db.collection("UsuarioConquista")
            .whereEqualTo("id_usuario", idUsuario) // Usando o id do usuário
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        idConquista = document.getString("id_conquista");
                        long pontos = document.getLong("pontos");

                        // Adiciona o nome da conquista e seus pontos no mapa
                        pontosUsuario.put(idConquista, pontos);
                    }
                    db.collection("Conquistas")
                        .get()
                        .addOnSuccessListener(conquistaSnapshots -> {
                            if (!conquistaSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot document : conquistaSnapshots) {
                                    String idConquistaEncontrado = document.getId(); // Obtendo o id da conquista
                                    if(Objects.equals(idConquista, idConquistaEncontrado)){
                                        String nomeConquista = document.getString("nome_conquista"); // Nome da conquista
                                        String descricaoConquista = document.getString("descricao_conquista"); // Descrição da conquista
                                        int pontosTotais = Integer.parseInt(document.getLong("pontosTotais").toString());

                                        // Recupera os pontos do usuário para essa conquista
                                        int pontos =0;
                                        if(pontosUsuario.get(idConquista)!=0){
                                            pontos = Integer.parseInt(pontosUsuario.get(idConquista).toString());
                                        }

                                        // Criando o objeto Conquista com os dados
                                        Conquista conquista = new Conquista(nomeConquista, descricaoConquista, pontos, pontosTotais);

                                        // Adiciona a conquista à lista
                                        conquistas.add(conquista);
                                    }
                                }
                                adapter = new ConquistaAdapter(this, conquistas);
                                ListView listaConquistas = findViewById(R.id.listaConquistas);
                                listaConquistas.setAdapter(adapter);
                            } else {
                                Log.d("Conquista", "Nenhuma conquista encontrada.");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Conquista", "Erro ao buscar conquistas no Firestore", e);
                            Toast.makeText(this, "Erro ao carregar conquistas. Tente novamente.", Toast.LENGTH_SHORT).show();
                        });
                }
            });

        /*BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.inflateMenu(R.menu.navbar_inferior);

        // Configurar o ouvinte de seleção do BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_:  // Substitua com o id real do item no menu
                        // Ação para o item 1
                        Intent intent1 = new Intent(MainActivity.this, Activity1.class);
                        startActivity(intent1);
                        return true;

                    case R.id.item2:  // Substitua com o id real do item no menu
                        // Ação para o item 2
                        Intent intent2 = new Intent(MainActivity.this, Activity2.class);
                        startActivity(intent2);
                        return true;

                    // Adicione outros casos conforme necessário

                    default:
                        return false;
                }
            }
        });*/
    }
}
