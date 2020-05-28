package sk.andrejmik.bankclient.objects;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity
{
    @SerializedName("name")
    private String name;
    
    @SerializedName("surname")
    private String surname;
}
