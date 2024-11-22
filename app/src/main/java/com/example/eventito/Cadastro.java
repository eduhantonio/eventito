package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Declaração dos campos
    EditText edtNome;
    EditText edtEmailCadastro;
    EditText edtSenhaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        // Inicialização dos campos
        edtNome = findViewById(R.id.edtNome);
        edtEmailCadastro = findViewById(R.id.edtEmailCadastro);
        edtSenhaCadastro = findViewById(R.id.edtSenhaCadastro);


    }

    public void cadastrarUsuario(View view) {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmailCadastro.getText().toString().trim();
        String senha = edtSenhaCadastro.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Log.w("Cadastro", "Preencha todos os campos.");
            return;  // Impede o envio de dados vazios
        }

        // Criação do mapa com os dados do usuário
        Map<String, Object> newuser = new HashMap<>();
        newuser.put("nome_usuario", nome);
        newuser.put("email_usuario", email);
        newuser.put("senha_usuario", senha);
        newuser.put("xp_usuario", 0);
        Log.d("Cadastro", "Dados preparados: " + newuser);

        // Adiciona o usuário ao Firebase
        db.collection("Usuarios")
                .add(newuser)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firebase", "Usuário adicionado com ID: " + documentReference.getId());

                    // Redireciona para a Activity de Login após o cadastro
                    Intent intent = new Intent(Cadastro.this, MainActivity.class);
                    startActivity(intent);
                    finish();  // Finaliza a Activity de Cadastro
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erro ao adicionar usuário", e);
                });
    }

}
