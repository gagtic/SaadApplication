package com.example.saadapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    ArrayList<Doctor> arrayList = new ArrayList<>();
    View parent_view;
    private static final String TAG = "ListAdapter";

    public ListAdapter(ArrayList<Doctor> arrayList, View parent_view){
        this.arrayList = arrayList;
        this.parent_view = parent_view;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.speciality.setText(arrayList.get(position).getSpeciality());
        holder.address.setText(arrayList.get(position).getAddress());
        holder.name.setText(arrayList.get(position).getName());
        Picasso.get().load(arrayList.get(position).getImage_url()).into(holder.doctor_image);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(parent_view.getContext(), arrayList.get(position).getName() + " clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(parent_view.getContext(), ClickActivity.class);
                intent.putExtra("address", arrayList.get(position).getAddress());
                intent.putExtra("id", arrayList.get(position).getId());
                parent_view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, address, speciality;
        LinearLayout container;
        ImageView doctor_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_doctor);
            address = (TextView) itemView.findViewById(R.id.address_doctor);
            speciality = (TextView) itemView.findViewById(R.id.speciality_doctor);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            doctor_image = (ImageView) itemView.findViewById(R.id.doctor_image);
        }
    }
}
