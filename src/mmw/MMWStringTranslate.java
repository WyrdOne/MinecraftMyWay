package net.minecraft.src;

import java.lang.reflect.Field;

public class MMWStringTranslate extends StringTranslate {
  private static StringTranslate parent = StringTranslate.getInstance();
  private static MMWHookInterface<String> hook;

  public MMWStringTranslate(String par1Str) {
    super(par1Str);
    parent.setLanguage(par1Str); // Chained in case others hook this same class
  }

  public void setHook(MMWHookInterface<String> hook) {
    Field field = null;
    try {
      field = parent.getClass().getDeclaredField("a");
    } catch (Exception notobfuscated) {
      try {field = parent.getClass().getDeclaredField("instance");} catch (Exception ignored) {}
    }
    field.setAccessible(true);
    try {field.set(null, this);} catch (Exception ignored) {}
    this.hook = hook;
  }

  public String translateKey(String par1Str) {
    hook.StringTranslateHook(par1Str);
    return parent.translateKey(par1Str); // Chained in case others hook this same class
  }
}
