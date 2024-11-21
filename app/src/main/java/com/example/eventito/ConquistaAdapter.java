package com.example.eventito;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventito.model.Conquista;

import java.util.List;

public class ConquistaAdapter extends ArrayAdapter<Conquista> {

    public ConquistaAdapter(Context context, List<Conquista> conquistas) {
        super(context, 0, conquistas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutilizar ou inflar o layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_conquistas, parent, false);
        }

        // Obter a conquista atual
        Conquista conquista = getItem(position);

        // Configurar os dados
        TextView nome = convertView.findViewById(R.id.conquista_nome);
        TextView descricao = convertView.findViewById(R.id.conquista_descricao);
        TextView pontos = convertView.findViewById(R.id.conquista_pontos);
        ImageView imagem = convertView.findViewById(R.id.conquista_icone);

        nome.setText(conquista.getNome());
        descricao.setText(conquista.getDescricao());
        pontos.setText(conquista.getPontosAtuais() + " / " + conquista.getPontosTotais());
        imagem.setImageResource(R.drawable.logo_eventito); // Imagem fixa para exemplo

        return convertView;
    }
}
