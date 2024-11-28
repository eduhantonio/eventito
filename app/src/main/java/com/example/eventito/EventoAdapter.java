package com.example.eventito;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventito.model.Evento;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private Context context;
    private List<Evento> eventos;
    private OnButtonClickListener listener;

    public interface OnButtonClickListener{
        void onButtonClick(Evento evento);

    }

    // Construtor do Adapter
    public EventoAdapter(Context context, List<Evento> eventos, OnButtonClickListener listener) {
        this.context = context;
        this.eventos = eventos;
        this.listener = listener;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla o layout de item (o XML que você forneceu)
        View view = LayoutInflater.from(context).inflate(R.layout.lista_eventos, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder holder, int position) {
        // Obtém o evento atual
        Evento evento = eventos.get(position);

        // Configura os dados na ViewHolder
        holder.txtNomeEvento.setText(evento.getNome());
        holder.txtDescricaoEvento.setText(evento.getDescricao());

        // Se você tiver uma imagem base64 ou URL para a imagem do evento, pode configurar assim:
        if (evento.getFoto() != null) {
            // Converta a string base64 para imagem e defina na ImageView
            // Se for base64, use um método para decodificar e configurar a imagem
            byte[] decodedString = Base64.decode(evento.getFoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgFotoEvento.setImageBitmap(decodedByte);
        } else {
            // Se não houver imagem, defina uma imagem padrão
            holder.imgFotoEvento.setImageResource(R.drawable.ic_event); // Substitua pelo ícone adequado
        }

        holder.btnEntrarEvento.setOnClickListener( v ->{
            EventoManager.setEventoAtual(evento);
            Intent intent = new Intent(context, QrCode.class);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return eventos.size(); // Retorna o tamanho da lista de eventos
    }

    // ViewHolder para otimizar a performance
    public static class EventoViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFotoEvento;
        TextView txtNomeEvento;
        TextView txtDescricaoEvento;
        Button btnEntrarEvento;

        public EventoViewHolder(View itemView) {
            super(itemView);
            // Inicializa as views do item
            imgFotoEvento = itemView.findViewById(R.id.imgIconEvento);
            txtNomeEvento = itemView.findViewById(R.id.txtNomeEvento);
            txtDescricaoEvento = itemView.findViewById(R.id.txtDescricaoEvento);
            btnEntrarEvento = itemView.findViewById(R.id.btnEntrarEvento);
        }

    }
}
