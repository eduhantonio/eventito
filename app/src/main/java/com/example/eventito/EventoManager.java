package com.example.eventito;

import com.example.eventito.model.Evento;
import com.example.eventito.model.Usuario;

public class EventoManager {
    private static Evento eventoAtual;

    public  static Evento getEventoAtual(){
        return eventoAtual;
    }

    public static void setEventoAtual(Evento eventoAtual) {
        EventoManager.eventoAtual = eventoAtual;
    }
}
