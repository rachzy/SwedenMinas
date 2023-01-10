package com.redesweden.swedenminas.functions;

import java.util.Collections;

public class SerializeToScoreboard {
    private final String string;
    private final int maxChar;

    public SerializeToScoreboard(String string, int maxChar) {
        this.string = string;
        this.maxChar = maxChar;
    }

    public String generate() {
        int spacesToAdd = (maxChar - string.length())/2;
        String spaces = String.join("", Collections.nCopies(spacesToAdd, " "));
        return spaces+string+spaces;
    }
}
