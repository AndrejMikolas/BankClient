package sk.andrejmik.bankclient.utils;

import androidx.databinding.InverseMethod;

/**
 * Helper converter for two-way binding of non-string types
 */
public class BindingConverter
{
    //region INTEGER
    public static Integer convertStringToNullInt(String text)
    {
        if (text == null || text.isEmpty())
        {
            return null;
        }
        try
        {
            return Integer.parseInt(text);
        } catch (NumberFormatException e)
        {
            return null;
        }
    }
    
    @InverseMethod(value = "convertStringToNullInt")
    public static String convertNullIntToString(Integer value)
    {
        if (value == null)
        {
            return null;
        }
        return Integer.toString(value);
    }
    //endregion
}
