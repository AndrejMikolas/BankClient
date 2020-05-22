package sk.andrejmik.bankclient.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class BaseEntity implements Serializable
{
    private String id;
}
