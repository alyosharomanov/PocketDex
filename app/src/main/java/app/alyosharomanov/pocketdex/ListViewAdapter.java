package app.alyosharomanov.pocketdex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import PokeAPI.Pokemon;
import app.alyosharomanov.pocketdex.PokemonNames;
import app.alyosharomanov.pocketdex.R;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<PokemonNames> PokemonNamesList = null;
    private ArrayList<PokemonNames> arraylist;

    public ListViewAdapter(Context context, List<PokemonNames> PokemonNamesList) {
        mContext = context;
        this.PokemonNamesList = PokemonNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PokemonNames>();
        this.arraylist.addAll(PokemonNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return PokemonNamesList.size();
    }

    @Override
    public PokemonNames getItem(int position) {
        return PokemonNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText("#" + (PokemonNamesList.get(position).getPokemonNumber() + 1) + " " + PokemonNamesList.get(position).getPokemonNames().substring(0, 1).toUpperCase() + PokemonNamesList.get(position).getPokemonNames().substring(1));
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        PokemonNamesList.clear();
        if (charText.length() == 0) {
            PokemonNamesList.addAll(arraylist);
        } else {
            for (PokemonNames wp : arraylist) {
                if (charText.replaceAll("[0-9]", "").equals("")) {
                    if (wp.getPokemonNumber() + 1 == (Integer.valueOf(charText.replaceAll("[^0-9]", "")))) {
                        PokemonNamesList.add(wp);
                    }
                } else if (wp.getPokemonNames().toLowerCase(Locale.getDefault()).contains(charText)) {
                    PokemonNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}