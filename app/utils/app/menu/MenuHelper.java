package utils.app.menu;

import java.util.HashMap;

import models.db.app.menu.ApplicationMenu;

public class MenuHelper {

	private static final int TOP_PUBLIC_MENU_ID = 1;
	private static final int TOP_USER_MENU_ID = 2;
	private static final int BACKOFFICE_MENU_ID = 3;

	private static final ApplicationMenu TOP_PUBLIC_MENU = new ApplicationMenu(TOP_PUBLIC_MENU_ID, null, "Menu Topo", "Top Menu", "Topo", "Top", null, null, null, true, true, true, 0, false, true, null);
	private static final ApplicationMenu TOP_USER_MENU = new ApplicationMenu(TOP_USER_MENU_ID, null, "Menu Utilizador", "User Menu", "Menu", "Menu", null, "fa fa-bars", null, true, true, true, 0, false, true, null);
	private static final ApplicationMenu BACKOFFICE_MENU = new ApplicationMenu(BACKOFFICE_MENU_ID, null, "Menu Backoffice", "Backoffice Menu", "Backoffice", "Backoffice", null, null, null, true, false, true, 0, false, true, null);

	@SuppressWarnings("serial")
	private static final HashMap<Integer, ApplicationMenu> MENUS = new HashMap<Integer, ApplicationMenu>() {
		{
			put(TOP_PUBLIC_MENU_ID, TOP_PUBLIC_MENU);
			put(TOP_USER_MENU_ID, TOP_USER_MENU);
			put(BACKOFFICE_MENU_ID, BACKOFFICE_MENU);
		}
	};

	public Integer[] getMenuIDs() {
		return new Integer[] { TOP_PUBLIC_MENU_ID, TOP_USER_MENU_ID, BACKOFFICE_MENU_ID };
	}

	public ApplicationMenu getMenuByID(Integer id) {
		ApplicationMenu menu = ApplicationMenu.getByID(id);
		if (menu == null) {
			menu = MENUS.get(id);
			menu.save();
		}
		return menu;
	}

	public ApplicationMenu getTopPublicMenu() {
		return getMenuByID(TOP_PUBLIC_MENU_ID);
	}

	public ApplicationMenu getTopUserMenu() {
		return getMenuByID(TOP_USER_MENU_ID);
	}

	public ApplicationMenu getBackofficeMenu() {
		return getMenuByID(BACKOFFICE_MENU_ID);
	}
}
