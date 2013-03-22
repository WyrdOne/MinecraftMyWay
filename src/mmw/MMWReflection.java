package mmw;

import net.minecraft.src.*;

import java.util.*;
import java.lang.reflect.*;

import net.minecraft.client.Minecraft;

/**
* Class exists only to centralize reflection calls
*/

public class MMWReflection {
  private static boolean isObfuscated;
  private static Properties obfuscation = new Properties() {{
    setProperty("blocksEffectiveAgainst", "c"); // field_77863_c
    setProperty("buttonGameMode", "w"); // field_73930_u
    setProperty("field_82665_g", "g");
    setProperty("field_82671_h", "h");
    setProperty("field_85159_M", "M");
    setProperty("foodExhaustionLevel", "c"); // field_75126_c 
    setProperty("gameMode", "o"); // field_73927_m
    setProperty("instance", "a");
    setProperty("isHardcore", "t"); // field_73933_r
    setProperty("recipeItems", "d"); // field_77574_d
    setProperty("structureCoords", "g"); // field_75057_g 
    setProperty("tryToFall", "k"); // func_72190_l
    setProperty("updateButtonText", "h"); // func_73914_h
  }};
  
  static {
    isObfuscated = false;
    try {
      Class.forName("net.minecraft.src.MathHelper", false, MMWUtil.class.getClassLoader());
    } catch (ClassNotFoundException e) {
      isObfuscated = true;
    }
  }

  public static boolean isObfuscated() {
    return isObfuscated;
  }

  public static String obfuscatedName(String fieldName) {
    if (fieldName=="Shapeless") { // Special case
      if (isObfuscated) {
        fieldName = "b";
      } else {
        fieldName = "recipeItems";
      }
    } else if (isObfuscated) {
      fieldName = obfuscation.getProperty(fieldName);
    }
    return fieldName;  
  }

  public static Object getPrivateValue(Class cls, Object obj, Class fieldType, int count) {
	  Field[] fields = cls.getDeclaredFields();
	  Object returnObject = null;
	  for (int idx=0; idx<fields.length; idx++) {
		  if (fields[idx].getType()==fieldType) {
			  if (count==0) {
				  fields[idx].setAccessible(true);
				  try {
					  returnObject = fields[idx].get(obj);
				  } catch (Exception ignored) {}
				  break;
			  }
			  count--;
		  }
	  }
	  return returnObject;
  }
  
  public static Object getPrivateValue(Object obj, Class fieldType, int count) {
    return getPrivateValue(obj.getClass(), obj, fieldType, count);
  }
  
  public static Object getPrivateValue(Class cls, Object obj, String fieldName) {
    try {
      Field field = cls.getDeclaredField(obfuscatedName(fieldName));
      field.setAccessible(true);
      return field.get(obj);
    } catch (Exception exception) {
      return null;
    }
  }

  public static boolean setPrivateValue(Class cls, Object obj, Class fieldType, int count, Object value) {
	  Field[] fields = cls.getDeclaredFields();
	  for (int idx=0; idx<fields.length; idx++) {
		  if (fields[idx].getType()==fieldType) {
			  if (count==0) {
				  fields[idx].setAccessible(true);
				  try {
					  fields[idx].set(obj, value);
					  return true;
				  } catch (Exception ignored) {}
				  break;
			  }
			  count--;
		  }
	  }
	  return false;
  }

  public static boolean setPrivateValue(Object obj, Class fieldType, int count, Object value) {
    return setPrivateValue(obj.getClass(), obj, fieldType, count, value);
  }
  
  public static boolean setPrivateValue(Class cls, Object obj, String fieldName, Object value) {
    try {
      Field field = cls.getDeclaredField(obfuscatedName(fieldName));
      field.setAccessible(true);
      field.set(obj, value);
      return true;
    } catch (Exception exception) {
      return false;
    }
  }
  
  public static Method getPrivateMethod(Class declaringClass, String methodName, Class... propertyType) {
    Method[] declaredMethods = declaringClass.getDeclaredMethods();
    for (Method method : declaredMethods) {
      if (method.getName().equals(obfuscatedName(methodName)) && Arrays.equals(method.getParameterTypes(), propertyType) ) {
        method.setAccessible(true);
        return method;
      }
    }
    return getPrivateMethod(declaringClass.getSuperclass(), methodName, propertyType);
  }
  
  public static List getControlList(GuiScreen screen) {
	  List controlList = (List)getPrivateValue(screen, List.class, 0);
	  return controlList;  
  }
}
