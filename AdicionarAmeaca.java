// Classe responsável por permitir que os usuários adicionem uma nova ameaça ambiental.
package br.feevale.ameacasambientaissqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

public class AdicionarAmeaca extends AppCompatActivity {

    private EditText editEndereco;
    private EditText editData;
    private EditText editDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adicionar_ameaca);

        // Inicialização de elementos da interface
        editEndereco = findViewById(R.id.editEndereco);
        editData = findViewById(R.id.editData);
        editDescricao = findViewById(R.id.editDescricao);
        Button btnAdicionar = findViewById(R.id.btnAdicionar);

        // Adiciona um TextWatcher para formatar a data enquanto o usuário digita
        editData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2 || s.length() == 5) {
                    s.append('/');
                }
            }
        });

        // Configura o evento de clique do botão "Adicionar"
        btnAdicionar.setOnClickListener(v -> {
            // Obtenha os dados da nova ameaça a partir dos campos de texto
            String endereco = editEndereco.getText().toString();
            String data = editData.getText().toString();
            String descricao = editDescricao.getText().toString();

            // Verifique se os campos não estão vazios e se a data é válida
            if (!endereco.isEmpty() && isValidData(data) && !descricao.isEmpty()) {
                // Crie uma nova instância de AmeacaAmbiental
                AmeacaAmbiental novaAmeaca = new AmeacaAmbiental(endereco, data, descricao);

                // Crie um Intent para retornar os dados da nova ameaça para a MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ameaca", novaAmeaca);

                // Defina o resultado como RESULT_OK
                setResult(RESULT_OK, resultIntent);

                // Feche a activity e retorne para a MainActivity
                finish();
            } else {
                Toast.makeText(AdicionarAmeaca.this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Função para validar a data no formato DD/MM/AAAA
    private boolean isValidData(String data) {
        if (data.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            // Verifique se o dia, mês e ano são válidos
            if (ano >= 1000 && ano <= 9999 && mes >= 1 && mes <= 12) {
                int[] diasPorMes = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

                if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) {
                    diasPorMes[2] = 29; // Ano bissexto
                }

                return dia >= 1 && dia <= diasPorMes[mes];
            }
        }
        return false;
    }
}
