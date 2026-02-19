package fr.uha40.mains;

import caseine.format.javajunit.Grade;
import fr.uha40.Elements.Paragraphe;
import org.junit.Test;

public class Execution {

    public static void main(String[] args) {
    }

    public static Paragraphe utiliserConstructeurParDefaut() {
        // TODO (5) Implémenter le code qui déclare un paragraphe, l'initialise en utilisant le constructeur par défaut et le retourne
        Paragraphe p = new Paragraphe();
        return p;
    }

    public static Paragraphe utiliserConstructeurParametre(String t) {
        // TODO (6) Implémenter le code qui déclare un paragraphe, l'initialise en utilisant le constructeur parametrée avec le text t et le retourne
        Paragraphe p = new Paragraphe(t);
        return p;
    }

    public static Paragraphe utiliserConstructeurParametreRepeat(String t, int repeat) {
        // TODO (7) Implémenter le code qui déclare un paragraphe, l'initialise en utilisant le constructeur parametrée avec le text t à répéter repeat fois et le retourne
        Paragraphe p = new Paragraphe(t, repeat);
        return p;
    }

    public static Paragraphe utiliserConstructeurParRecopie(Paragraphe p2Copie) {
        // TODO (8) Implémenter le code qui déclare un paragraphe, l'initialise en recopiant p2Copie et le retourne
        Paragraphe p = new Paragraphe(p2Copie);
        return p;
    }
}