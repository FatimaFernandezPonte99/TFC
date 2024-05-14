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

public class LibroViewHolder extends RecyclerView.ViewHolder {
    private TextView titulo;
    private TextView autor;
    private TextView paginas;
    //No sé si el botón hace falta
    private Button bot_ver_info;
    private LibroData libro;


    public LibroViewHolder(@NonNull View itemView) {
        super(itemView);
        titulo = (TextView) itemView.findViewById(R.id.titulo_libro);
        autor = (TextView) itemView.findViewById(R.id.autor_libro);
        paginas = (TextView) itemView.findViewById(R.id.paginas_libro);

        //Hacemos que al pulsar en un libro, se abra su info
        //ESTO YA LO HARÁS
        //itemView.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
               // AppCompatActivity activity = (AppCompatActivity) view.getContext();
               // Toast.makeText(activity, "Clicaste en un libro", Toast.LENGTH_SHORT).show();

                //AQUÍ TIENES QUE CAMBIAR EL ENDPOINT PARA QUE SE PASE
                //EL TÍTULO Y NO EL ID DE UN LIBRO PARA MOSTRAR SU INFORMACIÓN :)
               // InfoLibro infoLibro = new InfoLibro();
               // Bundle bundle = new Bundle();
               // bundle.putString("title", libro.getTitle());
                //infoLibro.setArguments(bundle);

               // FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
               // transaction.replace(R.id.fragmentContainer, infoLibro);
               // transaction.addToBackStack(null);
               // transaction.commit();
            //}
        //});
    }

    //Muestra la info básica del libro
    public void showData(LibroData data, Activity activity) {
        this.titulo.setText(data.getTitle());
        this.autor.setText(data.getAuthor());
        this.paginas.setText(String.valueOf(data.getNum_pages()));
        this.libro = data;
    }
}