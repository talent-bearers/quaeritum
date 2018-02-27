package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import eladkay.quaeritum.api.book.IBookGui;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentBookMark extends ComponentAnimatableVoid {

    private static Set<ComponentBookMark> bookMarks = new HashSet<>();

    private final IBookGui book;
    private final int id;
    private final Sprite box;

    private ComponentSprite bar;

    public ComponentBookMark(IBookGui book, Sprite icon, int id) {
        super(book.getMainComponent().getSize().getXi() - 10,
                20 + 5 * id + book.bookmarkSprite().getHeight() * id,
                book.bookmarkSprite().getWidth(), book.bookmarkSprite().getHeight());
        this.book = book;
        this.id = id;

        bookMarks.add(this);

        box = book.bookmarkSprite();

        clipping.setClipToBounds(true);

        animX = -box.getWidth() + 20;

        bar = new ComponentSprite(book.bookmarkSprite(), -box.getWidth() + 20, 0);
        bar.getColor().setValue(book.getBook().bookColor);
        add(bar);

        ComponentSprite iconComponent = new ComponentSprite(icon, getSize().getXi() - icon.getWidth() - 8, 1);
        bar.add(iconComponent);
    }

    @Nullable
    private static ComponentBookMark getBookMarkFromID(int id) {
        for (ComponentBookMark bookMark : bookMarks) {
            if (bookMark.getId() == id) return bookMark;
        }

        return null;
    }

    public static int getNextId() {
        int largest = 0;
        for (ComponentBookMark bookMark : bookMarks) {
            if (bookMark.getId() > largest) largest = bookMark.getId();
        }

        return ++largest;
    }

    public void slideOutShort() {
        BasicAnimation mouseOutAnim = new BasicAnimation<>(bar, "pos.x");
        mouseOutAnim.setDuration(10);
        mouseOutAnim.setEasing(Easing.easeOutQuart);
        mouseOutAnim.setTo(-40);
        bar.add(mouseOutAnim);
    }

    public void slideOutLong() {
        BasicAnimation mouseOutAnim = new BasicAnimation<>(bar, "pos.x");
        mouseOutAnim.setDuration(10);
        mouseOutAnim.setEasing(Easing.easeOutQuart);
        mouseOutAnim.setTo(0);
        bar.add(mouseOutAnim);
    }

    public void slideIn() {
        BasicAnimation mouseOutAnim = new BasicAnimation<>(bar, "pos.x");
        mouseOutAnim.setDuration(10);
        mouseOutAnim.setEasing(Easing.easeOutQuart);
        mouseOutAnim.setTo(-box.getWidth() + 20);
        bar.add(mouseOutAnim);
    }

    public int getId() {
        return id;
    }

    public IBookGui getBook() {
        return book;
    }
}
