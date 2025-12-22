package fr.honertis.guis;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.honertis.Honertis;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.modules.HitColor;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.KeyBindSettings;
import fr.honertis.settings.NumberSettings;
import fr.honertis.settings.Settings;
import fr.honertis.utils.AnimUtils;
import fr.honertis.utils.DrawUtils;
import fr.honertis.utils.LangManager;
import fr.honertis.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiHonertisOptions extends GuiScreen {
	public GuiScreen parent;
	public boolean clicked;
	public String currentDesc;
	private	int mouse = 0;
	private int mouseMax;
	
	private int maxScroll = 0;
	
	public GuiHonertisOptions(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, I18n.format("gui.back", new Object[0])));
		super.initGui();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void handleMouseInput() throws IOException {
	    super.handleMouseInput();

	    int dwheel = Mouse.getDWheel();
	    if (dwheel != 0) {
	    	mouse += dwheel / 8;
	    }

	    if (mouse > 0) mouse = 0;
	    if (mouse < -maxScroll) mouse = -maxScroll;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		int contentHeight = 0;

		for (Category c : Category.values()) {
		    contentHeight += 25;

		    for (ModuleBase m : Honertis.INSTANCE.modulesManager.modules) {
		        if (m.getCat() == c && m.shown) {
		            contentHeight += 25;

		            if (m.showSettings()) {
		                for (Settings s : m.settings) {
		                    if (s.show) {
		                        contentHeight += 25;
		                    }
		                }
		            }
		        }
		    }
		}

		int visibleHeight = (height - 72) - 15;
		maxScroll = Math.max(0, contentHeight - visibleHeight);
		
		int color = new Color(0,0,0,55).getRGB();
		drawRect(0,0,width, 45, color);
		drawRect(0, height - 27, width, height, color);
		drawGradientRect(0, 45, width, 70, color, 0);
    	drawGradientRect(0, height - 72, width, height - 27, 0, color);
		mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.honertis.options.name"), this.width / 2, 10, -1);
		mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.honertis.options.scroll"), this.width / 2, 24, -1);
		mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.honertis.options.scroll1"), this.width / 2, 32, -1);
    	
		GlStateManager.pushMatrix();
    	GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	Utils.scissorGui(0, 45, width, height - 72);
		int posY = 50 + mouse;
		for (Category c : Category.values()) {
			mc.fontRendererObj.drawCenteredStringWithShadow(c.getName(), this.width / 2, posY, -1);
			int modX = this.width / 2 + 60;
			int modY = posY + 23;
			for (ModuleBase m : Honertis.INSTANCE.modulesManager.modules) {
				int settsX = modX - 95;
				int settsY = modY + 25;
				if (m.getCat() == c && m.shown) {
					mc.fontRendererObj.drawStringWithShadow(m.getName(), modX - 175, modY - 2.5, -1);
					GlStateManager.pushMatrix();
					GlStateManager.scale(0.5, 0.5, 0);
					GlStateManager.translate(modX - 175, modY, 1);
					mc.fontRendererObj.drawStringWithShadow(m.getDesc(), modX - 165, modY + 15, -1);
					GlStateManager.popMatrix();
					drawRect(modX + 35, modY - 4, modX + 65, modY + 11, m.isEnabled() ? new Color(0, 205, 0, 190).getRGB() : new Color(205, 0, 0, 190).getRGB() );
					mc.fontRendererObj.drawCenteredString(m.isEnabled() ? "I" : "O", modX + 50, modY, -1);
					drawRect(  modX + (m.isEnabled() ? 60 : 34), modY - 5, modX + (m.isEnabled() ? 66 : 40), modY + 12, -1);					
					
					if (!m.settings.isEmpty()) {
						boolean hover = isHovered(modX + 8, modY - 5, (modX + 8) + 18, (modY - 5) + 18, mouseX, mouseY);
						drawImage(modX + 8, modY - 5, 18, 18, new ResourceLocation("honertis/settings" + (hover ? (m.showSettings() ? "OH" : "H") : m.showSettings() ? "O" : "C") + ".png"));
					}
			
					if (m.showSettings()) {
						HitColor clr = (HitColor) Honertis.INSTANCE.modulesManager.getModuleByName("HitColor");
						if (m.getName().equals(clr.getName()) && clr.custom.isEnabled()) {
							drawRect(settsX + 165, settsY + 15, settsX + 190, settsY + 40, new Color((int)clr.r.getDefValue(), (int)clr.g.getDefValue(), (int)clr.b.getDefValue(), (int)clr.a.getDefValue()).getRGB());
							mc.fontRendererObj.drawString("rendu :", settsX + 160, settsY, -1);
						}
					}
					for (Settings s : m.settings) {
						if (!m.settings.isEmpty() && m.showSettings() && s.show) {
							int width = settsX + 127;
							if (s instanceof NumberSettings) {
								NumberSettings num = (NumberSettings) s;
								double min = num.getMin();
								double max = num.getMax();
								double rdrWidth = 127 * ((num.getValue() - min) / (max - min));
								Gui.drawRect(settsX, settsY + 3, settsX + 127, settsY + 7, -1);
								Gui.drawRect(settsX - 3 + (int)rdrWidth, settsY - 3, settsX + 3 + (int)rdrWidth, settsY + 13, -1);
								mc.fontRendererObj.drawStringWithShadow( num.getIncrement() == 1 ? ("" + (int) num.getFloatValue()) : String.format("%." + (num.getIncrement() == 0.01 ? "2" : num.getIncrement() == 0.001 ? "3" : "1") + "f", num.getFloatValue()), settsX + 133, settsY + 1, -1);
								
								double mouseSnap = mouseX;
								mouseSnap = Math.max(settsX, mouseSnap);
								mouseSnap = Math.min(width, mouseSnap);
								double test = (mouseSnap - settsX)/(width-settsX);
								double diff = max - min;
								double val = min + test * diff;
		                       
								if (isHovered(settsX - 3, settsY - 3, settsX + 127, settsY + 13, mouseX, mouseY) && clicked) {
									num.setValue(val);
								}
		                      
							}
							if (m.getCat() == Category.ANCIEN_MODULES) {
								drawRect(modX, modY, modX + 50, modY + 25, -1);
								if (mouse >= modY + 25) {
									mouse=modY+25;
								}
							}
							if (s instanceof BooleanSettings) {
								BooleanSettings ss = (BooleanSettings) s;
								drawRect(settsX + 118, settsY - 4, settsX + 147, settsY + 11, ss.isEnabled() ? new Color(0, 205, 0, 190).getRGB() : new Color(205, 0, 0, 190).getRGB() );
								mc.fontRendererObj.drawCenteredString(ss.isEnabled() ? "I" : "O", settsX + 132, settsY, -1);
								drawRect(  settsX + 95 - 12 + (ss.isEnabled() ? 60 : 34), settsY - 5, settsX + 95 - 12 + (ss.isEnabled() ? 66 : 40), settsY + 12, -1);
							}
							if (s instanceof KeyBindSettings) {
								KeyBindSettings ss = (KeyBindSettings) s;
								String sn = ss.getName() + "  :  " + (ss.isChanging() ? "..." : Keyboard.getKeyName(ss.getKey()));
								int sx = settsX + 36;
								int sy = settsY;
								mc.fontRendererObj.drawCenteredString(sn ,sx, sy, -1);
								
							}
		                    if (!(s instanceof KeyBindSettings))
							mc.fontRendererObj.drawString(s.getName(), settsX - 70, settsY, -1);
		                    posY += 25;
							modY += 25;
		                    settsY += 25;
						}
					}
					modY += 25;
					posY += 25;
				}
			}
			posY += 25;
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
    	GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (isHovered(0, 45, width, height - 27, mouseX, mouseY)) {
			int posY = 50 + mouse;
			for (Category c : Category.values()) {
				int modX = this.width / 2 + 60;
				int modY = posY + 23;
				for (ModuleBase m : Honertis.INSTANCE.modulesManager.modules) {
					int settsX = modX - 95;
					int settsY = modY + 25;
					if (m.getCat() == c && m.shown) {
						if (mouseButton == 0) {
							if (isHovered(modX + 35, modY - 5, modX + 66, modY + 12, mouseX, mouseY)) {
								m.toggle();
							}
							if (isHovered(modX + 8, modY - 5, (modX + 8) + 18, (modY - 5) + 18, mouseX, mouseY)) {
								m.toggleShowSettings();
							}
						}
						for (Settings s : m.settings) {
							if (!m.settings.isEmpty() && m.showSettings() && s.show) {
								if (s instanceof NumberSettings) {
									if (isHovered(settsX - 3, settsY - 3, settsX + 127, settsY + 13, mouseX, mouseY) && mouseButton == 0) {
										clicked = true;
									}
								}
								if (s instanceof BooleanSettings) {
									BooleanSettings ss = (BooleanSettings) s; 
									if (isHovered(settsX + 118, settsY - 5, settsX + 147, settsY + 12, mouseX, mouseY) && mouseButton == 0) {
										ss.toggle();
									}
								}
								if (s instanceof KeyBindSettings) {
									KeyBindSettings ss = (KeyBindSettings) s;
									String sn = ss.getName() + "  :  " + Keyboard.getKeyName(ss.getKey());
									int sx = settsX + 36;
									int sy = settsY;
									if (isHovered(sx - 31, sy, sx + mc.fontRendererObj.getStringWidth(ss.getName() + "  :  " + Keyboard.getKeyName(ss.getKey())) - 26, sy + 10, mouseX, mouseY)) {
										ss.change();
									}
								}
								posY += 25;
								modY += 25;
			                    settsY += 25;
							}
						}
						modY += 25;
						posY += 25;
					}
				}
				posY += 25;
			}
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		clicked = false;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		int posY = 50 + mouse;
		for (Category c : Category.values()) {
			int modX = this.width / 2 + 60;
			int modY = posY + 23;
			for (ModuleBase m : Honertis.INSTANCE.modulesManager.modules) {
				int settsX = modX - 95;
				int settsY = modY + 25;
				if (m.getCat() == c && m.shown) {
					for (Settings s : m.settings) {
						if (!m.settings.isEmpty() && m.showSettings() && s.show) {
							
							if (s instanceof KeyBindSettings) {
								KeyBindSettings ss = (KeyBindSettings) s;
								if (ss.isChanging()) {
									if (keyCode == Keyboard.KEY_ESCAPE) {
										ss.key = 0;
									} else {
										ss.key = keyCode;
									}
									ss.change();
								}
							}
							posY += 25;
							modY += 25;
		                    settsY += 25;
						}
					}
					modY += 25;
					posY += 25;
				}
			}
			posY += 25;
		}
	}
}
