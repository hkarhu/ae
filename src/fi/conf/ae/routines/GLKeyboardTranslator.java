package fi.conf.ae.routines;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

public class GLKeyboardTranslator {
	
	public static char getCharFromEventKey(int eventKey){
		switch (eventKey) {
		case KeyEvent.VK_0: return '0';
		case KeyEvent.VK_1: return '1';
		case KeyEvent.VK_2: return '2';
		case KeyEvent.VK_3: return '3';
		case KeyEvent.VK_4: return '4';
		case KeyEvent.VK_5: return '5';
		case KeyEvent.VK_6: return '6';
		case KeyEvent.VK_7: return '7';
		case KeyEvent.VK_8: return '8';
		case KeyEvent.VK_9: return '9';
		case KeyEvent.VK_Q: return 'Q';
		case KeyEvent.VK_W: return 'W';
		case KeyEvent.VK_E: return 'E';
		case KeyEvent.VK_R: return 'R';
		case KeyEvent.VK_T: return 'T';
		case KeyEvent.VK_Y: return 'Y';
		case KeyEvent.VK_U: return 'U';
		case KeyEvent.VK_I: return 'I';
		case KeyEvent.VK_O: return 'O';
		case KeyEvent.VK_P: return 'P';
		case KeyEvent.VK_A: return 'A';
		case KeyEvent.VK_S: return 'S';
		case KeyEvent.VK_D: return 'D';
		case KeyEvent.VK_F: return 'F';
		case KeyEvent.VK_G: return 'G';
		case KeyEvent.VK_H: return 'H';
		case KeyEvent.VK_J: return 'J';
		case KeyEvent.VK_K: return 'K';
		case KeyEvent.VK_L: return 'L';
		case KeyEvent.VK_Z: return 'Z';
		case KeyEvent.VK_X: return 'X';
		case KeyEvent.VK_C: return 'C';
		case KeyEvent.VK_V: return 'V';
		case KeyEvent.VK_B: return 'B';
		case KeyEvent.VK_N: return 'N';
		case KeyEvent.VK_M: return 'M';
		case KeyEvent.VK_PERIOD: return '.';
		case KeyEvent.VK_COMMA: return ',';
		case KeyEvent.VK_EQUALS: return '=';
		case KeyEvent.VK_ADD: return '+';
		case KeyEvent.VK_MINUS: return '-';
		case KeyEvent.VK_SUBTRACT: return '-';
		case KeyEvent.VK_MULTIPLY: return '*';
		case KeyEvent.VK_DIVIDE: return '/';
		case KeyEvent.VK_SPACE: return ' ';
		default: return '\0';
		}
	}
	
	
	public static int translateKey(int GLEventKey) {
		switch (GLEventKey) {
			case Keyboard.KEY_0: return KeyEvent.VK_0;
			case Keyboard.KEY_1: return KeyEvent.VK_1;
			case Keyboard.KEY_2: return KeyEvent.VK_2;
			case Keyboard.KEY_3: return KeyEvent.VK_3;
			case Keyboard.KEY_4: return KeyEvent.VK_4;
			case Keyboard.KEY_5: return KeyEvent.VK_5;
			case Keyboard.KEY_6: return KeyEvent.VK_6;
			case Keyboard.KEY_7: return KeyEvent.VK_7;
			case Keyboard.KEY_8: return KeyEvent.VK_8;
			case Keyboard.KEY_9: return KeyEvent.VK_9;
			case Keyboard.KEY_Q: return KeyEvent.VK_Q;
			case Keyboard.KEY_W: return KeyEvent.VK_W;
			case Keyboard.KEY_E: return KeyEvent.VK_E;
			case Keyboard.KEY_R: return KeyEvent.VK_R;
			case Keyboard.KEY_T: return KeyEvent.VK_T;
			case Keyboard.KEY_Y: return KeyEvent.VK_Y;
			case Keyboard.KEY_U: return KeyEvent.VK_U;
			case Keyboard.KEY_I: return KeyEvent.VK_I;
			case Keyboard.KEY_O: return KeyEvent.VK_O;
			case Keyboard.KEY_P: return KeyEvent.VK_P;
			case Keyboard.KEY_A: return KeyEvent.VK_A;
			case Keyboard.KEY_S: return KeyEvent.VK_S;
			case Keyboard.KEY_D: return KeyEvent.VK_D;
			case Keyboard.KEY_F: return KeyEvent.VK_F;
			case Keyboard.KEY_G: return KeyEvent.VK_G;
			case Keyboard.KEY_H: return KeyEvent.VK_H;
			case Keyboard.KEY_J: return KeyEvent.VK_J;
			case Keyboard.KEY_K: return KeyEvent.VK_K;
			case Keyboard.KEY_L: return KeyEvent.VK_L;
			case Keyboard.KEY_Z: return KeyEvent.VK_Z;
			case Keyboard.KEY_X: return KeyEvent.VK_X;
			case Keyboard.KEY_C: return KeyEvent.VK_C;
			case Keyboard.KEY_V: return KeyEvent.VK_V;
			case Keyboard.KEY_B: return KeyEvent.VK_B;
			case Keyboard.KEY_N: return KeyEvent.VK_N;
			case Keyboard.KEY_M: return KeyEvent.VK_M;
			case Keyboard.KEY_F1: return KeyEvent.VK_F1;
			case Keyboard.KEY_F2: return KeyEvent.VK_F2;
			case Keyboard.KEY_F3: return KeyEvent.VK_F3;
			case Keyboard.KEY_F4: return KeyEvent.VK_F4;
			case Keyboard.KEY_F5: return KeyEvent.VK_F5;
			case Keyboard.KEY_F6: return KeyEvent.VK_F6;
			case Keyboard.KEY_F7: return KeyEvent.VK_F7;
			case Keyboard.KEY_F8: return KeyEvent.VK_F8;
			case Keyboard.KEY_F9: return KeyEvent.VK_F9;
			case Keyboard.KEY_F10: return KeyEvent.VK_F10;
			case Keyboard.KEY_F11: return KeyEvent.VK_F11;
			case Keyboard.KEY_F12: return KeyEvent.VK_F12;
			case Keyboard.KEY_PERIOD: return KeyEvent.VK_PERIOD; 
			case Keyboard.KEY_COMMA: return KeyEvent.VK_COMMA;
			case Keyboard.KEY_EQUALS: return KeyEvent.VK_EQUALS;
			case Keyboard.KEY_ADD: return KeyEvent.VK_ADD;
			case Keyboard.KEY_MINUS: return KeyEvent.VK_MINUS;
			case Keyboard.KEY_SUBTRACT: return KeyEvent.VK_SUBTRACT;
			case Keyboard.KEY_MULTIPLY: return KeyEvent.VK_MULTIPLY;
			case Keyboard.KEY_DIVIDE: return KeyEvent.VK_DIVIDE;
			case Keyboard.KEY_SPACE: return KeyEvent.VK_SPACE;
			case Keyboard.KEY_ESCAPE: return KeyEvent.VK_ESCAPE;
			case Keyboard.KEY_BACK: return KeyEvent.VK_BACK_SPACE;
			case Keyboard.KEY_UP: return KeyEvent.VK_UP;
			case Keyboard.KEY_RETURN: return KeyEvent.VK_ENTER;
			case Keyboard.KEY_DOWN: return KeyEvent.VK_DOWN;
			case Keyboard.KEY_DELETE: return KeyEvent.VK_DELETE;
			case Keyboard.KEY_END: return KeyEvent.VK_END;
			case Keyboard.KEY_HOME: return KeyEvent.VK_HOME;
			case Keyboard.KEY_NUMPAD0: return KeyEvent.VK_NUMPAD0;
			case Keyboard.KEY_NUMPAD1: return KeyEvent.VK_NUMPAD1;
			case Keyboard.KEY_NUMPAD2: return KeyEvent.VK_NUMPAD2;
			case Keyboard.KEY_NUMPAD3: return KeyEvent.VK_NUMPAD3;
			case Keyboard.KEY_NUMPAD4: return KeyEvent.VK_NUMPAD4;
			case Keyboard.KEY_NUMPAD5: return KeyEvent.VK_NUMPAD5;
			case Keyboard.KEY_NUMPAD6: return KeyEvent.VK_NUMPAD6;
			case Keyboard.KEY_NUMPAD7: return KeyEvent.VK_NUMPAD7;
			case Keyboard.KEY_NUMPAD8: return KeyEvent.VK_NUMPAD8;
			case Keyboard.KEY_NUMPAD9: return KeyEvent.VK_NUMPAD9;
			default: S.debug("Key not processed. Keycode: %s", GLEventKey); return -1;
		}
	}
}
