// Adaptador personalizado para exibir a lista de ameaças ambientais em uma ListView.
package br.feevale.ameacasambientaissqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AmeacaAmbientalAdapter extends ArrayAdapter<AmeacaAmbiental> {

    private final Context context;
    private final ArrayList<AmeacaAmbiental> ameacasList;

    // Construtor da classe AmeacaAmbientalAdapter.
    public AmeacaAmbientalAdapter(Context context, ArrayList<AmeacaAmbiental> ameacasList) {
        super(context, android.R.layout.simple_list_item_1, ameacasList);
        this.context = context;
        this.ameacasList = ameacasList;
    }

    @Override
    public int getCount() {
        return ameacasList.size();
    }

    @Override
    public AmeacaAmbiental getItem(int position) {
        return ameacasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            // Infla o layout do item da lista quando necessário.
            view = LayoutInflater.from(context).inflate(R.layout.list_item_ameaca, parent, false);
        }

        TextView enderecoTextView = view.findViewById(R.id.textViewEndereco);
        TextView dataTextView = view.findViewById(R.id.textViewData);
        TextView descricaoTextView = view.findViewById(R.id.textViewDescricao);

        AmeacaAmbiental ameaca = getItem(position);

        // Define os valores nos elementos de interface com base nos dados da ameaça.
        enderecoTextView.setText(ameaca.getEndereco());
        dataTextView.setText(ameaca.getData());
        descricaoTextView.setText(ameaca.getDescricao());

        return view;
    }
}
