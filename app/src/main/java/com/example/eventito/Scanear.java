package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.CaptureActivity;

public class Scanear extends AppCompatActivity {
    private TextView txtResultado;
    private String scannedResult;
    private String tarefa;
    private String evento;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idUsuario = "";
    String nomeEvento = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scanear);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(getIntent().getStringExtra("tarefa") != null){
            tarefa = getIntent().getStringExtra("tarefa");
        }

        if(getIntent().getStringExtra("evento") != null){
            evento = getIntent().getStringExtra("evento");
        }

        // Inicialize o TextView aqui
        txtResultado = findViewById(R.id.txtResultado);

        // Verifique se o resultado já está armazenado
        if (scannedResult != null) {
            txtResultado.setText("Resultado: " + scannedResult);
        } else {
            startScan();  // Inicia o scanner QR
        }
    }

    private void startScan() {
        Intent intent = new Intent(Scanear.this, CaptureActivity.class);
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifique se o resultado da leitura é OK
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("SCAN_RESULT");
            scannedResult = result;  // Armazena o resultado

            // Verifique se o TextView foi inicializado corretamente
            if (scannedResult != null) {
                //txtResultado.setText("Resultado: " + scannedResult);
                String[] parts = scannedResult.split("_");

                // Verifique se a string foi dividida corretamente
                if (parts.length == 2) {
                    idUsuario = parts[0];  // O primeiro elemento é o idUsuario
                    nomeEvento = parts[1];
                }

                /*db.collection("Eventos")
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

                                        // Verificando na coleção UsuarioEvento se o par idUsuario_nomeEvento existe
                                        String idUsuario = "id_do_usuario"; // Substitua com o id real do usuário
                                        checkUsuarioEventoExistence(idUsuario, nomeEvento, evento);
                                    }
                                }

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
                        });*/

                // Método para verificar se o registro existe na tabela UsuarioEvento
                //private void checkUsuarioEventoExistence(String idUsuario, String nomeEvento, Evento evento) {

                db.collection("UsuarioEvento")
                        .whereEqualTo("id_usuario", idUsuario)
                        .whereEqualTo("nome_evento", nomeEvento)
                        .get()
                        .addOnSuccessListener(usuarioEventoSnapshots -> {
                            if (!usuarioEventoSnapshots.isEmpty()) {
                                // Se o registro existir
                                //Log.d("UsuarioEvento", "Registro encontrado: " + idUsuario + " - " + nomeEvento);
                                // Aqui você pode adicionar o código para lidar com o evento encontrado,
                                QueryDocumentSnapshot document = (QueryDocumentSnapshot) usuarioEventoSnapshots.getDocuments().get(0);
                                Boolean check = document.getBoolean("check"); // Pegando o valor do campo "check"

                                if (check != null && check) {
                                    // Se o campo "check" já for true, não faça nada ou mostre uma mensagem
                                    Log.d("UsuarioEvento", "O evento já foi marcado como 'check' para o usuário.");
                                    txtResultado.setText("Usuário já esta registrado no evento!");
                                    Toast.makeText(Scanear.this, "Este evento já foi registrado.", Toast.LENGTH_SHORT).show();
                                } else {
                                    document.getReference().update("check", true)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("UsuarioEvento", "Campo 'check' atualizado para true");
                                                // Após a atualização, você pode realizar outras ações (como navegar para a tela do QR code)
                                                db.collection("Usuarios")
                                                        .document(idUsuario)
                                                        .get()
                                                        .addOnSuccessListener(userDocument -> {
                                                            if (userDocument.exists()) {
                                                                // Recuperando o nome e e-mail do usuário
                                                                String nomeUsuario = userDocument.getString("nome_usuario");
                                                                String emailUsuario = userDocument.getString("email_usuario");
                                                                txtResultado.setText("Usuario: " + nomeUsuario + "\nEmail: " + emailUsuario + "\nRegistrado no evento!");
                                                            }
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e("Usuario", "Erro ao buscar informações do usuário", e);
                                                            Toast.makeText(Scanear.this, "Erro ao buscar dados do usuário. Tente novamente.", Toast.LENGTH_SHORT).show();
                                                        });

                                                updateXpUsuario(idUsuario);

                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("UsuarioEvento", "Erro ao atualizar campo 'check'", e);
                                                Toast.makeText(Scanear.this, "Erro ao atualizar o evento. Tente novamente.", Toast.LENGTH_SHORT).show();
                                            });
                                }
                                // por exemplo, iniciar a tela do QR Code.
                            } else {
                                // Se o registro não existir
                                txtResultado.setText("Usuario não encontrado no evento");
                                Toast.makeText(Scanear.this, "Este evento não está vinculado ao seu usuário.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("UsuarioEvento", "Erro ao buscar UsuarioEvento no Firestore", e);
                            Toast.makeText(Scanear.this, "Erro ao verificar evento. Tente novamente.", Toast.LENGTH_SHORT).show();
                        });
                //}


            } else {
                Log.e("MainActivity", "TextView não inicializado.");
            }
        }
    }

    private void updateXpUsuario(String idUsuario) {
        db.collection("Usuarios")
                .document(idUsuario)
                .update("xp_usuario", FieldValue.increment(100))  // Supondo que você deseja adicionar 10 pontos ao XP
                .addOnSuccessListener(aVoid -> {
                    Log.d("Usuario", "XP do usuário atualizado com sucesso.");
                    // Aqui você pode adicionar mais ações após a atualização do XP, como navegar para outra tela ou atualizar a UI.
                })
                .addOnFailureListener(e -> {
                    Log.e("Usuario", "Erro ao atualizar XP do usuário", e);
                    Toast.makeText(Scanear.this, "Erro ao atualizar XP do usuário. Tente novamente.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        // Caso o usuário pressione "voltar", reinicia o escaneamento
        super.onBackPressed();
        scannedResult = null;  // Limpa o resultado armazenado
        startScan();
    }

    /*@Override
    public void onBackPressed() {
        // Cria uma nova Intent para a atividade que você quer abrir
        super.onBackPressed();
        Intent intent = new Intent(Scanear.this, ScansPossiveis.class);
        // Inicia a nova Activity
        startActivity(intent);
        // Opcionalmente, você pode finalizar a atividade atual, para que ela não apareça no histórico
        finish();
    }*/
}