package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventito.EventoAdapter;
import com.example.eventito.R;
import com.example.eventito.model.Conquista;
import com.example.eventito.model.Evento;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Eventos_criados extends AppCompatActivity {
    private RecyclerView recyclerViewEventos;
    private EventoAdapter eventoAdapter;
    List<Evento> eventosList = new ArrayList<>();
    private DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_criados);

        recyclerViewEventos = findViewById(R.id.recyclerViewEventos);
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(this));


        List<Evento> eventosList = new ArrayList<>();

        db.collection("Eventos")
                .get()
                .addOnSuccessListener(EventoSnapshots -> {
                    if (!EventoSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : EventoSnapshots) {
                            String nomeEvento = document.getString("nome_evento");
                            String descricaoEvento = document.getString("descricao_evento");
                            String imagemEvento = document.getString("imagem_evento");

                            if (nomeEvento != null && descricaoEvento != null) {
                                Evento evento = new Evento(imagemEvento, nomeEvento, descricaoEvento);
                                eventosList.add(evento);
                            }
                        }

                        // Agora, passamos a implementação de OnButtonClickListener
                        eventoAdapter = new EventoAdapter(this, eventosList, new EventoAdapter.OnButtonClickListener() {
                            @Override
                            public void onButtonClick(Evento evento) {
                                // Ação a ser realizada quando o botão for clicado
                                EventoManager.setEventoAtual(evento);
                                Intent intent = new Intent(Eventos_criados.this, QrCode.class);
                                startActivity(intent);
                                // Você pode adicionar mais ações aqui, como navegar para outra tela
                            }
                        });

                        recyclerViewEventos.setAdapter(eventoAdapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Eventos", "Erro ao buscar Eventos no Firestore", e);
                    Toast.makeText(this, "Erro ao carregar eventos. Tente novamente.", Toast.LENGTH_SHORT).show();
                });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //bottomNavigationView.inflateMenu(R.menu.navbar_inferior);

        // Configurar o ouvinte de seleção do BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                // Usar if em vez de switch
                if (itemId == R.id.nav_inferior_perfil) {
                    // Ação para "Perfil"
                    Intent intentPerfil = new Intent(Eventos_criados.this, PerfilUsuario.class);
                    startActivity(intentPerfil);
                    return true;
                } else if (itemId == R.id.nav_inferior_eventos) {
                    // Ação para "Eventos"
                    /*Intent intentEventos = new Intent(Eventos_criados.this, Eventos_criados.class);
                    startActivity(intentEventos);*/
                    return true;
                } else if (itemId == R.id.nav_inferior_criar) {
                    // Ação para "Eventos"
                    Intent intentEventos = new Intent(Eventos_criados.this, AdicionarEvento.class);
                    startActivity(intentEventos);
                    return true;
                }
                return false;
            }
        });

    }

}
