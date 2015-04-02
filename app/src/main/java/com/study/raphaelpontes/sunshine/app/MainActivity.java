package com.study.raphaelpontes.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            String[] data = {
                    "Mon 6/23 - Sunny - 31/17",
                    "Tue 6/24 - Foggy - 21/8",
                    "Wed 6/25 - Cloudy - 22/17",
                    "Thurs 6/26 - Rainy - 18/11",
                    "Fri 6/27 - Foggy - 21/10",
                    "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                    "Sun 6/29 - Sunny - 20/7"
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

            // Declarando essas duas linhas linhas fora do try/catch
            // para poder fechar no finally
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {
                // Possiveis parametros estao na pagina de forecasta da API OWM
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=brasilia,br&mode=json&units=metric&cnt=7");

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
            }catch (MalformedURLException e) {
                Log.e("PlaceholderFragment", "Error ", e);
            } catch (ProtocolException e) {
                Log.e("PlaceholderFragment", "Error ", e);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return rootView;
        }
    }
}
