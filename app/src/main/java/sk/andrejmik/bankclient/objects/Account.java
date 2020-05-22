package sk.andrejmik.bankclient.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity
{
    private String name;

    private Date dateCreated;

    private Person owner;

    private List<Card> cardsList = new ArrayList<>();

}
