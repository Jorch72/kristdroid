package net.teamdentro.kristwallet.krist;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 0L;

    private String label;
    private String password;
    private int id;
    private Node node;

    public Account(int id, String label, String password, Node node) {
        this.id = id;
        this.label = label;
        this.password = password;
        this.node = node;
    }

    public int getID() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getPassword() {
        return password;
    }

    public Node getNode() {
        return node;
    }
}
