package app.alyosharomanov.pocketdex;

public class PokemonNames {
    private String pokemonNames;
    private int pokemonNumber;

    public PokemonNames(String pokemonNames, int pokemonNumber) {
        this.pokemonNames = pokemonNames;
        this.pokemonNumber = pokemonNumber;
    }

    public String getPokemonNames() {
        return this.pokemonNames;
    }

    public int getPokemonNumber() {
        return pokemonNumber;
    }
}