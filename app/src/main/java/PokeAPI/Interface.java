package PokeAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Interface {

    private static JSONObject toJSON(String uri) throws IOException, JSONException {
        String responseString = "";
        URL url = new URL(uri);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            responseString = readStream(connection.getInputStream());
        }else{
            return null;
        }

        JSONTokener tokener = new JSONTokener(responseString);
        return new JSONObject(tokener);
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    private static JSONArray getPokemonArray() throws IOException, JSONException {
        String uri = "https://pokeapi.co/api/v2/";
        JSONObject jsonObjList = toJSON(uri);
        return toJSON(jsonObjList.getString("pokemon")).getJSONArray("results");
    }

    public static Pokemon getPokemon(String search) throws IOException, JSONException {
        String clearString = search.toLowerCase().replaceAll("[^a-zA-Z]", "");
        JSONArray pokemonList = getPokemonArray();
        JSONObject jsonPokemon;
        for (int i = 0; i < pokemonList.length(); i++) {
            if (clearString.equals(pokemonList.getJSONObject(i).getString("name"))) {
                jsonPokemon = toJSON(pokemonList.getJSONObject(i).get("url").toString());
                jsonPokemon.put("name", pokemonList.getJSONObject(i).get("name"));
                return new Pokemon(jsonPokemon);
            }
        }
        return null;
    }

    public static Pokemon getPokemon(int id) throws IOException, JSONException {
        JSONArray pokemonList = getPokemonArray();
        JSONObject jsonPokemon;
        if (id < 0 || id > pokemonList.length()) {
            return null;
        }
        jsonPokemon = toJSON(pokemonList.getJSONObject(id).get("url").toString());
        jsonPokemon.put("name", pokemonList.getJSONObject(id).get("name"));
        return new Pokemon(jsonPokemon);
    }

    public static double attackMultiplier(String TEMP, Pokemon pokemon) {
        return Type.getMultiplier(TEMP, pokemon);
    }

}
