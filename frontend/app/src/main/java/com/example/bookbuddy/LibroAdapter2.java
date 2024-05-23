package com.example.bookbuddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LibroAdapter2 extends RecyclerView.Adapter<LibroViewHolder2> {
    private List<LibroData> allLibros;
    private Activity activity;

    //Recibe la información que debemos mostrar
    public LibroAdapter2(List<LibroData> allLibros, Activity activity ) {
        this.allLibros = allLibros;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LibroViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_libro_view_holder2, parent, false);
        return new LibroViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder2 holder, int position) {
        LibroData dataToBeRendered = allLibros.get(position);
        holder.showData(dataToBeRendered, activity);
    }

    @Override
    public int getItemCount() {return allLibros.size();}

}
