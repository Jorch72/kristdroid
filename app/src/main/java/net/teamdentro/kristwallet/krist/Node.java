package net.teamdentro.kristwallet.krist;

import java.io.Serializable;
import java.net.URL;

public class Node implements Serializable {
    public String name;
    public String hosted;
    public String owner;
    public URL url;
    public String unique;
    public String currency;
    public String shorthandCurrency;

    public Node(String name, String hosted, String owner, URL url, String unique, String currency, String shorthandCurrency) {
        this.name = name;
        this.hosted = hosted;
        this.owner = owner;
        this.url = url;
        this.unique = unique;
        this.currency = currency;
        this.shorthandCurrency = shorthandCurrency;
    }
}
