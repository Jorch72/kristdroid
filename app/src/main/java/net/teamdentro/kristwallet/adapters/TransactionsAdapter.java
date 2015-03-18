package net.teamdentro.kristwallet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.accounts.CurrentAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.github.apemanzilla.kwallet.types.Transaction;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Transaction> items;
    private int lastPosition = -1;
    private int viewResource;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView amount;
        TextView date;
        RelativeLayout container;

        public ViewHolder(View view) {
            super(view);
            container = (RelativeLayout) view.findViewById(R.id.transactionLayout);
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            amount = (TextView) view.findViewById(R.id.amount);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions, int viewResource) {
        this.context = context;
        this.items = transactions;
        this.viewResource = viewResource;
    }

    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewResource, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        Transaction transaction = items.get(position);

        if (transaction.getAddr().equalsIgnoreCase("N/A(Mined)")) {
            holder.image.setImageResource(R.mipmap.krist_mined);
            holder.title.setText(context.getString(R.string.mined));
            holder.amount.setText(context.getString(R.string.balance, String.valueOf(transaction.getAmount())));
        } else if (transaction.getFromAddr().equals(account.getAddress())) {
            holder.image.setImageResource(R.mipmap.krist_red);
            holder.title.setText(context.getString(R.string.sent));
            holder.amount.setText(context.getString(R.string.transactionTo, String.valueOf(Math.abs(transaction.getAmount())), transaction.getToAddr()));
        } else if (transaction.getToAddr().equals(account.getAddress())) {
            holder.image.setImageResource(R.mipmap.krist_green);
            holder.title.setText(context.getString(R.string.received));
            holder.amount.setText(context.getString(R.string.transactionFrom, String.valueOf(transaction.getAmount()), transaction.getFromAddr()));
        }

        holder.date.setText(context.getString(R.string.placeholder, new SimpleDateFormat("dd MMM HH:mm").format(transaction.getTime())));

        setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}