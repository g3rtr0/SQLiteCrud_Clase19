package com.example.sqlitecrud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    ArrayList<Contacto> lista;
    daoContacto dao;
    Contacto c;
    Activity a;
    int id=0;

    public Adaptador(ArrayList<Contacto> lista, daoContacto dao, Activity a) {
        this.lista = lista;
        this.dao = dao;
        this.a = a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount(){
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        c=lista.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        c= lista.get(position);
        return c.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater li = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item,null);
        }
        c=lista.get(position);
        TextView nombre = v.findViewById(R.id.txt_nombre);
        TextView apellido = v.findViewById(R.id.txt_apellido);
        TextView email = v.findViewById(R.id.txt_email);
        TextView telefono = v.findViewById(R.id.txt_telefono);
        TextView ciudad = v.findViewById(R.id.txt_ciudad);
        Button editar = v.findViewById(R.id.btn_editar);
        Button eliminar = v.findViewById(R.id.btn_eliminar);
        nombre.setText(c.getNombre());
        apellido.setText(c.getApellido());
        email.setText(c.getEmail());
        telefono.setText(c.getTelefono());
        ciudad.setText(c.getCiudad());
        editar.setTag(position);
        eliminar.setTag(position);
        editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(convertView.getTag().toString());
                final Dialog dialog = new Dialog(a);
                dialog.setTitle("Eidtar Registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogo);
                dialog.show();
                final EditText nombre = dialog.findViewById(R.id.et_nombre);
                final EditText apellido = dialog.findViewById(R.id.et_apellido);
                final EditText email = dialog.findViewById(R.id.et_email);
                final EditText telefono = dialog.findViewById(R.id.et_telefono);
                final EditText ciudad = dialog.findViewById(R.id.et_ciudad);
                Button guardar = dialog.findViewById(R.id.btn_agregar);
                Button cancelar = dialog.findViewById(R.id.btn_cancelar);
                c=lista.get(pos);
                setId(c.getId());
                nombre.setText(c.getNombre());
                apellido.setText(c.getApellido());
                email.setText(c.getEmail());
                telefono.setText(c.getTelefono());
                ciudad.setText(c.getCiudad());
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            c = new Contacto(getId(),nombre.getText().toString(),
                            apellido.getText().toString(),email.getText().toString(),
                            telefono.getText().toString(),ciudad.getText().toString());
                            dao.editar(c);
                            lista = dao.verTodo();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(a, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(convertView.getTag().toString());
                        c = lista.get(position);
                        setId(c.getId());
                        AlertDialog.Builder del = new AlertDialog.Builder(a);
                        del.setMessage("Estas seguro de eliminar");
                        del.setCancelable(false);
                        del.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.eliminar(getId());
                                lista=dao.verTodo();
                                notifyDataSetChanged();
                            }
                        });
                        del.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        del.show();
                    }
                });
            }
        });
        return v;
    }
}
