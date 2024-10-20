package parameters;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class AreaConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String city) {
        return switch (city) {
            case "Москва" -> 1;
            case "Санкт-Петербург" -> 2;
            case "Ижевск" -> 96;
            case "Казань" -> 88;
            case "Нижний Новгород" -> 66;
            default -> null;
        };
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Integer.class);
    }
}
