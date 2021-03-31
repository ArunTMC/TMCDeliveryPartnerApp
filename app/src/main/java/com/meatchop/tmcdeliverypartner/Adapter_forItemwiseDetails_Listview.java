package com.meatchop.tmcdeliverypartner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class Adapter_forItemwiseDetails_Listview extends ArrayAdapter<Modal_AssignedOrders> {
    Context mContext;

    List<Modal_AssignedOrders> ordersList;

    public Adapter_forItemwiseDetails_Listview(Context mContext, List<Modal_AssignedOrders> ordersList) {
        super(mContext, R.layout.itemwisedetails_listviewitem, ordersList);

        this.mContext=mContext;
        this.ordersList=ordersList;



    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_AssignedOrders getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_AssignedOrders item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.itemwisedetails_listviewitem, (ViewGroup) view, false);
        final TextView itemName_widget = listViewItem.findViewById(R.id.itemName_widget);
        final TextView itemWeight_widget = listViewItem.findViewById(R.id.itemWeight_widget);
        final TextView itemQty_widget = listViewItem.findViewById(R.id.itemQty_widget);
        final TextView itemGst_widget = listViewItem.findViewById(R.id.itemGst_widget);
        final TextView itemSubtotal_widget = listViewItem.findViewById(R.id.itemSubtotal_widget);

        Modal_AssignedOrders modal_manageOrders_pojo_class = ordersList.get(pos);
        itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemName()));
        itemWeight_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight()));
        itemQty_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getQuantity()));
        itemGst_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getGstAmount()));
        itemSubtotal_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemFinalPrice()));
        return  listViewItem ;
    }

}
