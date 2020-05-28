package sk.andrejmik.bankclient.objects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.andrejmik.bankclient.utils.Globals;

@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity
{
    @SerializedName("name")
    private String name;
    
    @SerializedName("dateCreated")
    private Date dateCreated;
    
    @SerializedName("owner")
    private Person owner = new Person();
    
    @SerializedName("cardsList")
    private List<Card> cardsList = new ArrayList<>();
    
    public void addCard(Card card)
    {
        this.cardsList.add(card);
    }
    
    public void removeCard(int position)
    {
        this.cardsList.remove(position);
    }
    
    public String getDateCreatedFormatted()
    {
        if (dateCreated != null)
        {
            return Globals.DATE_FORMAT.format(dateCreated);
        }
        return "";
    }
    
}
