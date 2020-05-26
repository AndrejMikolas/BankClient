package sk.andrejmik.bankclient.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Card extends BaseEntity
{
    @SerializedName("cardNo")
    private String cardNo;
    
    @SerializedName("expirationYear")
    private int expirationYear;
    
    @SerializedName("expirationMonth")
    private int expirationMonth;
    
    public String getCardNoFormatted()
    {
        if (cardNo == null)
        {
            return null;
        }
        char delimiter = ' ';
        return cardNo.replaceAll(".{4}(?!$)", "$0" + delimiter);
    }
    
    public boolean getIsExpired()
    {
        String input = String.format(Locale.getDefault(), "%02d/%02d", expirationMonth + 1, expirationYear);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy", Locale.getDefault());
        simpleDateFormat.setLenient(false);
        Date expiry = null;
        try
        {
            expiry = simpleDateFormat.parse(input);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return false;
        }
        return expiry.before(new Date());
    }
    
    @NonNull
    @Override
    public String toString()
    {
        return String.format(Locale.getDefault(), "%s - %02d/%02d", getCardNoFormatted(), expirationMonth + 1, expirationYear);
    }
}
