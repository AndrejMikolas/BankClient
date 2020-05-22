package sk.andrejmik.bankclient.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Card extends BaseEntity
{
    private String cardNo;

    private int expirationYear;

    private int expirationMonth;
}
