package net.teamdentro.kristwallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.krist.Node;

import java.util.ArrayList;

public class NodeAdapter extends ArrayAdapter<Node> {
    public NodeAdapter(Context context, ArrayList<Node> nodes) {
        super(context, 0, nodes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.two_line_list_item, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(android.R.id.text1);
        label.setText(node.name);
        TextView label2 = (TextView) convertView.findViewById(android.R.id.text2);
        label2.setText(getContext().getString(R.string.nodeInfo, node.owner, node.hosted));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Node node = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(android.R.id.text1);
        label.setText(node.name);
        return convertView;
    }
}