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
      gameMode = (String)MMWUtil.getPrivateValue(GuiCreateWorld.class, screen, "gameMode", "o");
    } catch (Exception ignored) { }
    return gameMode;  
  }

  public void setGameMode(String gameMode) {
    try {
      MMWUtil.setPrivateValue(GuiCreateWorld.class, screen, "gameMode", "o", gameMode);
    } catch (Exception ignored) { }
  }

  public void setHardcore(boolean hardcore) {
    try {
      MMWUtil.setPrivateValue(GuiCreateWorld.class, screen, "isHardcore", "t", hardcore);
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
    try {
      Method method = screen.getClass().getDeclaredMethod((MMWUtil.isObfuscated()) ? "h" : "updateButtonText");
      method.setAccessible(true);
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
