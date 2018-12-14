package PokeAPI;

import PokeAPI.Move;
import PokeAPI.Pokemon;

import java.util.HashMap;

class Type {
    private static double[][] attackSearch = null;
    private static HashMap<String,Integer> convert = null;

    static private void populate() {
        convert = new HashMap<String,Integer>();
        convert.put("normal", 0);
        convert.put("fighting", 1);
        convert.put("flying", 2);
        convert.put("poison", 3);
        convert.put("ground", 4);
        convert.put("rock", 5);
        convert.put("bug", 6);
        convert.put("ghost", 7);
        convert.put("steel", 8);
        convert.put("fire", 9);
        convert.put("water", 10);
        convert.put("grass", 11);
        convert.put("electric", 12);
        convert.put("psychic", 13);
        convert.put("ice", 14);
        convert.put("dragon", 15);
        convert.put("dark", 16);
        convert.put("fairy", 17);

        attackSearch = new double[18][18];
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                attackSearch[i][j] = 1.0;
            }
        }
        attackSearch[convert.get("normal")][convert.get("rock")] = 0.5;
        attackSearch[convert.get("normal")][convert.get("ghost")] = 0.0;
        attackSearch[convert.get("normal")][convert.get("steel")] = 0.5;
        //TODO fill out the rest of these
    }

    static double getMultiplier(String move, Pokemon defender) {
        if (attackSearch == null || convert == null) {
            populate();
        }
        double multiplier = 1.0;
        for (int i = 0; i < defender.getTypes().length; i++) {
            multiplier *= attackSearch[convert.get(move)][convert.get(defender.getTypes()[i])];
        }
        return multiplier;
    }
}
