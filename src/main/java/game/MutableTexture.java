package game;

import game.events.EventBus;
import game.gl.GLTexture;
import game.image.BufferedImageImage;
import game.image.Material;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MutableTexture implements Material {
  
  private BufferedImageImage img;
  private GLTexture tex;
  
  public MutableTexture(EventBus eventBus, int width, int height) {
    img = new BufferedImageImage(width, height);
    tex = new GLTexture(eventBus);
    tex.setImage(img);
  }
  
  public void drawText(String text, int x, int y) {
    Graphics2D graphics = img.getImage().createGraphics();
    graphics.setColor(Color.red);
    graphics.setFont(new Font("SansSerif", Font.PLAIN, 12));
    graphics.drawString(text, x, y);
  }
  
  public void update() {
    tex.setImage(img);
  }
  
  public GLTexture getTexture() {
    return tex;
  }

  public void clear() {
    Graphics2D graphics = img.getImage().createGraphics();
    graphics.setColor(new Color(0, 0, 0, 0));
    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0F));    
    graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
  }

}
