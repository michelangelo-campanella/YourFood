package com.example.yourfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourfood.ui.edit.EditFragment;
import com.example.yourfood.ui.lista.ListaFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListDialog extends AppCompatDialogFragment implements DialogInterface {

    public String nome_prodotto;
    public String data_scadenza;
    public String data_acquisto;
    public String costo;
    public String categoria;

    //Todo: Mostrare il valore esatto dello spinner categoria e pasto

    public String pasto;
    public String quantita;
    public String count;
    public String posizione;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(nome_prodotto);
        builder.setMessage("Scadenza: " + data_scadenza +
                "\nAcquisto: " + data_acquisto +
                "\nPrezzo di acquisto: " + costo + "€ (1 quantità)" +
                "\nQuantità: " + quantita +
                "\nCategoria: " + categoria +
                "\nPasto: " + pasto +
                "\n\nE' consigliabile non farlo scadere!\n");
        builder.setPositiveButton("Consumato", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("Elimina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               delete();
               Toast.makeText(getActivity(), "This is a delete button", Toast.LENGTH_LONG).show();

                Fragment someFragment = new ListaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        builder.setNegativeButton("Modifica", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {




                Fragment someFragment = new EditFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                ((EditFragment) someFragment).posizione=posizione;

            }
        });


        return builder.create();

    }


    //
    private void delete() {


        final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/"+ posizione);
        DBRef.removeValue();

/*
        final DatabaseReference DBRef2 = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti");
        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String index = dataSnapshot.child("index").getValue().toString();
                Integer intIndex = parseInt(index);
                intIndex--;
                DBRef2.child("index").setValue(intIndex.toString());
            }
/*
        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String index = dataSnapshot.child("index").getValue().toString();
                String parentName = "Prodotto_" + index;
                // DatabaseReference DBRef2 = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + parentName);

                String nome= dataSnapshot.child("Prodotto_"+count).removeValue().toString();

                final Integer intIndex = parseInt(index);

                final String[] lista_nome = new String[intIndex];
                final String[] lista_dataScadenza= new String[intIndex];
                final String[] lista_dataAcquisto= new String[intIndex];
                final String[] lista_pasto= new String[intIndex];
                final String[] lista_costo= new String[intIndex];
                final String[] lista_categoria= new String[intIndex];

                final String[] lista_quantita= new String[intIndex];


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };DBRef2.addListenerForSingleValueEvent(messageListener);*/

    }

    @Override
    public void cancel() {


    }
}