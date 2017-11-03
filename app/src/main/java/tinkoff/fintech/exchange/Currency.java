package tinkoff.fintech.exchange;

public class Currency {

    private String name;
    private boolean isChecked;

    public Currency(String n, boolean c) {
        name = n;
        isChecked = c;
    }

    public Currency(String n) {
        name = n;
        isChecked = false;
     }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
