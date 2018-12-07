package PokeAPI;

import PokeAPI.Item;
import PokeAPI.Move;

import org.json.JSONException;
import org.json.JSONObject;

public class Pokemon {
    private String name;
    private String description;
    private int id;
    private int height;
    private int weight;
    private int speed;
    private int current_speed;
    private int specialDefense;
    private int current_specialDefense;
    private int specialAttack;
    private int current_specialAttack;
    private int defence;
    private int current_defense;
    private int attack;
    private int current_attack;
    private int hp;
    private int current_hp;
    private String url;
    private Move move1;
    private Move move2;
    private Move move3;
    private Move move4;
    private Move[] possibleMoves;
    private Item item;
    private String[] types;

    Pokemon(JSONObject data) throws JSONException {
        this.name = data.getString("name");
        this.description = data.getString("description");
        this.id = data.getInt("id");
        this.height = data.getInt("height");
        this.weight = data.getInt("weight");
        this.speed = data.getJSONArray("stats").getJSONObject(0).getInt("base_stat");
        this.specialDefense = data.getJSONArray("stats").getJSONObject(1).getInt("base_stat");
        this.specialAttack = data.getJSONArray("stats").getJSONObject(2).getInt("base_stat");
        this.defence = data.getJSONArray("stats").getJSONObject(3).getInt("base_stat");
        this.attack = data.getJSONArray("stats").getJSONObject(4).getInt("base_stat");
        this.hp = data.getJSONArray("stats").getJSONObject(5).getInt("base_stat");
        this.url = data.getJSONObject("sprites").getString("front_default");

        int length = data.getJSONArray("types").length();
        this.types = new String[length];
        for (int i = 0; i < length; i++) {
            types[i] = data.getJSONArray("types").getJSONObject(length - 1 - i).getJSONObject("type").getString("name");
        }
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getId() {
        return id;
    }
    public int getHeight() {
        return height;
    }
    public int getWeight() {
        return weight;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(final int toSpeed) {
        speed = toSpeed;
    }
    public int getSpecialDefense() {
        return specialDefense;
    }
    public void setSpecialDefense(final int toSpecialDefence) {
        specialDefense = toSpecialDefence;
    }
    public int getSpecialAttack() {
        return specialAttack;
    }
    public void setSpecialAttack(final int toSpecialAttack) {
        specialAttack = toSpecialAttack;
    }
    public int getDefence() {
        return defence;
    }
    public void setDefence(final int toDefence) {
        defence = toDefence;
    }
    public int getAttack() {
        return attack;
    }
    public void setAttack(final int toAttack) {
        attack = toAttack;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(final int toHp) {
        hp = toHp;
    }
    public String getUrl() {
        return this.url;
    }

    public Move getMove1() {
        return move1;
    }
    public Move getMove2() {
        return move2;
    }
    public Move getMove3() {
        return move3;
    }
    public Move getMove4() {
        return move4;
    }
    public void setMoves(final Move firstMove, final Move secondMove, final Move thirdMove, final Move fourthMove) {
        move1 = firstMove;
        move2 = secondMove;
        move3 = thirdMove;
        move4 = fourthMove;
    }
    public String[] getTypes() {
        return types;
    }
}
