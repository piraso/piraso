package ard.piraso.api.converter;

/**
 * Created by IntelliJ IDEA.
 * User: adleon
 * Date: 10/19/11
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectConverter {

    String convertToString(Object obj);

    Object convertToObject(String str);
}
