package com.example.eventito;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventito.model.Conquista;

import java.util.ArrayList;
import java.util.List;

public class PerfilUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // Criar conquistas fixas
        List<Conquista> conquistas = new ArrayList<>();
        conquistas.add(new Conquista("Visitante Assíduo", "Visite mais eventos para subir de nível. Eai, em quantos você consegue ir ?", 10, 10));
        conquistas.add(new Conquista("Apressadinho", "Seja um dos 10 primeiros a chegar", 3, 5));
        conquistas.add(new Conquista("Vou me matar...", "Não aguento mais tanto erroooo", 7, 10));

        // Configurar o Adapter
        ConquistaAdapter adapter = new ConquistaAdapter(this, conquistas);

        // Associar o Adapter à ListView
        ListView listaConquistas = findViewById(R.id.listaConquistas);
        listaConquistas.setAdapter(adapter);
    }

}