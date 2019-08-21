import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    public int id;
    public String name;
    public String email;

    public User(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("email") String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
