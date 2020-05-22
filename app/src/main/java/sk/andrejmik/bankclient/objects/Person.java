package sk.andrejmik.bankclient.objects;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity
{
    private String name;

    private String surname;

    private Date born;
}
