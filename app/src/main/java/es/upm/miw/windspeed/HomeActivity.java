package es.upm.miw.windspeed;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeActivity extends AppCompatActivity {

    private static final String API_BASE_URL = "https://api.openweathermap.org";
    private static final String LOG_TAG = "MiW";
    static String sIp = " 192.168.0.111";
    private Button mButtonSignOut;
    private TextView textViewName;
    private TextView textViewEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference tiemposDatabaseReference;
    private ChildEventListener mChildEventListener;
    private TextView textViewResponse;
    private EditText editTextLocation;
    private Button btnUpdate;
    private TextView textViewTemperature;
    private TextView textViewWrapUp;
    private TextView textViewVientoKm;
    private TextView textViewMarquee;
    private Double mediaTemperatura = 0.0;
    private Double vientoKm = 0.0;
    private ICountryRESTAPIService apiService;
    private Button btnLamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mButtonSignOut = (Button) findViewById(R.id.btnSignOut);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewWrapUp = (TextView) findViewById(R.id.textViewWrapUp);
        textViewVientoKm = (TextView) findViewById(R.id.textViewVientoKm);
        textViewMarquee = (TextView) findViewById(R.id.textViewMarquee);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        tiemposDatabaseReference = mDatabase.child("tiempos");
        btnLamp = (Button) findViewById(R.id.btnLamp);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast();
            }
        });

        btnLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLampLights();
                manageBlinkEffect();

            }
        });

        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, AuthActivity.class));
                finish();
            }
        });

        getUserInfo();
        getForecast();


    }

    private void manageBlinkEffect() {
        ObjectAnimator anim = ObjectAnimator.ofInt(btnLamp, "backgroundColor", Color.WHITE +
                Color.BLUE, Color.RED);
        anim.setDuration(800);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }


    private void getUserInfo() {
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "Hola " + snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();

                    textViewName.setText(name);
                    textViewEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void guardarDatosDatabase(Forecast forecast) {
        tiemposDatabaseReference.push().setValue(forecast);
    }

    public void getForecast() {
        // btb added for retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ICountryRESTAPIService.class);
        Log.i(LOG_TAG, "gettingForecast ");
        obtenerInfoPais();
        Toast.makeText(this, "Tiempo guardado en BBDD", Toast.LENGTH_LONG).show();
    }

    public void obtenerInfoPais() {
        Call<Forecast> call_async = apiService.getForecast();
        call_async.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                Log.i(LOG_TAG, "response => respuesta=" + response.body());

                Forecast forecast = response.body();
                textViewResponse.setText("");
                List<es.upm.miw.windspeed.List> hoursList;
                if (null != forecast) {
                    hoursList = forecast.getList();
                    guardarDatosDatabase(forecast);// guardando en base de datos
                    for (int i = 0; i < 9; i++) {
                        // kelvin to celsius
                        double dTemp = hoursList.get(i).getMain().getTemp() - 273.15;
                        double dTempRoundOff = Math.round(dTemp * 100) / 100;
                        mediaTemperatura += dTempRoundOff;
                        // velocidad
                        Double velocidad = hoursList.get(i).getWind().getSpeed();
                        vientoKm += velocidad;
                        textViewResponse.append(hoursList.get(i).getDtTxt() + " " + dTempRoundOff + "ºC Viento:" + velocidad + "km/h\n\n");
                    }
                    mediaTemperatura = mediaTemperatura / 9;
                    vientoKm = vientoKm / 9;
                    temperaturaViento(mediaTemperatura, vientoKm);

                    Log.i(LOG_TAG, "obtenerInfoPais => respuesta=" + forecast);
                } else {
                    textViewResponse.setText("error al recoger el tiempo");
                    Log.i(LOG_TAG, "error al recoger el tiempo");
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    public void temperaturaViento(Double mediaTemperatura, Double vientoKm) {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        textViewTemperature.setText("Temperatura media: " + numberFormat.format(mediaTemperatura) + "ºC");
        textViewVientoKm.setText("Velocidad media del viento: " + numberFormat.format(vientoKm) + "km/h");

        String textWrapUp;
        String textMarquee;

        if (mediaTemperatura < 18.0) {
            textWrapUp = "❄ ¡Hora de abrigarse!";
            textViewWrapUp.setTextColor(getResources().getColor(R.color.red));
        } else if (mediaTemperatura >= 18.0 && mediaTemperatura <= 22.0) {
            textWrapUp = "\uD83E\uDD76 Si eres friolero, abrígate";
            textViewWrapUp.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            textWrapUp = "☀️ Hace calor";
            textViewWrapUp.setTextColor(getResources().getColor(R.color.green));
        }

        if (vientoKm < 1) {
            textMarquee = "\uD83D\uDE0E No te preocupes por el toldo";
            textViewMarquee.setTextColor(getResources().getColor(R.color.green));
        } else if (vientoKm >= 1 && vientoKm <= 2) {
            textMarquee = "☝️ Deberías de subir el toldo por si acaso";
            textViewMarquee.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            textMarquee = "⏫ Sube el toldo y evita desperfectos";
            textViewMarquee.setTextColor(getResources().getColor(R.color.red));
        }

        textViewWrapUp.setText(textWrapUp);
        textViewMarquee.setText(textMarquee);
    }

    public void setLampLights() {
        //encender luz
        lampOn();
        //si no hace mucho viento, luz azul
        if (vientoKm < 1) {
            sendColor(new FeedbackColor(0, 0, 255));
        } else {
            //si sí hace viento, luz roja
            sendColor(new FeedbackColor(255, 0, 0));
        }
    }

    public void sendColor(FeedbackColor color) {
        FCColor fcc = new FCColor(sIp, "" + color.getR(), ""
                + color.getG(), "" + color.getB());
        new FeedbackLampManager().execute(fcc);
    }

    public void lampOn() {
        FCOn f = new FCOn(sIp);
        new FeedbackLampManager().execute(f);
    }

    public void apagarCubo() {
        FCOff f = new FCOff(sIp);
        new FeedbackLampManager().execute(f);
    }


}