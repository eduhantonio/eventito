package com.example.eventito;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventito.EventoAdapter;
import com.example.eventito.R;
import com.example.eventito.model.Evento;

import java.util.ArrayList;

public class Eventos_criados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_criados);

        // Referência à ListView no layout
        ListView listViewEventos = findViewById(R.id.listViewEventos);

        // Criar uma lista de eventos
        ArrayList<Evento> eventos = new ArrayList<>();

        // Configurar o adapter
        EventoAdapter adapter = new EventoAdapter(this, eventos);

        // Associar o adapter à ListView
        listViewEventos.setAdapter(adapter);
    }
}
