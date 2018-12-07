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
import java.util.HashMap;

public class Interface {

    final static int MAX_LENGTH = 719;

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

    private static JSONArray getPokedexArray() throws IOException, JSONException {
        String uri = "https://pokeapi.co/api/v2/pokedex/national/";
        JSONObject jsonObjList = toJSON(uri);
        return jsonObjList.getJSONArray("pokemon_entries");
    }

    public static Pokemon getPokemon(String search) throws IOException, JSONException {
        String clearString = search.toLowerCase().replaceAll("[^a-zA-Z]", "");
        JSONArray pokemonList = getPokemonArray();
        JSONArray pokedexList = getPokedexArray();
        JSONObject jsonPokemon = null;
        for (int i = 0; i < MAX_LENGTH; i++) {
            if (clearString.equals(pokemonList.getJSONObject(i).getString("name"))) {
                jsonPokemon = toJSON(pokemonList.getJSONObject(i).get("url").toString());
                jsonPokemon.put("name", pokemonList.getJSONObject(i).get("name"));
            }
            if (clearString.equals(pokedexList.getJSONObject(i).getJSONObject("pokemon_species").getString("name"))) {
                int position = 1;
                for (int j = 0; j < toJSON(pokedexList.getJSONObject(i).getJSONObject("pokemon_species").getString("url")).getJSONArray("flavor_text_entries").length(); i++) {
                    if (toJSON(pokedexList.getJSONObject(i).getJSONObject("pokemon_species").getString("url")).getJSONArray("flavor_text_entries").getJSONObject(j).getJSONObject("language").getString("name").equals("en")) {
                        position = j;
                        break;
                    }
                }
                jsonPokemon.put("description", toJSON(pokedexList.getJSONObject(i).getJSONObject("pokemon_species").getString("url")).getJSONArray("flavor_text_entries").getJSONObject(position).getString("flavor_text").replaceAll("\\n"," "));
            }
        }
        return new Pokemon(jsonPokemon);
    }

    public static Pokemon getPokemon(int id) throws IOException, JSONException {
        JSONArray pokemonList = getPokemonArray();
        JSONArray pokedexList = getPokedexArray();
        JSONObject jsonPokemon;
        if (id < 0 || id > MAX_LENGTH) {
            return null;
        }
        jsonPokemon = toJSON(pokemonList.getJSONObject(id).get("url").toString());
        jsonPokemon.put("name", pokemonList.getJSONObject(id).get("name"));

        int position = 1;
        for (int i = 0; i < MAX_LENGTH; i++) {
            if (toJSON(pokedexList.getJSONObject(id).getJSONObject("pokemon_species").getString("url")).getJSONArray("flavor_text_entries").getJSONObject(i).getJSONObject("language").getString("name").equals("en")) {
                position = i;
                break;
            }
        }
        jsonPokemon.put("description", toJSON(pokedexList.getJSONObject(id).getJSONObject("pokemon_species").getString("url")).getJSONArray("flavor_text_entries").getJSONObject(position).getString("flavor_text").replaceAll("\\n"," "));

        return new Pokemon(jsonPokemon);
    }

    public static double attackMultiplier(String TEMP, Pokemon pokemon) {
        return Type.getMultiplier(TEMP, pokemon);
    }

    public static String[] getPokemonList() throws IOException, JSONException {
        JSONArray pokemonList = getPokemonArray();
        String[] returnString = new String[MAX_LENGTH];
        for (int i = 0; i < MAX_LENGTH; i++) {
            returnString[i] = pokemonList.getJSONObject(i).getString("name")
                    .replaceAll("-aria","")
                    .replaceAll("-ordinary","")
                    .replaceAll("-incarnate","")
                    .replaceAll("-f","")
                    .replaceAll("-m","");
        }
        return returnString;
    }

    public static String getBackgroundURL(Pokemon pokemon) {
        HashMap<String,String> convert = new HashMap<String,String>();
        convert.put("normal", "https://cdn.bulbagarden.net/upload/4/48/Amie_Normal_Wallpaper.png");
        convert.put("fighting", "https://cdn.bulbagarden.net/upload/a/a4/Amie_Fighting_Wallpaper.png");
        convert.put("flying", "https://cdn.bulbagarden.net/upload/b/b1/Amie_Flying_Wallpaper.png");
        convert.put("poison", "https://cdn.bulbagarden.net/upload/1/1a/Amie_Poison_Wallpaper.png");
        convert.put("ground", "https://cdn.bulbagarden.net/upload/3/34/Amie_Ground_Wallpaper.png");
        convert.put("rock", "https://cdn.bulbagarden.net/upload/3/35/Amie_Rock_Wallpaper.png");
        convert.put("bug", "https://cdn.bulbagarden.net/upload/9/95/Amie_Bug_Wallpaper.png");
        convert.put("ghost", "https://cdn.bulbagarden.net/upload/6/61/Amie_Ghost_Wallpaper.png");
        convert.put("steel", "https://cdn.bulbagarden.net/upload/3/36/Amie_Steel_Wallpaper.png");
        convert.put("fire", "https://cdn.bulbagarden.net/upload/2/24/Amie_Fire_Wallpaper.png");
        convert.put("water", "https://cdn.bulbagarden.net/upload/9/98/Amie_Water_Wallpaper.png");
        convert.put("grass", "https://cdn.bulbagarden.net/upload/f/f2/Amie_Grass_Wallpaper.png");
        convert.put("electric", "https://cdn.bulbagarden.net/upload/6/62/Amie_Electric_Wallpaper.png");
        convert.put("psychic", "https://cdn.bulbagarden.net/upload/d/d8/Amie_Psychic_Wallpaper.png");
        convert.put("ice", "https://cdn.bulbagarden.net/upload/b/b5/Amie_Ice_Wallpaper.png");
        convert.put("dragon", "https://cdn.bulbagarden.net/upload/e/ed/Amie_Dragon_Wallpaper.png");
        convert.put("dark", "https://cdn.bulbagarden.net/upload/9/93/Amie_Dark_Wallpaper.png");
        convert.put("fairy", "https://cdn.bulbagarden.net/upload/2/23/Amie_Fairy_Wallpaper.png");

        return convert.get(pokemon.getTypes()[0]);
    }

    public static String getPokemonColor(String type) {
        HashMap<String,String> convert = new HashMap<String,String>();
        convert.put("normal", "A8A77A");
        convert.put("fighting", "C22E28");
        convert.put("flying", "A98FF3");
        convert.put("poison", "A33EA1");
        convert.put("ground", "E2BF65");
        convert.put("rock", "A6B91A");
        convert.put("bug", "A6B91A");
        convert.put("ghost", "735797");
        convert.put("steel", "B7B7CE");
        convert.put("fire", "EE8130");
        convert.put("water", "6390F0");
        convert.put("grass", "7AC74C");
        convert.put("electric", "F7D02C");
        convert.put("psychic", "F95587");
        convert.put("ice", "96D9D6");
        convert.put("dragon", "6F35FC");
        convert.put("dark", "705746");
        convert.put("fairy", "D685AD");

        return "#" + convert.get(type);
    }


}
