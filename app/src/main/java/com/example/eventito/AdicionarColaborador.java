package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdicionarColaborador extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText editTextEmail;
    EditText editTextSenha;
    EditText editTextNomeEvento;
    Button buttonEnviar;
    String nomeEvento;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adicionar_colaborador);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        String nomeEvento = getIntent().getStringExtra("nomeEvento");
    }
    public void cadastrarColaborador(View view){
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        String tipoUsuario = "Colaborador";

        if (email.isEmpty() || senha.isEmpty()){
            Log.w("Cadastro", "Preencha todos os campos.");
            return;
        }
        Map<String, Object> newuser = new HashMap<>();
        newuser.put("nome_usuario", "");
        newuser.put("evento", nomeEvento);
        newuser.put("email_usuario", email);
        newuser.put("senha_usuario", senha);
        newuser.put("xp_usuario", 0);
        newuser.put("tipo_usuario", tipoUsuario);
        Log.d("Cadastro", "Dados preparados: " + newuser);
        db.collection("Usuarios")
                .add(newuser)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firebase", "Usuário adicionado com ID: " + documentReference.getId());


                    Intent intent = new Intent(AdicionarColaborador.this, AdicionarColaborador.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erro ao adicionar usuário", e);
                });
    }
}
