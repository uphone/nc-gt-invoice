package nc.greentown.z90111;

@SuppressWarnings("serial")
public class ClientUI_PJ2 extends ClientUI_PJ {
	protected nc.ui.trade.manage.ManageEventHandler createEventHandler() {
		return new EventHandler2(this, getUIControl());
	}
}
