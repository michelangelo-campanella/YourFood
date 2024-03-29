package com.example.yourfood.ui.lista.edit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;
import com.example.yourfood.ui.lista.ListaFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class EditFragment extends Fragment {


    public String posizione;

    private EditFragmentModel editFragmentModel;
    private DatePickerDialog.OnDateSetListener mDateSetListenerAcquisto, mDateSetListenerScadenza;
    final Date c = Calendar.getInstance().getTime();
    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    final String currentDate = df.format(c);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        editFragmentModel = ViewModelProviders.of(this).get(EditFragmentModel.class);
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        final EditText nome_prodotto = root.findViewById(R.id.editTextNomeProdotto);
        final EditText data_acquisto = root.findViewById(R.id.editTextAcquisto);
        final EditText data_scadenza = root.findViewById(R.id.editTextScadenza);
        final Spinner pasto = root.findViewById(R.id.spinnerPasto);
        final Spinner categoria = root.findViewById(R.id.spinnerCategoria);
        final EditText num_quantita = root.findViewById(R.id.editTextQuantita);
        final EditText costo = root.findViewById(R.id.editTextCosto);
        final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + posizione);

        final Button back = root.findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment someFragment = new ListaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack("Modifica Prodotto");  // if written, this transaction will be added to backstack
                transaction.commit();


            }
        });

        ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String nome = dataSnapshot.child("Nome").getValue().toString();
                String scadenza = dataSnapshot.child("Data_scadenza").getValue().toString();
                String acquisto = dataSnapshot.child("Data_acquisto").getValue().toString();
                String costo1 = dataSnapshot.child("Costo").getValue().toString();
                String pasto1 = dataSnapshot.child("Pasto").getValue().toString();
                String categoria1 = dataSnapshot.child("Categoria").getValue().toString();
                String quantita = dataSnapshot.child("Quantita").getValue().toString();

                nome_prodotto.setText(nome);
                data_scadenza.setText(scadenza);
                data_acquisto.setText(acquisto);
                num_quantita.setText(quantita);
                costo.setText(costo1);
                int idPasto = Integer.parseInt(pasto1);
                pasto.setSelection(idPasto);
                int idCategoria = Integer.parseInt(categoria1);
                categoria.setSelection(idCategoria);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        DBRef.addListenerForSingleValueEvent(messageListener);


        Calendar c = Calendar.getInstance();
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        final int mMonth = c.get(Calendar.MONTH);
        final int mYear = c.get(Calendar.YEAR);

        final ImageButton btnDataAccquisto = root.findViewById(R.id.btnDataAcquisto);
        btnDataAccquisto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, mDateSetListenerAcquisto, mYear, mMonth, mDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListenerAcquisto = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mDay, int mMonth, int mYear) {
                // data_acquisto.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                String day = "" + mYear;
                String month = "" + (mMonth + 1);
                String year = "" + mDay;

                if (mYear > 0 && mYear < 10) {
                    day = "0" + day;
                }

                if (mMonth + 1 > 0 && mMonth + 1 < 10) {
                    month = "0" + month;
                }

                data_acquisto.setText(day + "/" + month + "/" + year);
                Calendar rightNow = Calendar.getInstance();
                int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
                int currentMounth = rightNow.get(Calendar.MONTH);
                int currentYear = rightNow.get(Calendar.YEAR);


                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputString1 = data_acquisto.getText().toString();
                String inputString2;

                try {

                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(currentDate);

                    long diff = date1.getTime() - date2.getTime();
                    //long diff=0;

                    if (diff > 0) {

                        data_acquisto.setText(null);
                        Toast.makeText(getActivity(), "Non puoi viaggiare nel tempo!", Toast.LENGTH_SHORT).show();

                    }

                } catch (ParseException e) {
                    data_acquisto.setText(null);
                    e.printStackTrace();
                }


            }
        };

        data_acquisto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputString1 = data_acquisto.getText().toString();


                try {

                    Date date1 = myFormat.parse(inputString1);
                    System.out.println(date1);

                } catch (ParseException e) {

                    data_acquisto.setText(null);
                    data_scadenza.setText(null);
                    Toast.makeText(getActivity(), "Inserire una data acquisto/scadenza valida: dd/mm/yy", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        final ImageButton btnDataScadenza = root.findViewById(R.id.btnDataScadenza);
        btnDataScadenza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, mDateSetListenerScadenza, mYear, mMonth, mDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListenerScadenza = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mDay, int mMonth, int mYear) {
                String day = "" + mYear;
                String month = "" + (mMonth + 1);
                String year = "" + mDay;

                if (mYear > 0 && mYear < 10) {

                    day = "0" + day;

                }

                if (mMonth + 1 > 0 && mMonth + 1 < 10) {

                    month = "0" + month;
                }

                data_scadenza.setText(day + "/" + month + "/" + year);

                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputString1 = data_acquisto.getText().toString();
                String inputString2 = data_scadenza.getText().toString();

                try {

                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();

                    if (diff < 0) {

                        data_scadenza.setText(null);
                        Toast.makeText(getActivity(), "Seleziona una data scadenza maggiore della data di acquisto!", Toast.LENGTH_SHORT).show();


                    }

                    System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                } catch (ParseException e) {
                    data_scadenza.setText(null);
                    e.printStackTrace();
                }

            }

        };


        data_scadenza.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                    SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String inputString1 = data_acquisto.getText().toString();
                    String inputString2 = data_scadenza.getText().toString();

                    try {

                        Date date1 = myFormat.parse(inputString1);
                        System.out.println(date1);
                        Date date2 = myFormat.parse(inputString2);
                        System.out.println(date2);
                        long diff = date2.getTime() - date1.getTime();

                        if (diff < 0) {

                            data_scadenza.setText(null);
                            Toast.makeText(getActivity(), "Seleziona una data scadenza maggiore della data di acquisto!", Toast.LENGTH_SHORT).show();

                        }

                        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    } catch (ParseException e) {

                        data_scadenza.setText(null);
                        Toast.makeText(getActivity(), "Inserire una data scadenza valida: dd/mm/yy", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        });


        final Button reset = root.findViewById(R.id.delete);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + posizione);
                DBRef.removeValue();

                Fragment someFragment = new ListaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        Button salva = root.findViewById(R.id.save);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((nome_prodotto.getText().toString().trim().length()) > 0 &&
                        (data_acquisto.getText().toString().trim().length()) > 0 &&
                        (data_scadenza.getText().toString().trim().length()) > 0 &&
                        (num_quantita.getText().toString().trim().length()) > 0 &&
                        (costo.getText().toString().trim().length()) > 0
                ) {


                    if (parseInt(num_quantita.getText().toString()) > 0 && parseFloat(costo.getText().toString()) > 0 && (nome_prodotto.getText().toString().trim().length()) > 0) {


                        final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti");


                        final String nomeProdotto = nome_prodotto.getText().toString();
                        final String dataScadenza = data_scadenza.getText().toString();
                        final String dataAcquisto = data_acquisto.getText().toString();
                        int idPasto = pasto.getSelectedItemPosition();
                        final String dataPasto = String.valueOf(idPasto);
                        int idCategoria = categoria.getSelectedItemPosition();
                        final String dataCategoria = String.valueOf(idCategoria);
                        final String dataQuantita = num_quantita.getText().toString();
                        final String dataCosto = costo.getText().toString();

                        ValueEventListener messageListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                DatabaseReference DBRef2 = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + posizione);
                                DBRef2.child("Nome").setValue(nomeProdotto);
                                DBRef2.child("Data_scadenza").setValue(dataScadenza);
                                DBRef2.child("Data_acquisto").setValue(dataAcquisto);
                                DBRef2.child("Pasto").setValue(dataPasto);
                                DBRef2.child("Categoria").setValue(dataCategoria);
                                DBRef2.child("Quantita").setValue(dataQuantita);
                                DBRef2.child("Costo").setValue(dataCosto);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        DBRef.addListenerForSingleValueEvent(messageListener);
                        // resetCampi();
                        Toast.makeText(getActivity(), "Prodotto modificato correttamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Inserisci una quantità o costo valida!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Dati mancanti", Toast.LENGTH_SHORT).show();
                }

                Fragment someFragment = new ListaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }


        });

        return root;

    }

}