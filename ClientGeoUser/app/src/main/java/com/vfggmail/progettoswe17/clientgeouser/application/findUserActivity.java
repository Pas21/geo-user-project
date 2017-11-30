package com.vfggmail.progettoswe17.clientgeouser.application;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vfggmail.progettoswe17.clientgeouser.R;
import com.vfggmail.progettoswe17.clientgeouser.commons.ErrorCodes;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidDateException;
import com.vfggmail.progettoswe17.clientgeouser.commons.InvalidUsernameException;
import com.vfggmail.progettoswe17.clientgeouser.commons.Posizione;

import org.json.JSONArray;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.support.design.widget.Snackbar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class findUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText userC;
    private String username,password;
    private Gson gson;
    private Snackbar sn;
    private HashSet<Posizione> posizioni;
    SharedPreferences editor;
    public final static String prefName="Preference";
    private static TextView clock1;
    private static TextView clock2;
    private Button find;
    private Button time1,time2,date1,date2;
    private static int usedset=0;

    private static int myear1;
    private static int mmonth1;
    private static int mday1;
    private static int mhour1;
    private static int mminutes1;
    private static int myear2;
    private static int mmonth2;
    private static int mday2;
    private static int mhour2;
    private static int mminutes2;
    private static Spinner spinner;
    private static int item=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        clock1=(TextView) findViewById(R.id.find_date1);
        clock2=(TextView) findViewById(R.id.find_date2);
        find=(Button) findViewById(R.id.find_find_button);
        time1=(Button) findViewById(R.id.find_time1_buttonTime);
        time2=(Button) findViewById(R.id.find_time2_buttonTime);
        date1=(Button) findViewById(R.id.find_date1_buttonDate);
        date2=(Button) findViewById(R.id.find_date2_buttonDate);
        userC=(EditText) findViewById(R.id.find_username);


        final Calendar c = Calendar.getInstance();


        myear1 = c.get(Calendar.YEAR);
        mmonth1 = c.get(Calendar.MONTH);
        mday1 = c.get(Calendar.DAY_OF_MONTH);
        mhour1 = c.get(Calendar.HOUR_OF_DAY);
        mminutes1 = c.get(Calendar.MINUTE);
        myear2 = c.get(Calendar.YEAR);
        mmonth2 = c.get(Calendar.MONTH);
        mday2 = c.get(Calendar.DAY_OF_MONTH);
        mhour2 = c.get(Calendar.HOUR_OF_DAY);
        mminutes2 = c.get(Calendar.MINUTE);

        String text=myear1+"/"+(mmonth1+1)+"/"+mday1+" "+mhour1+":"+mminutes1+":00";
        clock1.setText(text);
        clock2.setText(text);


        spinner= (Spinner) findViewById(R.id.find_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.find_spinner_strings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                usedset=1;
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });


        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                usedset=1;
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                usedset=2;
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });


        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                usedset=2;
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });





        editor=getSharedPreferences(prefName,MODE_PRIVATE);

        username=editor.getString("username","");
        password=editor.getString("password","");




        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d1=null, d2=null;
                View parent = (View) findViewById(R.id.activity_find_user);
                SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                if(String.valueOf(userC.getText()).equals("")){
                    sn.make(parent,"inserire un utente da ricercare",Snackbar.LENGTH_SHORT);
                }
                try {
                    d1=format.parse(clock1.getText().toString());
                    d2=format.parse(clock2.getText().toString());
                } catch (ParseException e) {
                    sn.make(parent,"Formato data non valido",Snackbar.LENGTH_SHORT).show();
                }



                if(userC.getText().equals("") || clock1.getText().equals("") || clock2.getText().equals(""))
                    sn.make(parent,"Inserisci i dati",Snackbar.LENGTH_SHORT).show();

                else if(d2.before(d1)){
                    sn.make(parent,"Date non valide",Snackbar.LENGTH_SHORT).show();

                }


                else{
                    switch(item){
                        case 0: new findRestTask().execute(String.valueOf(userC.getText()), String.valueOf(clock1.getText()), String.valueOf(clock2.getText()));break;
                        case 1: new findRestTask().execute(String.valueOf(userC.getText()), String.valueOf(clock1.getText()), "null");break;
                        case 2: new findRestTask().execute(String.valueOf(userC.getText()), "null", String.valueOf(clock1.getText()));break;
                        default: break;

                    }

                }

            }

        });


    }

    public findRestTask createFindRestTask() {return new findRestTask();}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){
            clock2.setVisibility(View.VISIBLE);
            date2.setVisibility(View.VISIBLE);
            time2.setVisibility(View.VISIBLE);
            item=i;
        }else if (i==1){
            clock2.setVisibility(View.INVISIBLE);
            date2.setVisibility(View.INVISIBLE);
            time2.setVisibility(View.INVISIBLE);
            item=i;
        }else{
            clock2.setVisibility(View.INVISIBLE);
            date2.setVisibility(View.INVISIBLE);
            time2.setVisibility(View.INVISIBLE);
            item=i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class findRestTask extends AsyncTask<String, Void, Integer> {


        protected Integer doInBackground(String... params) {

            String data1S=params[1],data2S=params[2];
            data1S=data1S.replaceAll("/", "-");
            data2S=data2S.replaceAll("/", "-");
            data1S=data1S.replaceAll(" ", "_");
            data2S=data2S.replaceAll(" ", "_");
            String URI=mainPage.baseURI+"auth/positions/"+params[0]+"/"+data1S+"/"+data2S;
            ClientResource cr=new ClientResource(URI);
            String gsonResponse=null;
            posizioni=new HashSet<Posizione>(0);
            gson=new Gson();

            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,username,password);

            cr.setChallengeResponse(authentication);




            try{
                gsonResponse=cr.get().getText();
                JsonArray arrayJSON=new JsonParser().parse(gsonResponse).getAsJsonArray();

                for (JsonElement elemJSON: arrayJSON){
                    posizioni.add(gson.fromJson(elemJSON,Posizione.class));
                }
                if(cr.getStatus().getCode()== ErrorCodes.INVALID_USERNAME_CODE)
                    throw gson.fromJson(gsonResponse, InvalidUsernameException.class);
                else if(cr.getStatus().getCode()==ErrorCodes.INVALID_DATE_CODE)
                    throw gson.fromJson(gsonResponse, InvalidDateException.class);
                posizioni = gson.fromJson(gsonResponse, new TypeToken<HashSet<Posizione>>() {}.getType());

                return 0;

            }catch(InvalidUsernameException e2){
                return 1;
            } catch (ResourceException e1) {
                return 2;
            } catch (IOException e1) {
                return 2;
            } catch (InvalidDateException e) {
                return 3;
            }


        }


        protected void onPostExecute(Integer c) {
            View parent = (View) findViewById(R.id.activity_find_user);
            if (c == 0) {
                if(posizioni.isEmpty()){
                    sn.make(parent,"Nessuna posizione registrata dall'utente",Snackbar.LENGTH_SHORT);
                }else {
                    Intent myIntent = new Intent(findUserActivity.this, showMapActivity.class);
                    myIntent.putExtra("array", posizioni);
                    startActivity(myIntent);
                }
            } else if (c == 1) {
                sn.make(parent, "Utente non registrato", Snackbar.LENGTH_SHORT).show();
            } else if (c == 2) {
                sn.make(parent, "Errore", Snackbar.LENGTH_SHORT).show();
            } else if(c == 3){
                sn.make(parent, "Date in formato errato", Snackbar.LENGTH_SHORT).show();

            }

        }

    }



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(usedset==1) {
                myear1 = year;
                mmonth1 = month;
                mday1 = day;
                String text = myear1+"/"+(mmonth1+1)+"/"+mday1+" "+mhour1+":"+mminutes1+":00";
                clock1.setText(text);
            }
            else{
                myear2 = year;
                mmonth2 = month;
                mday2 = day;
                String text = myear2+"/"+(mmonth2+1)+"/"+mday2+" "+mhour2+":"+mminutes2+":00";
                clock2.setText(text);
            }


        }
    }



    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(usedset==1) {
                mhour1 = hourOfDay;
                mminutes1 = minute;
                String text = myear1+"/"+(mmonth1+1)+"/"+mday1+" "+mhour1+":"+mminutes1+":00";
                clock1.setText(text);
            }else{
                mhour2 = hourOfDay;
                mminutes2 = minute;
                String text = myear2+"/"+(mmonth2+1)+"/"+mday2+" "+mhour2+":"+mminutes2+":00";
                clock2.setText(text);

            }


        }
    }

}
