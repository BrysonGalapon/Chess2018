package src;

import java.awt.AWTException;
import java.awt.event.*;

import src.datatypes.*;
import src.mouse_control.*;

public class Main {
  public static void main(String[] args) {
    try {
      Mouse_Control mouse = new Mouse_Control();
      mouse.makeMove();
    } catch (InterruptedException e1) {
      throw new Error("Thread was interrupted");
    } catch (AWTException e2) {
      throw new Error("Mouse excepted");
    }
  }
}
