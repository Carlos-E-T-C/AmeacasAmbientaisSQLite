// Importações necessárias
package br.feevale.ameacasambientaissqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

// Classe para editar uma ameaça existente
public class EditarAmeaca extends AppCompatActivity {

    private EditText editEndereco;
    private EditText editData;
    private EditText editDescricao;
    private AmeacaAmbiental ameaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_ameaca);

        // Inicialização dos elementos da interface
        editEndereco = findViewById(R.id.editEndereco);
        editData = findViewById(R.id.editData);
        editDescricao = findViewById(R.id.editDescricao);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        // Obtenha os dados da ameaça a ser editada da Intent
        Intent intent = getIntent();
        ameaca = (AmeacaAmbiental) intent.getSerializableExtra("ameaca");

        if (ameaca != null) {
            // Preencha os campos de texto com os dados da ameaça existente
            editEndereco.setText(ameaca.getEndereco());
            editData.setText(ameaca.getData());
            editDescricao.setText(ameaca.getDescricao());
        }

        // Adicione um ouvinte de texto para formatar automaticamente a data (DD/MM/AAAA)
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

        btnAtualizar.setOnClickListener(v -> {
            // Obtenha os novos dados da ameaça dos campos de texto
            String novoEndereco = editEndereco.getText().toString();
            String novaData = editData.getText().toString();
            String novaDescricao = editDescricao.getText().toString();

            // Verifique se os campos não estão vazios e se a data é válida
            if (!novoEndereco.isEmpty() && isValidData(novaData) && !novaDescricao.isEmpty()) {
                // Atualize os dados da ameaça existente
                ameaca.setEndereco(novoEndereco);
                ameaca.setData(novaData);
                ameaca.setDescricao(novaDescricao);

                // Crie um Intent para retornar os dados da ameaça atualizada diretamente para a MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ameaca", ameaca);
                setResult(RESULT_OK, resultIntent);

                // Feche a activity e retorne para a MainActivity
                finish();
            } else {
                Toast.makeText(EditarAmeaca.this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
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
