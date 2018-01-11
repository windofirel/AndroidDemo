package priv.wind.recycleviewdemo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import priv.wind.recycleviewdemo.annotation.FormHeader;

/**
 * @author Dongbaicheng
 * @version 2017/12/4
 */

public class ListHelper<T> {





    @Deprecated
    public List<String> getHeaderNames(Class entityClass) {
        List<String> headerNames = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            FormHeader annotation = field.getAnnotation(FormHeader.class);
            if (annotation != null){
                headerNames.add(annotation.name());
            }
        }
        return headerNames;
    }
}
