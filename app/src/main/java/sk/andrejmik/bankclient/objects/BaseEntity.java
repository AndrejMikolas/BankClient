package sk.andrejmik.bankclient.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity implements Serializable
{
    @SerializedName("id")
    private String id;
}
