package com.example.bookbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LibroViewHolder2 extends RecyclerView.ViewHolder {
    private TextView titulo;
    private TextView autor;
    private TextView paginas;
    private LibroData libro;

    public LibroViewHolder2(@NonNull View itemView) {
        super(itemView);
        titulo = (TextView) itemView.findViewById(R.id.titulo_libro);
        autor = (TextView) itemView.findViewById(R.id.autor_libro);
        paginas = (TextView) itemView.findViewById(R.id.paginas_libro);


        //Hacemos que al pulsar en un libro, se abra su info
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                InfoLibroHistorial infoLibroHistorial = new InfoLibroHistorial();
                String title = libro.getTitle();
                //Toast.makeText(activity, "Clicaste en el libro: "+title, Toast.LENGTH_SHORT).show();



                Bundle bundle = new Bundle();
                bundle.putString("title", libro.getTitle());
                infoLibroHistorial.setArguments(bundle);



                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, infoLibroHistorial);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

    }

    //Muestra la info básica del libro
    public void showData(LibroData data, Activity activity) {
        this.titulo.setText(data.getTitle());
        this.autor.setText(data.getAuthor());
        this.paginas.setText(String.valueOf(data.getNum_pages()));
        this.libro = data;
    }
}