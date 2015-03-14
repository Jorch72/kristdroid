package net.teamdentro.kristwallet.accounts;

public class Account {
    private String label;
    private String password;
    private int id;

    public Account(int id, String label, String password) {
        this.id = id;
        this.label = label;
        this.password = password;
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
}
