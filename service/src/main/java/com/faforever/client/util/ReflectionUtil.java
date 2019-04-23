package com.faforever.client.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class ReflectionUtil {

  /**
   * Searches for the Field of name {@param fieldName} in the {@param targetClass} and its super classes and return the
   * type of that field.
   *
   * @param fieldName name of the field
   * @param targetClass The class to look in
   * @return The class type of the field
   * @throws NoSuchFieldException When no field is found
   */
  public Class<?> getFieldType(String fieldName, Class<?> targetClass) throws NoSuchFieldException {
    Class currentClass = targetClass;
    Class clazz = null;
    while (clazz == null) {
      try {
        clazz = currentClass.getDeclaredField(fieldName).getType();
      } catch (NoSuchFieldException e) {
        currentClass = targetClass.getSuperclass();
        if (currentClass == null) {
          throw new NoSuchFieldException(fieldName);
        }
      }
    }
    return clazz;
  }

  @SneakyThrows
  public void setField(Object target, String fieldName, Object value) {
    Field declaredField = target.getClass().getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    declaredField.set(target, value);
  }
}
