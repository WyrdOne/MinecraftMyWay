package net.minecraft.src;

import java.lang.reflect.*;

public class MMWModeButton extends GuiButton {
  GuiCreateWorld screen;

  public MMWModeButton(GuiScreen screen) {
    super(121, screen.width / 2 - 75, 115, 150, 20, StringTranslate.getInstance().translateKey("selectWorld.gameMode"));
    this.screen = (GuiCreateWorld)screen;
  }

  public String getGameMode() {
    String gameMode = "survival";
    try {
      gameMode = (String)MMWReflection.getPrivateValue(GuiCreateWorld.class, screen, "gameMode");
    } catch (Exception ignored) { }
    return gameMode;  
  }

  public void setGameMode(String gameMode) {
    try {
      MMWReflection.setPrivateValue(GuiCreateWorld.class, screen, "gameMode", gameMode);
    } catch (Exception ignored) { }
  }

  public void setHardcore(boolean hardcore) {
    try {
      MMWReflection.setPrivateValue(GuiCreateWorld.class, screen, "isHardcore", hardcore);
    } catch (Exception ignored) { }
  }
  
  public void setAllowCommands(boolean enabled) {
    GuiButton guiButton = (GuiButton)screen.controlList.get(7);
    guiButton.enabled = enabled;  
  }
  
  public void setBonusItems(boolean enabled) {
    GuiButton guiButton = (GuiButton)screen.controlList.get(5);
    guiButton.enabled = enabled;  
  }

  public void updateButtonText() {
    Method method = MMWReflection.getPrivateMethod(screen.getClass(), "updateButtonText");

    try {
      method.invoke(screen);
    } catch (Exception ignored) { }
  }

  public void mouseReleased(int par1, int par2) {
    // Sequence:  survival -> hardcore -> creative -> adventure
    if (getGameMode()=="survival") {
      setGameMode("hardcore");
      setHardcore(true);
      setAllowCommands(false);
      setBonusItems(false);
    } else if (getGameMode()=="hardcore") {
      setGameMode("creative");
      setHardcore(false);
      setAllowCommands(true);
      setBonusItems(true);
    } else if (getGameMode()=="creative") {
      setGameMode("adventure");
      setHardcore(false);
      setAllowCommands(true);
      setBonusItems(true);
    } else {
      setGameMode("survival");
      setHardcore(false);
      setAllowCommands(true);
      setBonusItems(true);
    }
    updateButtonText();
  }
}
