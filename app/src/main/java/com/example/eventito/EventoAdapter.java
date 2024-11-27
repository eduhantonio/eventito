package com.example.eventito;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventito.model.Evento;

import java.util.List;

public class EventoAdapter extends BaseAdapter {

    private Context context;
    private List<Evento> eventos;

    // Construtor do Adapter
    public EventoAdapter(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public int getCount() {
        return eventos.size(); // Retorna o número de itens na lista
    }

    @Override
    public Object getItem(int position) {
        return eventos.get(position); // Retorna o objeto do evento na posição especificada
    }

    @Override
    public long getItemId(int position) {
        return position; // Retorna a posição como ID do item
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Verificar se o layout precisa ser inflado
        if (convertView == null) {
            // Inflar o layout do card
            convertView = LayoutInflater.from(context).inflate(R.layout.lista_eventos, parent, false);

            // Configurar o ViewHolder para armazenar as referências dos componentes
            holder = new ViewHolder();
            holder.imgFotoEvento = convertView.findViewById(R.id.evento_foto);
            holder.txtNomeEvento = convertView.findViewById(R.id.evento_nome);
            holder.txtDescricaoEvento = convertView.findViewById(R.id.evento_descricao);

            // Salvar o holder na tag da view
            convertView.setTag(holder);
        } else {
            // Recuperar o holder da tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Obter o evento atual
        Evento evento = eventos.get(position);

        // Preencher os dados do evento no card
        holder.imgFotoEvento.setImageResource(evento.getFoto()); // Configura a foto do evento
        holder.txtNomeEvento.setText(evento.getNome());          // Configura o nome do evento
        holder.txtDescricaoEvento.setText(evento.getDescricao()); // Configura a descrição do evento

        return convertView;
    }

    // ViewHolder para otimizar a performance da ListView
    static class ViewHolder {
        ImageView imgFotoEvento;
        TextView txtNomeEvento;
        TextView txtDescricaoEvento;
    }
}
