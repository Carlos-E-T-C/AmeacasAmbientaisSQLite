// Importações necessárias
package br.feevale.ameacasambientaissqlite;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

// Classe principal que representa a atividade principal da aplicação
public class MainActivity extends AppCompatActivity {

    // Lista de ameaças ambientais
    private ArrayList<AmeacaAmbiental> ameacasList;

    // Adaptador para a lista de ameaças ambientais
    private AmeacaAmbientalAdapter ameacaAmbientalAdapter;

    // Lançador de atividade para adicionar uma nova ameaça
    private ActivityResultLauncher<Intent> addAmeacaLauncher;

    // Lançador de atividade para editar uma ameaça existente
    private ActivityResultLauncher<Intent> editarAmeacaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização de elementos da interface
        ListView listView = findViewById(R.id.listView);
        Button btnNovaAmeaca = findViewById(R.id.btnNovaAmeaca);

        // Inicialização da lista de ameaças ambientais
        ameacasList = new ArrayList<>();

        // Inicialização do adaptador da lista
        ameacaAmbientalAdapter = new AmeacaAmbientalAdapter(this, ameacasList);
        listView.setAdapter(ameacaAmbientalAdapter);

        // Carregue todas as ameaças ambientais da base de dados
        List<AmeacaAmbiental> ameacas = new AmeacasSQLiteDatabase(this).getAllAmeacas();

        // Converta a lista para ArrayList
        ameacasList.addAll(ameacas);

        // Configuração do lançador de atividade para edição de ameaça
        editarAmeacaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            AmeacaAmbiental ameacaEditada = (AmeacaAmbiental) data.getSerializableExtra("ameaca");
                            if (ameacaEditada != null) {
                                // Atualize a ameaça no banco de dados
                                new AmeacasSQLiteDatabase(MainActivity.this).updateAmeaca(ameacaEditada);
                                // Encontre a ameaça na lista e atualize-a pelo ID
                                for (int i = 0; i < ameacasList.size(); i++) {
                                    if (ameacasList.get(i).getId() == ameacaEditada.getId()) {
                                        ameacasList.set(i, ameacaEditada);
                                        break;
                                    }
                                }
                                ameacaAmbientalAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
        );

        // Configuração do evento de clique na lista de ameaças para edição
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AmeacaAmbiental ameaca = ameacasList.get(position);
            Intent intent = new Intent(MainActivity.this, EditarAmeaca.class);
            intent.putExtra("ameaca", ameaca);

            editarAmeacaLauncher.launch(intent);
        });

        // Configuração do evento de clique longo na lista de ameaças para exclusão
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AmeacaAmbiental ameaca = ameacasList.get(position);
            long ameacaId = ameaca.getId();

            // Exclua a ameaça do banco de dados
            new AmeacasSQLiteDatabase(MainActivity.this).deleteAmeaca(ameacaId);

            // Remova a ameaça da lista e notifique o Adapter
            ameacasList.remove(ameaca);
            ameacaAmbientalAdapter.notifyDataSetChanged();
            return true;
        });

        // Configuração do lançador de atividade para adição de nova ameaça
        addAmeacaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        AmeacaAmbiental ameaca = (AmeacaAmbiental) data.getSerializableExtra("ameaca");

                        // Adicione a ameaça ao banco de dados e obtenha o ID retornado
                        long newAmeacaId = new AmeacasSQLiteDatabase(MainActivity.this).addAmeaca(ameaca);

                        // Defina o ID da nova ameaça com o ID retornado
                        ameaca.setId(newAmeacaId);

                        // Adicione a ameaça à lista e notifique o Adapter
                        ameacasList.add(ameaca);
                        ameacaAmbientalAdapter.notifyDataSetChanged();
                    }
                });

        // Configuração do botão "Nova Ameaça" para adicionar uma nova ameaça
        btnNovaAmeaca.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdicionarAmeaca.class);
            addAmeacaLauncher.launch(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                AmeacaAmbiental ameacaEditada = (AmeacaAmbiental) data.getSerializableExtra("ameaca");
                if (ameacaEditada != null) {
                    // Encontre a ameaça na lista e atualize-a pelo ID
                    for (int i = 0; i < ameacasList.size(); i++) {
                        if (ameacasList.get(i).getId() == ameacaEditada.getId()) {
                            ameacasList.set(i, ameacaEditada);
                            break;
                        }
                    }
                    ameacaAmbientalAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
