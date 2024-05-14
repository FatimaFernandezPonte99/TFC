package com.example.bookbuddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroViewHolder> {
    private List<LibroData> allLibros;
    private Activity activity;

    //Recibe la información que debemos mostrar
    public LibroAdapter(List<LibroData> allLibros, Activity activity ) {
        this.allLibros = allLibros;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_libro_view_holder, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        LibroData dataToBeRendered = allLibros.get(position);
        holder.showData(dataToBeRendered, activity);
    }

    @Override
    public int getItemCount() {return allLibros.size();}

}
