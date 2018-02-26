package eladkay.quaeritum.client.gui.book;

import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author WireSegal
 * Created at 8:52 AM on 2/26/18.
 */
@SideOnly(Side.CLIENT)
public interface ISearchAlgorithm {
    @Nullable List<? extends Result> search(String input);

    default Consumer<String> textBoxConsumer(GuiBook book, Supplier<Acceptor> newComponent) {
        return (input) -> {
            Acceptor acceptor = book.focus instanceof Acceptor ?
                    (Acceptor) book.focus :
                    newComponent.get();

            acceptor.acceptResults(search(input));

            if (book.focus instanceof ComponentSearchResults)
                book.forceInFocus(acceptor);
            else
                book.placeInFocus(acceptor);
        };
    }

    interface Result extends Comparable<Result> {
        double frequency();

        boolean specificResults();

        Entry found();

        @Override
        default int compareTo(@NotNull ISearchAlgorithm.Result o) {
            return Double.compare(o.frequency(), frequency());
        }
    }

    interface Acceptor extends IBookElement {
        void acceptResults(@Nullable List<? extends Result> results);
    }
}
