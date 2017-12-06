package tinkoff.fintech.exchange.pojo;


public class CheckableCurrency {
    private String name;
    private Boolean isChecked;

    public CheckableCurrency(String name, Boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
