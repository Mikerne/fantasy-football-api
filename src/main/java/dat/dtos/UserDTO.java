package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private Set<String> roles;


    public UserDTO(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
