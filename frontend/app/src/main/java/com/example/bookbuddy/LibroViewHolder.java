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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                InfoLibro infoLibro = new InfoLibro();
                String title = libro.getTitle();
                //Hacer un método que se comunique con el back y que me devuelva el id de un libro según
                //su título, y luego se lo pasas al fragment siguiente y vía
                Toast.makeText(activity, "Clicaste en el libro: "+title, Toast.LENGTH_SHORT).show();


                //AQUÍ TIENES QUE CAMBIAR EL ENDPOINT PARA QUE SE PASE
                //EL TÍTULO Y NO EL ID DE UN LIBRO PARA MOSTRAR SU INFORMACIÓN :)
               // InfoLibro infoLibro = new InfoLibro();
                Bundle bundle = new Bundle();
                bundle.putString("title", libro.getTitle());
                infoLibro.setArguments(bundle);



                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, infoLibro);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        //PRUEBA DE CLICAR EN EL BOTÓN EN VEZ DE EN EL ÍTEM

    }

    //Muestra la info básica del libro
    public void showData(LibroData data, Activity activity) {
        this.titulo.setText(data.getTitle());
        this.autor.setText(data.getAuthor());
        this.paginas.setText(String.valueOf(data.getNum_pages()));
        this.libro = data;
    }
}