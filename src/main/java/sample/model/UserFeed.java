package sample.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UserFeed {

    private String name;
    private String emailId;
    private String address;
    private String phoneNo;
    private String birthDate;
    private Date birthDateDb;

}
