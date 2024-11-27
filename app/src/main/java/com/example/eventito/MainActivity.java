package com.example.eventito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventito.model.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText edtEmail;
    EditText edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loginUsuario(View view){
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("Usuarios")
                .whereEqualTo("email_usuario", email)
                .whereEqualTo("senha_usuario", senha)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String idUsuario = document.getId();
                            String nomeUsuario = document.getString("nome_usuario");
                            String tipoUsuario = document.getString("tipo_usuario");
                            long xp_usuario = document.getLong("xp_usuario");

                            Usuario usuario = new Usuario(idUsuario, tipoUsuario, nomeUsuario,email, "", "", xp_usuario);
                            UsuarioManager.setUsuarioAtual(usuario);
                            Log.d("Login", "Usuário encontrado: " + document.getData());
                            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, PerfilUsuario.class);
                            intent.putExtra("Id", idUsuario);

                            startActivity(intent);
                            finish();
                            return;
                        }
                    } else {
                        Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Login", "Erro ao buscar usuário no Firestore", e);
                    Toast.makeText(this, "Erro ao realizar login. Tente novamente.", Toast.LENGTH_SHORT).show();
                });
    }



    public void irParaCadastro(View view) {
        Intent intent = new Intent(MainActivity.this, AdicionarEvento.class);
        startActivity(intent);
    }
}