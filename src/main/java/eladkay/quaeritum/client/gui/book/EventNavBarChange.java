package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.eventbus.EventCancelable;

public class EventNavBarChange extends EventCancelable {

	private final int page;

	public EventNavBarChange(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}
}
