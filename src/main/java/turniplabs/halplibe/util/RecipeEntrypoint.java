package turniplabs.halplibe.util;

public interface RecipeEntrypoint {
    void onRecipesReady();

    default void initNamespaces(){

    }
}
