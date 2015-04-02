package com.study.raphaelpontes.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        String[] data = {
                "Seg 30/03 - Ensolarado - 31º/17º",
                "Ter 31/03 - Névoa - 21º/8º",
                "Qua 01/04 - Nublado - 22º/17º",
                "Qui 02/04 - Chuvoso - 18º/11º",
                "Sex 03/04 - Nevando - 21º/10º",
                "Sab 04/04 - PRESO NA ESTAÇAO DO TEMPO! HELP - 23º/18º",
                "Dom 05/04 - Ensolarado - 20º/7º"
        };
        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));

            /*
            Now that we have some dummy forecast data, create an ArrayAdapter.
            The ArrayAdapter will take data from a source (like our dummy forecast) and
            use it to populate the ListView it's attached to.
            */
        ArrayAdapter<String> forecastAdapter;
        forecastAdapter = new ArrayAdapter<>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_forecast, // The name of the layout ID.
                R.id.list_item_forecast_textview, // The ID of the textview to populate.
                weekForecast);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);

        return rootView;


    }

    public class FetchWeatherTask extends AsyncTask<Void, Void, Void>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {
            // Declarando essas duas linhas linhas fora do try/catch
            // para poder fechar no finally
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {
                // Possiveis parametros estao na pagina de forecasta da API OWM
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=brasilia,br&mode=json&units=metric&cnt=7&lang=pt");

                // Cria o request para OpenWeatherMap e abre a conexao
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Adicionando uma quebra pra facilitar logs e debug
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
            }catch (MalformedURLException | ProtocolException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error fechando stream! ", e);
                    }
                }
            }
            return null;
        }
    }
}

