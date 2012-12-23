package net.minecraft.src;

import java.util.*;
import java.lang.reflect.*;
import net.minecraft.client.Minecraft;

/**
* Class exists only to centralize reflection calls
*/

public class MMWReflection {
  private static Minecraft mc = Minecraft.getMinecraft();
  private static boolean isObfuscated;
  private static Properties obfuscation = new Properties() {{
    setProperty("addMapping", "a");
    setProperty("buttonGameMode", "w");
    setProperty("field_85159_M", "M");
    setProperty("foodExhaustionLevel", "c");
    setProperty("gameMode", "o");
    setProperty("instance", "a");
    setProperty("isHardcore", "t");
    setProperty("nameToSoundPoolEntriesMapping", "d");
    setProperty("recipeItems", "d");
    setProperty("recordIsPlaying", "i");
    setProperty("recordPlaying", "g");
    setProperty("recordPlayingUpFor", "h");
    setProperty("renderBossHealth", "d");
    setProperty("renderInventorySlot", "a");
    setProperty("renderPortalOverlay", "b");
    setProperty("renderPumpkinBlur", "a");
    setProperty("renderVignette", "a");
    setProperty("soundPoolSounds", "b");
    setProperty("translateTable", "b");
    setProperty("tryToFall", "l");
    setProperty("updateButtonText", "h");
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

  public static Object getPrivateValue(Class cls, Object obj, String fieldName) {
    try {
      Field field = cls.getDeclaredField(obfuscatedName(fieldName));
      field.setAccessible(true);
      return field.get(obj);
    } catch (Exception exception) {
      return null;
    }
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
}
